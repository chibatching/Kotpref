# Module moshi-support

Serialize/Deserialize json object to KotprefModel using Moshi

### Set up

Pass the application context to Kotpref

```kotlin
Kotpref.moshi = Moshi.Builder()
    .add(
        Date::class.java,
        Rfc3339DateJsonAdapter().nullSafe()
    ) // optional if date needed com.squareup.moshi:moshi-adapters
    .build()
```
