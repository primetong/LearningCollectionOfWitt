# AsyncTask 的安全隐患

从 Android Studio 3.0 起，在 Activity 中，以非静态或者匿名内部类的方式使用 [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html) 会被警告：

> [This AsyncTask class should be static or leaks might occur](https://stackoverflow.com/questions/44309241)

究其原因，AsyncTask 或者 Handler 之类的，生命周期与 [Activity 的生命周期](https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle) 并不吻合。参考[这里](https://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html)的解释。除此之外，AsyncTask 在一些场合，例如屏幕旋转（导致 Activity 重新启动）等会[出现问题](https://stackoverflow.com/questions/4584015)。

为了规避这些问题，往往将 AsyncTask 声明为静态类，并存储一个 Activity 的弱引用 `WeakReference` 使得它可以和 UI 线程交互。同时，也要在导致 Activity 重新启动的场合，保存当前状态并结束这个 AsyncTask，最后再恢复等等操作。

对于拉取数据的这类任务，我们有一些更安全的解决方法：

- [Loader](https://developer.android.com/guide/components/loaders.html)，可与参考这篇[介绍](https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832)
- [IntentService](https://developer.android.com/training/run-background-service/create-service.html)

注：IntentService 无法与 UI 线程直接交互，往往通过：[ResultReceiver](https://developer.android.com/reference/android/os/ResultReceiver.html) 或者 [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver.html) 来实现 UI 线程的数据更新。这里使用了更为简单的第三方库 [EventBus](https://github.com/greenrobot/EventBus) 来进行事件响应。

# 使用 [IntentService](https://developer.android.com/training/run-background-service/create-service.html) + [EventBus](https://github.com/greenrobot/EventBus) 拉取数据

这里采用了 [IntentService](https://developer.android.com/training/run-background-service/create-service.html) + [EventBus](https://github.com/greenrobot/EventBus) 实现了发送数据请求和接受数据后的更新界面。

想要开始执行一个 IntentService 也比较简单：

```kotlin
// 创建一个 Intent
Intent(Intent.ACTION_SYNC, null, this, PullJsonService::class.java).apply {
    // 写入必要的参数到 Intent
    putExtra(PullJsonService.Contract.REQUEST.name, QueryUser.Request(/*...*/))
    // 发送这个 Intent 到目标 IntentService
    startService(this)
}
```

IntentService 本身结构很简单：

```kotlin
class PullJsonService: IntentService(PullJsonService::class.java.name)
{
    override fun onHandleIntent(intent: Intent?) {
        // 从 Intent 中获取参数
        val args = (get(Contract.REQUEST.name) as? IRequest)
                 ?: // 没有参数下的异常处理
      
        // 要处理的任务
        // ...
      
        // 发送这个事件
        EVENT_BUS.post(JsonEvent(json))
    }

    // 待发送的事件
    data class JsonEvent(val json: String)
  
    // 约定好的 Intent 中的参数名称
    enum class Contract {
        REQUEST,
    }
  
    companion object {
        // 默认的 EventBus 实例，用于发送事件
        private val EVENT_BUS = EventBus.getDefault()
    }
}
```

# EventBus 的使用

根据官方教程，我们需要在 gradle 中加入相关：

> compile 'org.greenrobot:eventbus:3.1.1'

在需要响应 `JsonEvent` 事件的 Activity 里，使用 EventBus 订阅事件

> [EventBus in 3 steps](https://github.com/greenrobot/EventBus#eventbus-in-3-steps)

注：关于在 Activity 生命周期中，那些合适地方进行 EventBus 的注册和取消注册，[这里](https://stackoverflow.com/questions/29236704)有一些讨论。

这里我选了在 `onStart` 和 `onStop` 注册和取消注册。

```kotlin
class LoginActivity: AppCompatActivity() {
    // ......
  
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveJsonEvent(event: PullJsonService.JsonEvent) {
        // 处理该事件
    }
  
    override fun onStart() {
        super.onStart()
        EVENT_BUS.register(this)
    }

    override fun onStop() {
        EVENT_BUS.unregister(this)
        super.onStop()
    }
}
```

