# Migrating from Kotpref to Jetpack DataStore

Kotpref is no longer maintained. This guide covers the parts of the move to
[Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore/preferences-datastore)
that are specific to Kotpref — how its delegates, key names and preference file
names map across — and links to the official documentation for everything else.

Preferences DataStore is the closest match to how Kotpref stored things, which is
why this guide targets it. [Proto DataStore](https://developer.android.com/topic/libraries/architecture/datastore/proto-datastore)
is the other option, and a reasonable one: a `KotprefModel` was already a typed
object, and Proto DataStore gives that a real schema instead of loose keys. It
needs a serializer and a `migrate` lambda mapping the old values onto your type,
but the key-name and file-name notes below apply either way.

> [!NOTE]
> This was written when the project was archived and has not been verified against
> a real migration. Treat it as a starting point, and check the behaviour against
> your own data before shipping.

The published Kotpref artifacts stay on Maven Central, so nothing breaks if you
migrate gradually — or not at all.

## Delegate mapping

Each delegate maps onto a `Preferences.Key<T>` plus a default value that you now
supply at the read site: `preferences[key]` returns `null` when the key is absent,
and DataStore has no notion of a stored default.

| Kotpref delegate | Kotpref default | DataStore key | Read |
| --- | --- | --- | --- |
| `stringPref()` | `""` | `stringPreferencesKey` | `prefs[KEY] ?: ""` |
| `nullableStringPref()` | `null` | `stringPreferencesKey` | `prefs[KEY]` |
| `intPref()` | `0` | `intPreferencesKey` | `prefs[KEY] ?: 0` |
| `longPref()` | `0L` | `longPreferencesKey` | `prefs[KEY] ?: 0L` |
| `floatPref()` | `0f` | `floatPreferencesKey` | `prefs[KEY] ?: 0f` |
| `booleanPref()` | `false` | `booleanPreferencesKey` | `prefs[KEY] ?: false` |
| `stringSetPref()` | empty set | `stringSetPreferencesKey` | `prefs[KEY] ?: emptySet()` |

### Key names

The string passed to `xxxPreferencesKey(...)` **must match the key Kotpref used**,
or migrated data will not be found. Kotpref's rule is `key ?: property.name`: the
property name, unless the delegate was given an explicit `key`.

```kotlin
var highScore by longPref()                     // key: "highScore"
var useFunc1 by booleanPref(key = "use_func1")  // key: "use_func1"
```

## Rewriting a KotprefModel

```kotlin
// Before
object UserInfo : KotprefModel() {
    var name by stringPref()
    var age by intPref(default = 14)
}

UserInfo.name = "chibatching"
val age = UserInfo.age
```

```kotlin
// After
private val Context.userInfoDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

class UserInfoRepository(private val dataStore: DataStore<Preferences>) {

    private val NAME = stringPreferencesKey("name")
    private val AGE = intPreferencesKey("age")

    val name: Flow<String> = dataStore.data.map { it[NAME] ?: "" }
    val age: Flow<Int> = dataStore.data.map { it[AGE] ?: 14 }

    suspend fun setName(value: String) {
        dataStore.edit { it[NAME] = value }
    }
}
```

Taking the `DataStore` as a constructor parameter rather than keeping an `object`
makes the class easy to fake in tests. If a screen needs several values at once,
map them into a single data class in one `map { }` rather than combining flows.

Other equivalents:

- `bulk { }` / `blockingBulk { }` → a single `edit { }`, which is atomic and
  suspends until the write is durable. `commitByDefault` and
  `commitAllPropertiesByDefault` have no counterpart and can be dropped.
- `clear()` → `dataStore.edit { it.clear() }`
- `remove(UserInfo::age)` → `dataStore.edit { it.remove(AGE) }`
- `Kotpref.init(context)` and the `initializer` module → nothing; the
  `preferencesDataStore` delegate creates the instance lazily.

## Synchronous `var` to `suspend` / `Flow`

This is where the real work is. Kotpref reads hit an in-memory SharedPreferences
map, so `UserInfo.age` returns immediately from anywhere. DataStore reads are a
`Flow` and writes are `suspend`, by design, so that disk I/O never runs on the
main thread. Three consequences to plan for:

1. **You cannot read a value at an arbitrary point in synchronous code.** Something
   like `if (UserInfo.isLoggedIn)` inside a click listener has to become either a
   collected state value or a `suspend` call inside a coroutine.
2. **The first emission is not instantaneous.** There is a window at startup where
   the value is not available yet. Decide per screen whether that means a loading
   state or a default value.
3. **Reads are a stream, not a snapshot.** Usually an upgrade: the UI updates by
   itself when the value changes, which previously needed `livedata-support`.

For a genuine one-shot read — a `WorkManager` worker, an interceptor building a
header — use `dataStore.data.map { it[KEY] }.first()`. Reach for
`runBlocking { }` only where there is no alternative; it blocks the calling thread
and reintroduces exactly the jank DataStore avoids.

For plumbing the flow into a UI, the standard patterns apply and are documented
upstream: [`stateIn`](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
in a `ViewModel` with an explicit initial value,
[`collectAsStateWithLifecycle()`](https://developer.android.com/topic/libraries/architecture/compose#state-flow)
in Compose, `repeatOnLifecycle` in Views, or `.asLiveData()` if you want to keep
LiveData at the boundary. One thing to watch: writes are now fire-and-forget from
the caller's side, so do not write and then read the value back on the next line —
collect the flow instead.

## Migrating existing XML data

[`SharedPreferencesMigration`](https://developer.android.com/topic/libraries/architecture/datastore/preferences-datastore#datastore-sharedpreferences)
copies an existing SharedPreferences file into DataStore on first read, then
deletes it. Keys and types carry over as-is — which is why the key names above
must match.

### Which name do I pass?

`sharedPreferencesName` is the file name **without the `.xml` extension**, and
Kotpref derives it as follows:

- **`kotprefName` not overridden** → the model's simple class name.
  `object UserInfo : KotprefModel()` writes `UserInfo.xml`, so pass
  **`"UserInfo"`**, capitalised exactly as the class is.
- **`kotprefName` overridden** → that exact string.
  `override val kotprefName: String = "user_info"` → pass `"user_info"`.

Getting this wrong fails silently: no file is found, DataStore starts empty, and
every read falls back to your defaults — which looks identical to a working
migration. Confirm the real name on a device that has existing data:

```
adb shell run-as your.package.name ls shared_prefs/
```

```kotlin
private val Context.userInfoDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_info",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context = context,
                sharedPreferencesName = "UserInfo", // Kotpref's kotprefName — the class name by default
            )
        )
    }
)
```

The DataStore's own `name` is unrelated and does not have to match. Each
`KotprefModel` had its own XML file, so one DataStore per model is the natural
mapping; several migrations can feed one DataStore, but watch for key collisions,
since two models can both have a `name` property without conflicting today.

Passing `keysToMigrate` limits which keys move, in which case only those keys are
removed and the XML file survives if others remain. Otherwise the file is deleted
once everything has been migrated — so to retest, clear app storage, install the
old version, create data, and upgrade. Worth doing once before release.

## Optional modules

| Module | Replacement |
| --- | --- |
| `initializer` | Not needed — DataStore has no global initialisation step. |
| `livedata-support` | Built in. `asLiveData(UserInfo::name)` becomes `dataStore.data.map { it[NAME] }`, plus `.asLiveData()` if you still need LiveData. |
| `enum-support` | Store it yourself. `enumValuePref` persisted `Enum.name`, so read with `enumValueOf<T>(string)` guarded by `runCatching`. `enumOrdinalPref` persisted the ordinal — migrating is a good moment to switch to name-based storage, since reordering the enum silently changes the meaning of stored data. |
| `gson-support` | Either keep serialising to a string key yourself (existing JSON migrates across as a plain string, so you can keep reading it with Gson), or move to [Proto DataStore](https://developer.android.com/topic/libraries/architecture/datastore/proto-datastore) with a typed `Serializer` — the better home for structured objects. |
| `preference-screen-dsl` | No equivalent. `androidx.preference` is built directly on SharedPreferences and reads synchronously, so it does not fit DataStore's model. Either build the settings UI yourself, keep `androidx.preference` and SharedPreferences for that one screen (fine, as long as each key has exactly one owner), or bridge with a custom `PreferenceDataStore` — possible, but its API is synchronous, which defeats much of the point. This module was always experimental. |

## Other behaviour differences

- **Read-modify-write is safe inside `edit { }`.** Concurrent edits are serialised,
  unlike `prefs.getInt(...) + 1` followed by a `put`.
- **`stringSetPref` returned a mutable set** whose mutations were written back
  (`UserInfo.prizes.add("Gold")`). DataStore returns an immutable `Set<String>`;
  replace the whole value: `prefs[PRIZES] = (prefs[PRIZES] ?: emptySet()) + "Gold"`.
- **No `MODE_MULTI_PROCESS`.** If you relied on `kotprefMode`, multi-process access
  needs `MultiProcessDataStoreFactory`; opening one file from two processes with the
  standard delegate corrupts it.
- **One instance per file.** Creating two DataStores over the same file name in one
  process throws. Declare the delegate once, at top level.
- **Corruption surfaces as an `IOException`** from `dataStore.data`; the usual
  handling is `.catch { if (it is IOException) emit(emptyPreferences()) else throw it }`.
- **Testing** no longer needs Robolectric for the storage layer — build a DataStore
  over a temporary folder in a plain JVM test, or fake the repository outright.

---

Thanks for having used Kotpref. This repository is archived and cannot take
corrections, but this guide is Apache-2.0 like the rest of the project — copy and
fix it freely.
