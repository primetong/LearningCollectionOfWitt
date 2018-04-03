在将 SimpleXML 序列化过后的天气数据 `WthrcdnData` 转换为 UI 呈现的 `WeatherData` 的过程时，会面临一个较为尴尬的问题，需要链式检查值是否为空。

这是因为服务端的天气数据格式并不严格统一，为了加强健壮性，我们允许大多数元素为 `null`。而 `WthrcdnData` 本身又是一个多级嵌套的 Bean。

在面对例如 `x.y = a.b.c` 这样的赋值过程中，最坏会这样使用：

```java
if (a != null && a.getB() != null && a.getB().getC() != null)
	x.setY(a.getB().getC());
```

或者使用不太好的忽略异常的方式：

```java
try { x.setY(a.getB().getC()); }
catch (NullPointerException e) {}
```

或者一些来自 Java 8 的其它方式，甚至是对这种情况进行重构：

- [Null check chain vs catching NullPointerException](https://stackoverflow.com/questions/37960674)
- [Java - ignore exception and continue](https://stackoverflow.com/questions/8436306/java-ignore-exception-and-continue)

在一些例如使用到数组的场合，情况会更加坏：

```java
dst.setDescription(src.forecast.get(0).day.type);
```

相比之下，假如使用 Kotlin 则会安全和简单很多：

```kotlin
dst.description = src.forecast?.firstOrNull()?.day?.type?:defaultValue
```

另外，Kotlin 中的 `data class` 比起 `Java Bean` 也要方便很多。

Java 版本：

```java
public class WeatherData {

    private String cityCode;
    private String city;
    // ......

    public String getCityCode() {
        return cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    // ......
}
```

Kotlin 版本（见：[Data Classes](http://kotlinlang.org/docs/reference/data-classes.html)）：

```kotlin
data class WeatherData(
    var cityCode: String = "",
    // ......
)
```

作为 [Android 官方推荐的开发语言](https://developer.android.com/kotlin/index.html)，Kotlin 简洁、可以无缝与 Java 集成，它还有其它很多优点，这里不再赘述。

- [Get Started with Kotlin on Android](https://developer.android.com/kotlin/get-started.html)
- [Getting started with Android and Kotlin](https://kotlinlang.org/docs/tutorials/kotlin-android.html)