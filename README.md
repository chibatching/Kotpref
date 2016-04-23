# Kotpref

Android SharedPreference delegation for Kotlin.

## Install

```groovy
compile 'com.chibatching:kotpref:1.3.0'
```

## How to use

### Declare preference model

```kotlin
object UserInfo : KotprefModel() {
    var name: String by stringPrefVar()
    var code: String? by stringNullablePrefVar()
    var age: Int by intPrefVar(default = 14)
    var highScore: Long by longPrefVar()
    var rate: Float by floatPrefVar()
    val prizes: MutableSet<String> by stringSetPrefVal {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPrefVal set
    }
}
```

### Set up

```kotlin
Kotpref.init(context)
```

### Read and Write

```kotlin
UserInfo.name = "chibatching"
UserInfo.code = "DAEF2599-7FC9-49C5-9A11-3C12B14A6898"
UserInfo.age = 30
UserInfo.highScore = 49219902
UserInfo.rate = 49.21F
UserInfo.prizes.add("Bronze")
UserInfo.prizes.add("Silver")
UserInfo.prizes.add("Gold")

Log.d(TAG, "User name: ${UserInfo.name}")
Log.d(TAG, "User code: ${UserInfo.code}")
Log.d(TAG, "User age: ${UserInfo.age}")
Log.d(TAG, "User high score: ${UserInfo.highScore}")
Log.d(TAG, "User rate: ${UserInfo.rate}")
UserInfo.prizes.forEachIndexed { i, s -> Log.d(TAG, "prize[$i]: ${s}") }
```

### Bulk edit

```kotlin
UserInfo.bulk {
    name = "chibatching Jr"
    code = "451B65F6-EF95-4C2C-AE76-D34535F51B3B"
    age = 2
    highScore = 3901
    rate = 0.4F
    prizes.clear()
    prizes.add("New Born")
}
```

### Result shared preference xml

XML file name equals model class name. If model class named `UserInfo`, XML file name is `UserInfo.xml`.

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <long name="highScore" value="49219902" />
    <set name="prizes">
        <string>Beginner</string>
        <string>Bronze</string>
        <string>Gold</string>
        <string>Silver</string>
    </set>
    <string name="name">chibatching</string>
    <string name="code">DAEF2599-7FC9-49C5-9A11-3C12B14A6898</string>
    <int name="age" value="30" />
    <float name="rate" value="49.21" />
</map>
```

### Options

#### Change default value

```kotlin
var age: Int by intPrefVar(18)
```

or

```kotlin
var age: Int by intPrefVar(default = 18)
```

#### Change preference key

You can custom preference key or use from string resources.

```kotlin
var useFunc1: Boolean by booleanPrefVar(key = "use_func1")
var mode: Int by intPrefVar(default = 1, key = R.string.pref_mode)
```

#### Change XML file name

Override `kotprefName` property.

```kotlin
object UserInfo : KotprefModel() {
    override val kotprefName: String = "user_info"
```

#### Change SharedPreference mode

Override `kotprefMode` property. Default is `Context.MODE_PRIVATE`.

```kotlin
object UserInfo : KotprefModel() {
    override val kotprefMode: Int = Context.MODE_MULTI_PROCESS
```

## License

```
Copyright 2015 chibatching

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
