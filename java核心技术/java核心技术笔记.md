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

**第23讲 请介绍类加载过程，什么是双亲委派模型？**

加载

链接

1. 验证（Verification）
2. 准备（Preparation）
3. 解析（Resolution）

初始化

**第24讲 | 有哪些方法可以在运行时动态生成一个Java类？**

Java Compiler API

利用 Java 字节码操纵工具和类库来实现，比如在专栏第 6 讲中提到的ASM、Javassist、cglib

类加载过程中，字节码转换成类

```java
protected final Class<?> defineClass(String name, byte[] b, int off, int len,
ProtectionDomain protectionDomain)
protected final Class<?> defineClass(String name, java.nio.ByteBuffer b,
ProtectionDomain protectionDomain)
```

Java 动态代理 

实现InvocationHandler,通过 Proxy 类，调用其 newProxyInstance 方法，生成一个实现了相应基础接口的代理类实例

**第25讲 | 谈谈JVM内存区域的划分，哪些区域可能发生OutOfMemoryError?**

1 程序计数器（PC，Program Counter Register）。线程独有

2 Java 虚拟机栈（Java Virtual Machine Stack）线程独有

内部保存一个个的栈帧（Stack Frame），对应着一次次的 Java 方法调用。栈帧中存储着局部变量表、操作数（operand）栈、动态链接、方法正常退出或者异常退出的定义等。

3 堆（Heap） 线程共享

4 方法区（Method Area）线程共享

存储所谓的元（Meta）数据，例如类结构信息，以及对应的运行时常量池、字段、方法代码等。运行时常量池（Run-Time Constant Pool），Java 的常量池可以存放各种常量信息，不管是编译期生成的各种字面量，还是需要在运行时决定的符号引用。

5 本地方法栈（Native Method Stack）。线程独有