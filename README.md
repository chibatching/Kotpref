# kotpref

SharedPreference delegation for Kotlin.

```kotlin
public class MainActivity : AppCompatActivity() {

    private var userName: String by Kotpref.stringPrefVar({ this })
    private var userAge: Int by Kotpref.intPrefVar({ this }, default = 18)
    private var userScore: Long by Kotpref.longPrefVar({ this })
    private var threshold: Float by Kotpref.floatPrefVar({ this })
    private var enableFunc: Boolean by Kotpref.booleanPrefVar({ this }, default = true)
    private var itemCart: MutableSet<String> by Kotpref.stringSetPrefVar({ this }, default = TreeSet<String>())

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userName = "chibatching"
        userAge = 30
        userScore = 212434085
        threshold = 0.3551f
        enableFunc = false
        itemCart.add("aaa")
        itemCart.add("bbb")
        itemCart.add("ccc")
    }
}
```

Result

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
<long name="userScore" value="212434085" />
<string name="userName">chibatching</string>
<set name="itemCart">
<string>aaa</string>
<string>bbb</string>
<string>ccc</string>
</set>
<boolean name="enableFunc" value="false" />
<int name="userAge" value="30" />
<float name="threshold" value="0.3551" />
</map>
```