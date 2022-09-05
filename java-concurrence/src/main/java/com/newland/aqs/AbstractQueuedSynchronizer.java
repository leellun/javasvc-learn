package com.newland.aqs;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

/**
 * AQS注释类
 */
public abstract class AbstractQueuedSynchronizer
        extends AbstractOwnableSynchronizer
        implements java.io.Serializable {

    private static final long serialVersionUID = 7373984972572414691L;

    protected AbstractQueuedSynchronizer() { }

    static final class Node {
        /** 标记，表示节点在共享模式下等待 */
        static final Node SHARED = new Node();
        /** 标记，表示节点在独占模式下等待 */
        static final Node EXCLUSIVE = null;

        /** waitStatus值表示线程已经取消。 */
        static final int CANCELLED =  1;
        /** waitStatus值表示后继线程需要解除驻留。 */
        static final int SIGNAL    = -1;
        /** waitStatus值表示线程正在等待。 */
        static final int CONDITION = -2;
        /**
         * waitStatus值表示下一个被请求的shared应该无条件传播。
         */
        static final int PROPAGATE = -3;

        /**
         *  当前等待状态
         */
        volatile int waitStatus;

        /**
         * 前一个节点
         */
        volatile Node prev;

        /**
         * 下一个节点
         */
        volatile Node next;

        /**
         * 当前节点所在线程
         */
        volatile Thread thread;

        /**
         * 下一个等待节点或特殊值SHARED
         */
        Node nextWaiter;

        /**
         * 共享模式
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * 返回上一个节点
         */
        final Node predecessor() {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {}

        Node(Node nextWaiter) {
            this.nextWaiter = nextWaiter;
            THREAD.set(this, Thread.currentThread());
        }

        /** Constructor used by addConditionWaiter. */
        Node(int waitStatus) {
            WAITSTATUS.set(this, waitStatus);
            THREAD.set(this, Thread.currentThread());
        }

        /** CASes waitStatus field. */
        final boolean compareAndSetWaitStatus(int expect, int update) {
            return WAITSTATUS.compareAndSet(this, expect, update);
        }

        /** CASes next field. */
        final boolean compareAndSetNext(Node expect, Node update) {
            return NEXT.compareAndSet(this, expect, update);
        }

        final void setPrevRelaxed(Node p) {
            PREV.set(this, p);
        }

        // VarHandle mechanics
        private static final VarHandle NEXT;
        private static final VarHandle PREV;
        private static final VarHandle THREAD;
        private static final VarHandle WAITSTATUS;
        static {
            try {
                MethodHandles.Lookup l = MethodHandles.lookup();
                NEXT = l.findVarHandle(Node.class, "next", Node.class);
                PREV = l.findVarHandle(Node.class, "prev", Node.class);
                THREAD = l.findVarHandle(Node.class, "thread", Thread.class);
                WAITSTATUS = l.findVarHandle(Node.class, "waitStatus", int.class);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    /**
     * 头节点
     */
    private transient volatile AbstractQueuedSynchronizer.Node head;

    /**
     * 尾节点
     */
    private transient volatile AbstractQueuedSynchronizer.Node tail;

    /**
     * 同步状态
     */
    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * cas更新状态
     */
    protected final boolean compareAndSetState(int expect, int update) {
        return STATE.compareAndSet(this, expect, update);
    }

    // Queuing utilities

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices
     * to improve responsiveness with very short timeouts.
     */
    static final long SPIN_FOR_TIMEOUT_THRESHOLD = 1000L;

    /**
     * 将节点插入队列，必要时初始化。
     * 返回node节点的上一个节点，当前节点将成为新的 尾节点
     */
    private Node enq(Node node) {
        for (;;) {
            Node oldTail = tail;
            if (oldTail != null) {
                node.setPrevRelaxed(oldTail);
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    return oldTail;
                }
            } else {
                initializeSyncQueue();
            }
        }
    }

    /**
     * 为当前线程和给定模式创建和排队节点。独占或者共享节点
     * addWaiter与enq区别在于，addWaiter返回的是当前等待节点
     */
    private Node addWaiter(Node mode) {
        Node node = new Node(mode);

        for (;;) {
            Node oldTail = tail;
            if (oldTail != null) {
                //设置当前节点的上一个节点为尾部节点
                node.setPrevRelaxed(oldTail);
                //尾部节点替换为当前等待节点
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    //当前节点成功添加到尾部则返回
                    return node;
                }
            } else {
                //如果尾部节点为空则初始化尾部节点，然后再添加等待节点
                initializeSyncQueue();
            }
        }
    }

    /**
     * 设置头节点
     *
     * @param node the node
     */
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * 如果节点的后继节点存在，则唤醒它。
     */
    private void unparkSuccessor(Node node) {
        /*
         * 如果状态为负(即可能需要信号)，尝试清除信号。如果失败了，或者状态被等待线程改变了。
         */
        int ws = node.waitStatus;
        if (ws < 0)
            node.compareAndSetWaitStatus(ws, 0);

        /*
         * 要取消驻留的线程保存在后继节点中，后继节点通常是下一个节点。但如果被取消或明显为空，则从tail向后遍历以找到实际未被取消的后继对象。
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            /**
             * 为什么从尾部查找有效节点？
             * 因为aqs是节点从头向尾部添加，如果高并发情况下，从头部查找，可能出现当其中一个线程查找node.next的时候，最后一个节点还没有加入进去。
             * 而从尾部查找tail是volatile变量，只要tail设置了，那么就不会出现线程问题
             */
            for (Node p = tail; p != node && p != null; p = p.prev)
                if (p.waitStatus <= 0)
                    s = p;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

    /**
     * 为共享模式释放动作——信号后继和确保传播。(注:对于独占模式，只释放数量 如果需要信号，可以调用头部的unpark继承者。)
     */
    private void doReleaseShared() {
        /*
         * 确保发布传播，即使有其他正在进行的获取/发布。这以通常的方式进行，如果需要信号，尝试解除头部的park继承者。
         * 但是如果没有，状态设置为PROPAGATE，以确保在发布时传播继续。此外，我们必须进行循环，以防在执行此操作时添加了新节点。
         * 另外，与unpark后继器的其他使用不同，我们需要知道CAS重置状态是否失败，如果失败则需要重新检查。
         */
        for (;;) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                // 如果头节点的状态为通知状态，触发当前节点进入执行状态
                if (ws == Node.SIGNAL) {
                    //如果当前SIGNAL 状态改为0成功，则unpack该node的线程
                    if (!h.compareAndSetWaitStatus(Node.SIGNAL, 0))
                        continue;
                    unparkSuccessor(h);
                }
                //如果当前头节点的状态为0，说明执行状态或者等待执行，则设置状态为PROPAGATE（下一个被请求的shared应该无条件传播。）
                else if (ws == 0 &&
                        !h.compareAndSetWaitStatus(0, Node.PROPAGATE))
                    continue;                // 如果cas没有设置成功，则需要重新获取头节点，再次loop
            }
            if (h == head)                   // loop if head changed
                break;
        }
    }

    /**
     * 共享模式下，设置头节点，并且判断后续节点是否共享节点，如果是共享节点则回调doReleaseShared()方法进行释放资源
     * node节点 已经申请到资源，node节点将会设置成新的头节点
     *
     * @param node the node
     * @param propagate the return value from a tryAcquireShared
     */
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        setHead(node);
        /*
         * propagate是tryAcquireShared的返回值，这是决定是否传播唤醒的依据之一。
         * h.waitStatus为SIGNAL或者PROPAGATE时也根据node的下一个节点共享来决定是否传播唤醒，
         * 这里为什么不能只用propagate > 0来决定是否可以传播在本文下面的思考问题中有相关讲述。
         */
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }

    /**
     * 取消正在进行的收购尝试。
     * @param node the node
     */
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        node.thread = null;

        // 找到 node节点的有效prev节点
        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;
        // 记录pred节点的后继为predNext，后续CAS会用到。
        Node predNext = pred.next;
        // 直接把当前节点的等待状态置为取消,后继节点即便也在cancel可以跨越node节点。
        node.waitStatus = Node.CANCELLED;

        /*
         * 如果CAS将tail从node置为pred节点了
         * 则剩下要做的事情就是尝试用CAS将pred节点的next更新为null以彻底切断pred和node的联系。
         * 这样一来就断开了pred与pred的所有后继节点，这些节点由于变得不可达，最终会被回收掉。
         * 由于node没有后继节点，所以这种情况到这里整个cancel就算是处理完毕了。
         *
         * 这里的CAS更新pred的next即使失败了也没关系，说明有其它新入队线程或者其它取消线程更新掉了。
         */
        if (node == tail && compareAndSetTail(node, pred)) {
            pred.compareAndSetNext(predNext, null);
        } else {
            // 如果后继需要信号，试着设置pred的下一个链接，这样它就会得到一个。否则就唤醒它进行传播。
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && pred.compareAndSetWaitStatus(ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                /*
                 * 如果node的后继节点next非取消状态的话，则用CAS尝试把pred的后继置为node的后继节点
                 * 这里if条件为false或者CAS失败都没关系，这说明可能有多个线程在取消，总归会有一个能成功的。
                 */
                if (next != null && next.waitStatus <= 0)
                    pred.compareAndSetNext(predNext, next);
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    /**
     * 根据前驱节点中的waitStatus来判断是否需要阻塞当前线程。
     * 作用：
     * 1 清除取消节点
     * 2 将pred节点状态设置为SIGNAL
     * 返回：如果pred节点成功为SIGNAL返回true
     *
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            /*
             * 前驱节点设置为SIGNAL状态，在释放锁的时候会唤醒后继节点，
             * 所以后继节点（也就是当前节点）现在可以阻塞自己。
             */
            return true;
        if (ws > 0) {
            /*
             * 前驱节点状态为取消,向前遍历，更新当前节点的前驱为往前第一个非取消节点。
             * 当前线程会之后会再次回到循环并尝试获取锁。
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /**
             * 等待状态为0或者PROPAGATE(-3)，设置前驱的等待状态为SIGNAL,
             * 并且之后会回到循环再次重试获取锁。
             */
            pred.compareAndSetWaitStatus(ws, Node.SIGNAL);
        }
        return false;
    }

    /**
     * 中断当前线程的方便方法。
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * 阻塞节点，当唤醒当前线程时，检查是否中断
     */
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    /**
     * 以独占不可中断模式获取已在队列中的线程。用于条件等待方法以及获取方法。
     */
    final boolean acquireQueued(final Node node, int arg) {
        boolean interrupted = false;
        try {
            for (;;) {
                //上一个节点
                final Node p = node.predecessor();
                //如果是头节点，并且申请信号量成功，则设置当前节点为头节点
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return interrupted;
                }
                //如果p节点的状态为SIGNAL
                if (shouldParkAfterFailedAcquire(p, node))
                    interrupted |= parkAndCheckInterrupt();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            if (interrupted)
                selfInterrupt();
            throw t;
        }
    }

    /**
     * 以独占可中断模式获取。
     */
    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    /**
     * 以独占定时模式获取。
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.EXCLUSIVE);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return true;
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L) {
                    cancelAcquire(node);
                    return false;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > SPIN_FOR_TIMEOUT_THRESHOLD)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    /**
     * 以共享不可中断模式获取。
     * @param arg the acquire argument
     */
    private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        boolean interrupted = false;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    //如果当前节点的前置节点为头节点则获取资源
                    int r = tryAcquireShared(arg);
                    //如果资源允许则设置头节点为当前节点
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        return;
                    }
                }
                //如果当前节点的前置节点为SIGNAL，则等待前置节点通知
                if (shouldParkAfterFailedAcquire(p, node))
                    interrupted |= parkAndCheckInterrupt();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        } finally {
            if (interrupted)
                selfInterrupt();
        }
    }

    /**
     * 以共享可中断模式获取。
     * @param arg the acquire argument
     */
    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    /**
     * 共享模式带timeout的方式获取
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L) {
                    cancelAcquire(node);
                    return false;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > SPIN_FOR_TIMEOUT_THRESHOLD)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    // Main exported methods

    /**
     * 申请执行
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 释放
     */
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 尝试以共享模式获取。该方法应该查询对象的状态是否允许在共享模式中获取它，如果允许，则获取它。
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 尝试设置状态以反映共享模式下的释放。
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 如果同步是与当前(调用)线程独占的，返回{@code true}。
     */
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * 申请独占
     */
    public final void acquire(int arg) {
        //nextWaiter=Node.EXCLUSIVE=null 表示独占，通知节点为空
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

    /**
     * 以独占模式获取，如果中断则终止。
     */
    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    /**
     * 尝试以独占模式获取，如果被中断则终止，如果给定超时超时则失败。
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }

    /**
     * 以独占模式发布。如果{@link #tryRelease}返回true，则解除一个或多个线程的阻塞。
     *
     */
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            //如果头节点waitStatus 不为0，则可能为SIGNAL，CANCELLED，PROPAGATE，这三种情况都应该让后续节点继续执行
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    /**
     * 以共享模式获取，忽略中断。通过至少调用一次{@link # tryacquiresred}实现，成功返回。否则线程会排队，可能会反复阻塞和解阻塞，
     * 调用{@link# tryAcquireShared},直到成功。
     */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    /**
     * 以共享模式获取，考虑中断。通过至少调用一次{@link # tryacquiresred}实现，成功返回。否则线程会排队，可能会反复阻塞和解阻塞，
     */
    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    /**
     * 尝试在共享模式下获取，如果被中断将终止，如果给定超时超时将失败。首先检查中断状态，然后至少调用一次{@link # tryacquiresred}，成功返回。
     * 否则，线程会排队，可能会反复阻塞和解阻塞，调用{@link # tryacquiresred}，直到成功或线程被中断或超时。
     *
     * @param nanosTimeout 等待的最大纳秒数
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    /**
     * 共享模式的释放
     */
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    // Queue inspection methods

    /**
     * 查询是否有线程正在等待获取。
     */
    public final boolean hasQueuedThreads() {
        for (Node p = tail, h = head; p != h && p != null; p = p.prev)
            if (p.waitStatus <= 0)
                return true;
        return false;
    }

    /**
     * 头节点存在表示有值
     */
    public final boolean hasContended() {
        return head != null;
    }

    /**
     * 返回第一个线程
     */
    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        return (head == tail) ? null : fullGetFirstQueuedThread();
    }

    /**
     * Version of getFirstQueuedThread called when fastpath fails.
     */
    private Thread fullGetFirstQueuedThread() {
        /*
         * 这里判断两次，第二个节点有线程
         */
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;

        /*
         * 查找第一个有线程的节点线程
         */

        Thread firstThread = null;
        for (Node p = tail; p != null && p != head; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                firstThread = t;
        }
        return firstThread;
    }

    /**
     * 指定线程是否在同步队列中
     */
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }

    /**
     * 返回{@code true}如果明显的第一个排队的线程，如果存在，在独占模式等待。
     */
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return (h = head) != null &&
                (s = h.next)  != null &&
                !s.isShared()         &&
                s.thread != null;
    }

    /**
     * 因为队列是FIFO的，所以需要判断队列中有没有相关线程的节点已经在排队了。有则返回true表示线程需要排队，没有则返回false则表示线程无需排队。
     */
    public final boolean hasQueuedPredecessors() {
        Node h, s;
        if ((h = head) != null) {
            if ((s = h.next) == null || s.waitStatus > 0) {
                s = null; // traverse in case of concurrent cancellation
                for (Node p = tail; p != h && p != null; p = p.prev) {
                    if (p.waitStatus <= 0)
                        s = p;
                }
            }
            if (s != null && s.thread != Thread.currentThread())
                return true;
        }
        return false;
    }

    // Instrumentation and monitoring methods

    /**
     * 等待获取的线程的估计数量
     */
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    /**
     * 线程的集合
     */
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    /**
     * 返回一个集合，其中包含可能正在以独占模式等待获取的线程。
     */
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<>();
        for (Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * 返回一个集合，其中包含可能正在以共享模式等待获取的线程。
     */
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<>();
        for (AbstractQueuedSynchronizer.Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    public String toString() {
        return super.toString()
                + "[State = " + getState() + ", "
                + (hasQueuedThreads() ? "non" : "") + "empty queue]";
    }


    // Internal support methods for Conditions

    /**
     * 如果是一个节点，则返回true，且总是最初放置在该节点上的节点 一个条件队列，现在正等待在同步队列上重新获取。
     */
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        /*
         * 判断节点是否在同步队列中。
         */
        return findNodeFromTail(node);
    }

    /**
     * 如果节点在同步队列中，通过从tail向后搜索返回true。仅当isOnSyncQueue需要时调用。
     * @return true if present
     */
    private boolean findNodeFromTail(Node node) {
        for (Node p = tail;;) {
            if (p == node)
                return true;
            if (p == null)
                return false;
            p = p.prev;
        }
    }

    /**
     * 将节点从条件队列传输到同步队列。如果成功返回true。
     * @param node the node
     * @return true if successfully transferred (else the node was
     * cancelled before signal)
     */
    final boolean transferForSignal(Node node) {
        /*
         * 如果不能更改waitStatus，则该节点已被取消。 node节点如果是condition则进入后面的加入节点到队列中
         */
        if (!node.compareAndSetWaitStatus(Node.CONDITION, 0))
            return false;

        /*
         * 拼接到队列中，并尝试将前任的waitStatus设置为 指示线程(可能)正在等待。如果取消或 尝试设置waitStatus失败，唤醒重新同步
如果waitStatus是暂时的并且无害的错误)。
         */
        Node p = enq(node);
        int ws = p.waitStatus;
        //大于0表示已经取消 如果不能置为SIGNAL表示被其他线程执行过了
        if (ws > 0 || !p.compareAndSetWaitStatus(ws, Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }

    /**
     * 如果有必要，在取消等待后将节点传输到同步队列。如果线程在发出信号之前被取消，则返回true。
     * @param node the node
     * @return true if cancelled before the node was signalled
     */
    final boolean transferAfterCancelledWait(Node node) {
        //这里会有两个Node.CONDITION到0的过程，保证将condition节点加入队列中
        if (node.compareAndSetWaitStatus(Node.CONDITION, 0)) {
            enq(node);
            return true;
        }
        /*
         * 如果我们输给了一个signal()，那么在它完成enq()之前我们不能继续。在不完全转移期间取消是罕见的和短暂的，所以只要旋转。
         */
        while (!isOnSyncQueue(node))
            Thread.yield();
        return false;
    }

    /**
     * 调用带有当前状态值的release;返回保存的状态。  取消节点并在失败时抛出异常。  这里主要做唤醒后续节点，做一个容错的操作
     * @param node the condition node for this wait
     * @return previous sync state
     */
    final int fullyRelease(Node node) {
        try {
            int savedState = getState();
            if (release(savedState))
                return savedState;
            throw new IllegalMonitorStateException();
        } catch (Throwable t) {
            node.waitStatus = Node.CANCELLED;
            throw t;
        }
    }

    // Instrumentation methods for conditions

    /**
     *查询给定条件对象是否使用此同步器作为其锁。
     * @param condition the condition
     * @return {@code true} if owned
     * @throws NullPointerException if the condition is null
     */
    public final boolean owns(ConditionObject condition) {
        return condition.isOwnedBy(this);
    }

    /**
     * 查询是否有任何线程正在等待与此同步器关联的给定条件。
     * 注意，因为超时和中断可能在任何时候发生，{@code true}返回不保证将来的{@code信号}会唤醒任何线程。这种方法主要用于监控系统状态。
     * @param condition the condition
     * @return {@code true} if there are any waiting threads
     */
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    /**
     * 返回等待与此同步器关联的给定条件的线程数的估计。注意，由于超时和中断随时可能发生，
     * 因此估计仅作为实际等待者数量的上限。该方法主要用于监控系统状态，而不是用于同步控制。
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     */
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    /**
     * 返回一个集合，其中包含那些可能正在等待与此同步器关联的给定条件的线程。
     * 因为在构造这个结果时，实际的线程集可能会动态变化，所以返回的集合只是最佳估计。返回集合的元素没有特定的顺序。
     *
     * @param condition the condition
     * @return the collection of threads
     */
    public final Collection<Thread> getWaitingThreads(AbstractQueuedSynchronizer.ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    /**
     * Condition实现类
     */
    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        /** 条件队列的第一个节点。 */
        private transient Node firstWaiter;
        /** 条件队列的最后一个节点。 */
        private transient Node lastWaiter;
        public ConditionObject() { }

        // Internal methods

        /**
         * 添加一个新的等待节点 状态Condition
         */
        private Node addConditionWaiter() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }

            Node node = new Node(Node.CONDITION);

            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

        /**
         * 删除和传输节点，直到到达非取消的1或null。从signal中分离出来，部分原因是为了鼓励编译器在没有等待的情况下内联。
         * @param first (non-null) the first node on condition queue
         */
        private void doSignal(Node first) {
            do {
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                    (first = firstWaiter) != null);
        }

        /**
         * 唤醒所有condition节点
         */
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;

                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        /**
         * 从条件队列中取消已取消服务节点的链接。仅在持有锁时调用。当条件等待期间发生取消时，以及当lastWaiter被取消时插入新的waiter时，
         * 将调用此函数。在没有信号的情况下，需要使用这种方法来避免垃圾保留。因此，即使它可能需要完整的遍历，它也只在没有信号的情况下发生超时或取消时才会发挥作用。
         * 它遍历所有节点，而不是停在一个特定的目标，以解除所有指向垃圾节点的指针的链接，而不需要在取消风暴期间多次重新遍历。
         */
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }

        // public methods

        /**
         * 将等待时间最长的线程(如果存在)从该条件的等待队列移动到所属锁的等待队列。
         */
        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        /**
         * 将所有线程从此条件的等待队列移到所属锁的等待队列。
         */
        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }

        /**
         * 实现不可中断条件等待。
         */
        public final void awaitUninterruptibly() {
            //添加等待节点
            Node node = addConditionWaiter();
            //释放资源，并唤醒其它节点
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            //当前节点没有在同步队列中
            while (!isOnSyncQueue(node)) {
                //阻塞当前线程
                LockSupport.park(this);
                //当唤醒当前线程时，判断是否中断
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        /** Mode meaning to reinterrupt on exit from wait */
        private static final int REINTERRUPT =  1;
        /** Mode meaning to throw InterruptedException on exit from wait */
        private static final int THROW_IE    = -1;

        /**
         * 检查中断，如果在信号发出之前被中断返回THROW_IE，如果信号发出之后被中断返回REINTERRUPT，如果没有被中断则返回0。
         */
        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }

        /**
         * 重新中断当前线程，或者什么都不做。
         */
        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        /**
         * 实现可中断条件等待。
         */
        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            //添加一个等待节点
            Node node = addConditionWaiter();

            int savedState = fullyRelease(node);
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null) // clean up if cancelled
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        /**
         * Implements timed condition wait.
         * <ol>
         * <li>If current thread is interrupted, throw InterruptedException.
         * <li>Save lock state returned by {@link #getState}.
         * <li>Invoke {@link #release} with saved state as argument,
         *     throwing IllegalMonitorStateException if it fails.
         * <li>Block until signalled, interrupted, or timed out.
         * <li>Reacquire by invoking specialized version of
         *     {@link #acquire} with saved state as argument.
         * <li>If interrupted while blocked in step 4, throw InterruptedException.
         * </ol>
         */
        public final long awaitNanos(long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            // We don't check for nanosTimeout <= 0L here, to allow
            // awaitNanos(0) as a way to "yield the lock".
            final long deadline = System.nanoTime() + nanosTimeout;
            long initialNanos = nanosTimeout;
            AbstractQueuedSynchronizer.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout > SPIN_FOR_TIMEOUT_THRESHOLD)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            long remaining = deadline - System.nanoTime(); // avoid overflow
            return (remaining <= initialNanos) ? remaining : Long.MIN_VALUE;
        }

        /**
         * 定时等待
         */
        public final boolean awaitUntil(Date deadline)
                throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() >= abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        /**
         * 等待指定的时间，时间一到就中断
         */
        public final boolean await(long time, TimeUnit unit)
                throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            // We don't check for nanosTimeout <= 0L here, to allow
            // await(0, unit) as a way to "yield the lock".
            final long deadline = System.nanoTime() + nanosTimeout;
            AbstractQueuedSynchronizer.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout > SPIN_FOR_TIMEOUT_THRESHOLD)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation

        /**
         * ConditionObject是AbstractQueuedSynchronizer的成员内部类
         * 所以AbstractQueuedSynchronizer.this 和 当前ConditionObject所属的AbstractQueuedSynchronizer下的
         * 作用：这里主要判断ConditionObject是否是当前AbstractQueuedSynchronizer产生的
         *
         * @return {@code true} if owned
         */
        final boolean isOwnedBy(AbstractQueuedSynchronizer sync) {
            return sync == AbstractQueuedSynchronizer.this;
        }

        /**
         * 是否有等待节点
         */
        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        /**
         * 返回等待节点的长度
         */
        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (AbstractQueuedSynchronizer.Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == AbstractQueuedSynchronizer.Node.CONDITION)
                    ++n;
            }
            return n;
        }

        /**
         * 返回等待节点的线程
         */
        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<>();
            for (AbstractQueuedSynchronizer.Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == AbstractQueuedSynchronizer.Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }

    // VarHandle mechanics
    private static final VarHandle STATE;
    private static final VarHandle HEAD;
    private static final VarHandle TAIL;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            STATE = l.findVarHandle(AbstractQueuedSynchronizer.class, "state", int.class);
            HEAD = l.findVarHandle(AbstractQueuedSynchronizer.class, "head", Node.class);
            TAIL = l.findVarHandle(AbstractQueuedSynchronizer.class, "tail", Node.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }

        // Reduce the risk of rare disastrous classloading in first call to
        // LockSupport.park: https://bugs.openjdk.java.net/browse/JDK-8074773
        Class<?> ensureLoaded = LockSupport.class;
    }

    /**
     * 在第一次争用时初始化头部和尾部字段。
     */
    private final void initializeSyncQueue() {
        Node h;
        if (HEAD.compareAndSet(this, null, (h = new Node())))
            tail = h;
    }

    /**
     * CASes tail field.
     */
    private final boolean compareAndSetTail(AbstractQueuedSynchronizer.Node expect, AbstractQueuedSynchronizer.Node update) {
        return TAIL.compareAndSet(this, expect, update);
    }
}
