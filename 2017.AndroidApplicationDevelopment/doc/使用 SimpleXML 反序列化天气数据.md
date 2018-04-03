在课程提供的资料中，并没有方便的方式用于解析略不方便的 XML 天气数据：

```xml
<resp>
  <city>北京</city>
  <updatetime>20:11</updatetime>
  <wendu>11</wendu>
  <fengli>
    <![CDATA[ 1级 ]]>
  </fengli>
  <shidu>75%</shidu>
  <fengxiang>东风</fengxiang>
  <sunrise_1>06:22</sunrise_1>
  <sunset_1>17:38</sunset_1>
  <sunrise_2/><sunset_2/>
  <environment>
    <aqi>72</aqi>
    <pm25>52</pm25>
    <suggest>极少数敏感人群应减少户外活动</suggest>
    <quality>良</quality>
    <!-- 略 -->
```

这些数据的不便之处在于——某些数据项会随着城市代码的不同而缺失。

使用课程中提到的 [`XmlPullParser`](https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser.html) 手动解析并处理这些情况不太方便。因此这里推荐使用 [SimpleXML](http://simple.sourceforge.net/) 解析这些天气数据。[SimpleXML](http://simple.sourceforge.net/) 的操作与 [Gson](https://github.com/google/gson) 类似，甚至支持 [ElementUnion](http://simple.sourceforge.net/download/stream/doc/javadoc/org/simpleframework/xml/ElementUnion.html) ，使用起来很方便。

这里有一些官方文档：

- [Tutorial](http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php)
- [Examples](http://simple.sourceforge.net/download/stream/doc/examples/examples.php)

[SimpleXML 提供了 Maven 包](http://simple.sourceforge.net/maven.php)，我们可以在[这里](http://mvnrepository.com/artifact/org.simpleframework/simple-xml/2.7.1)找到对 Gradle 的支持。

因此我们可以直接在 `app/build.gradle` 的 `dependencies` 中添加：

```
compile('org.simpleframework:simple-xml:2.7.+'){
    exclude module: 'stax'
    exclude module: 'stax-api'
    exclude module: 'xpp3'
}
```

参考：[Using SimpleXML with Android and Gradle](https://stackoverflow.com/questions/18084285/using-simplexml-with-android-and-gradle)

在解析之前，我们需要对该 XML 数据建立对应的 Bean：

```java
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import java.util.List;

@Root(name="resp")
public class WthrcdnData
{
    @Element    (required = true ) public String city;
    @Element    (required = true ) public String updatetime;
    @Element    (required = false) public String wendu;
    @Element    (required = true ) public String fengli;
    @Element    (required = true ) public String shidu;
    @Element    (required = true ) public String fengxiang;
    @Element    (required = false) public String sunrise_1;
    @Element    (required = false) public String sunset_1;
    @Element    (required = false) public String sunrise_2;
    @Element    (required = false) public String sunset_2;
    @Element    (required = false) public Environment environment;
    @Element    (required = false) public Yesterday yesterday;
    @ElementList(required = true ) public List<Weather> forecast;
    @ElementList(required = false) public List<Zhishu> zhishus;

    @Root public static class Environment {
        @Element(required = false) public String aqi;
        @Element(required = true ) public String pm25;
        @Element(required = false) public String suggest;
        @Element(required = true ) public String quality;
        @Element(required = false) public String MajorPollutants;
        @Element(required = false) public String o3;
        @Element(required = false) public String co;
        @Element(required = false) public String pm10;
        @Element(required = false) public String so2;
        @Element(required = false) public String no2;
        @Element(required = false) public String time;
    }
 
    @Root public static class Yesterday {
        @Element(required = false) public String date_1;
        @Element(required = false) public String high_1;
        @Element(required = false) public String low_1;
        @Element(required = false) public HalfDay day_1;
        @Element(required = false) public HalfDay night_1;

        @Root public static class HalfDay {
            @Element(required = false) public String type_1;
            @Element(required = false) public String fx_1;
            @Element(required = false) public String fl_1;
        }
    }

    @Root public static class Weather {
        @Element(required = true ) public String date;
        @Element(required = true ) public String high;
        @Element(required = true ) public String low;
        @Element(required = false) public HalfDay day;
        @Element(required = false) public HalfDay night;

        @Root public static class HalfDay {
            @Element(required = true ) public String type;
            @Element(required = false) public String fengxiang;
            @Element(required = false) public String fengli;
        }
    }
  
    @Root public static class Zhishu {
        @Element(required = false) public String name;
        @Element(required = false) public String value;
        @Element(required = false) public String detail;
    }
}
```

注：这里没有使用 `Getter` 与 `Setter`，参考该问题的讨论：[Struct like objects in Java](https://stackoverflow.com/a/8565711)

这之后，我们可以在需要的地方进行解析了：

```java
Serializer serializer = new Persister();
WthrcdnData src = serializer.read(WthrcdnData.class, xml);
```

对于Kotlin 版本，类似的有：

```kotlin
@Root(name = "resp")
class WthrcdnData
{
    @field:Element(required = true ) var city        : String? = null
    @field:Element(required = true ) var updatetime  : String? = null
    @field:Element(required = false) var wendu       : String? = null
    @field:Element(required = true ) var fengli      : String? = null
    @field:Element(required = true ) var shidu       : String? = null
    @field:Element(required = true ) var fengxiang   : String? = null
    @field:Element(required = false) var sunrise_1   : String? = null
    @field:Element(required = false) var sunset_1    : String? = null
    @field:Element(required = false) var sunrise_2   : String? = null
    @field:Element(required = false) var sunset_2    : String? = null
    @field:Element(required = false) var environment : Environment? = null
    @field:Element(required = false) var yesterday   : Yesterday? = null
    @field:ElementList(required = true ) var forecast: List<Weather>? = null
    @field:ElementList(required = false) var zhishus : List<Zhishu >? = null

    // ...省略...
}
```

