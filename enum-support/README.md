# Kotpref enum-sppurt

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
