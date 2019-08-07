并发编程领域可以抽象成**三个核心问题：分工、同步和互斥**。

### 1. 分工

 Executor、Fork/Join、Future 

生产者 - 消费者、Thread-Per-Message、Worker Thread 模式等都是用来指导你如何分工的

### 2. 同步

Future、 CountDownLatch、CyclicBarrier、Phaser、Exchanger

### 3. 互斥

synchronized、SDK 里的各种 Lock

Thread Local 和 final 关键字，还有一种 Copy-on-write 的模式



![](../images/1.png)