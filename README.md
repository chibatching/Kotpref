# Kotpref

SharedPreference delegation for Kotlin.

## Install

```
compile 'com.chibatching:kotpref:0.2.0'
```

## How to use

### Declare preference model

```kotlin
public object UserInfo : KotprefModel() {
    var name: String by stringPrefVar()
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
UserInfo.age = 30
UserInfo.highScore = 49219902
UserInfo.rate = 49.21F
UserInfo.prizes.add("Bronze")
UserInfo.prizes.add("Silver")
UserInfo.prizes.add("Gold")

Log.d(javaClass.getSimpleName(), "User name: ${UserInfo.name}")
Log.d(javaClass.getSimpleName(), "User age: ${UserInfo.age}")
Log.d(javaClass.getSimpleName(), "User high score: ${UserInfo.highScore}")
Log.d(javaClass.getSimpleName(), "User rate: ${UserInfo.rate}")
UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.getSimpleName(), "prize[$i]: ${s}") }
```

### Bulk edit

```kotlin
Kotpref.bulk(UserInfo) {
    name = "chibatching Jr"
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

#### Change XML file name

Override `kotprefName` property.

```kotlin
public object UserInfo : KotprefModel() {
    override val kotprefName: String = "user_info"
```

#### Change SharedPreference mode

Override `kotprefMode` property. Default is `Context.MODE_PRIVATE`.

```kotlin
public object UserInfo : KotprefModel() {
    override val kotprefMode: Int = Context.MODE_MULTI_PROCESS
```

## Proguard

```
-keepnames class * extends com.chibatching.kotpref.KotprefModel
-dontwarn com.chibatching.kotpref.**
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
