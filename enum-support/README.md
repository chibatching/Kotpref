# Module enum-support

Saving enum value to KotprefModel as String

```kotlin
object UserSetting : KotprefModel() {
    var gameLevel by enumValuePref(GameLevel.NORMAL)
}

enum class GameLevel {
    EASY,
    NORMAL,
    HARD
}
```
