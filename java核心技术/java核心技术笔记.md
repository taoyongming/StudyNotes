**第18讲  如何避免死锁？**

1. 尽量避免使用多个锁，并且只有需要时才持有锁。
2. 尽量设计好锁的获取顺序。
3. 使用带超时的方法，为程序带来更多可控性。
   类似 Object.wait(…) 或者 CountDownLatch.await(…)。
4. 通过静态代码分析（如 FindBugs）。



**第20讲  并发包中的ConcurrentLinkedQueue和LinkedBlockingQueue有什么区别？**

java.util.concurrent 包提供的容器（Queue、List、Set）、Map，从命名上可以大概区分为 Concurrent、CopyOnWrite和 Blocking* 等三类，



**21讲  Java 并发类库提供的线程池有哪几种？ 分别有什么特点？**

Executors 目前提供了 5 种不同的线程池创建配置：

newCachedThreadPool()

newFixedThreadPool(int nThreads)，

newSingleThreadExecutor()，

newScheduledThreadPool(int corePoolSize)，

newWorkStealingPool(int parallelism)，



**第22讲 AtomicInteger底层实现原理是什么？如何在自己的产品代码中应用CAS操作？**

基于 CAS（compare-and-swap）技术

```java
public final boolean compareAndSet(int expectedValue, int newValue)
```

原子修改类成员变量 AtomicIntegerFieldUpdater, AtomicLongFieldUpdater和AtomicReferenceFieldUpdater

Variable Handle API

ReentrantLock 源码实现

AQS源码

```java
public final void acquire(int arg) {
	if (!tryAcquire(arg) &&
		acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
		selfInterrupt();
}
```

tryAcquire(int acquires)由ReentrantLock 自己实现。

当前线程会被包装成为一个排他模式的节点（EXCLUSIVE），通过 addWaiter 方法添加到队列
中。