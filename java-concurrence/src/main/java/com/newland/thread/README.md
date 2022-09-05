public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,
TimeUnit unit,BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler)

（1）corePoolSize

核心线程数，当有任务进来的时候，如果当前线程数还未达到 corePoolSize 个数，则创建核心线程，核心线程有几个特点：

1、当线程数未达到核心线程最大值的时候，新任务进来，即使有空闲线程，也不会复用，仍然新建核心线程；2、核心线程一般不会被销毁，即使是空闲的状态，但是如果通过方法 allowCoreThreadTimeOut(boolean value) 设置为 true 时，超时也同样会被销毁；3、生产环境首次初始化的时候，可以调用 prestartCoreThread() 方法来预先创建所有核心线程，避免第一次调用缓慢；

（2）maximumPoolSize

除了有核心线程外，有些策略是当核心线程完全无空闲的时候，还会创建一些临时的线程来处理任务，maximumPoolSize 就是核心线程 + 临时线程的最大上限。临时线程有一个超时机制，超过了设置的空闲时间没有事儿干，就会被销毁。

（3）keepAliveTime

这个就是上面两个参数里所提到的超时时间，也就是线程的最大空闲时间，默认用于非核心线程，通过 allowCoreThreadTimeOut(boolean value) 方法设置后，也会用于核心线程。

（4）unit

这个参数配合上面的 keepAliveTime ，指定超时的时间单位，秒、分、时等。

（5）workQueue

等待执行的任务队列，如果核心线程没有空闲的了，新来的任务就会被放到这个等待队列中。

（6）threadFactory

它是一个接口，用于实现生成线程的方式、定义线程名格式、是否后台执行等等，可以用 Executors.defaultThreadFactory() 默认的实现即可，也可以用 Guava 等三方库提供的方法实现，如果有特殊要求的话可以自己定义。它最重要的地方应该就是定义线程名称的格式，便于排查问题了吧。

（7）handler

当没有空闲的线程处理任务，并且等待队列已满（当然这只对有界队列有效），再有新任务进来的话，就要做一些取舍了，而这个参数就是指定取舍策略的，有下面四种策略可以选择：

ThreadPoolExecutor.AbortPolicy：直接抛出异常，这是默认策略；
ThreadPoolExecutor.DiscardPolicy：直接丢弃任务，但是不抛出异常。
ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后将新来的任务加入等待队列
ThreadPoolExecutor.CallerRunsPolicy：由线程池所在的线程处理该任务，比如在 main 函数中创建线程池，如果执行此策略，将有 main 线程来执行该任务

