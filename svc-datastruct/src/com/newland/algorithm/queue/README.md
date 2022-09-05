java中的各种并发Queue可以归为以下的几种：　

ConcurrentLinkedQueue： 一个由链表结构组成的非阻塞队列
ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列
LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列
PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列
DelayQueue：一个使用优先级队列实现的无界阻塞队列
SynchronousQueue：一个不存储元素的阻塞队列
LinkedTransferQueue：一个由链表结构组成的无界阻塞队列
LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列
　　ConcurrentLinkedQueue

ConcurrentLinkedQueue不能阻塞队列，但是速度快。在不需要阻塞的情况下，应该优选ConcurrentLinkedQueue。

ArrayBlockingQueue

ArrayBlockingQueue是一个用数组实现的有界阻塞队列。在队列大小固定的情况下是优先选择，入队出队只有一把锁，锁的竞争会比较激烈。

LinkedBlockingQueue

LinkedBlockingQueue是一个用链表实现的有界阻塞队列，此队列的默认和最大长度为Integer.MAX_VALUE。在队列大小没有限制的情况下优先选择。入队和出队做了锁分离，对于锁的竞争会比较小。

由于LinkedBlockingQueue是基于链表实现的，当队列容量较大，做查找操作时会比较耗时。

PriorityBlockingQueue

PriorityBlockingQueue是一个支持优先级的无界队列。默认情况下元素采取自然顺序排列，也可以通过比较器comparator来指定元素的排序规则。

需要对队列中的元素做排序操作时，PriorityBlockingQueue是唯一的选择。

DelayQueue

DelayQueue一个支持延时获取元素的无界阻塞队列。队列中的每个元素都有一个延迟时间，只有当延迟时间到了之后才能执行出队操作。

DelayQueue中锁添加的元素必须实现Delayed接口。

SynchronousQueue
SynchronousQueue是一个不存储元素的阻塞队列。每一个put操作必须等待一个take操作的完成，否则不能继续添加元素。

SynchronousQueue可以用于快速交换元素，具有最快的速度。

LinkedTransferQueue　
　　 LinkedTransferQueue是一个由链表结构组成的无界阻塞队列。相对于其他阻塞队列LinkedTransferQueue多了tryTransfer和transfer方法。

transfer方法可以把生产者传入的元素立刻transfer（传输）给消费者。如果没有消费者在等待接收元素，transfer方法会将元素存放在队列的末端，并等到该元素被消费者消费了才返回。
tryTransfer则是用来试探下生产者传入的元素是否能直接传给消费者。如果没有消费者等待接收元素，则立即返回false。

LinkedBlockingDeque
LinkedBlockingDeque是一个由链表结构组成的双向阻塞队列。不同于其他队列，入队只能在队尾，出队只能在队头，入队和出队可以发生在队尾或者队头。

相比其他的阻塞队列，LinkedBlockingDeque多了addFirst，addLast，offerFirst，offerLast，peekFirst，peekLast等方法。

