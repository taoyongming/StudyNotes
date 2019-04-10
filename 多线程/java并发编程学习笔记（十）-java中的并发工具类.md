​	在JDK的并发包里提供了几个非常有用的并发工具类。CountDownLatch、CyclicBarrier和
Semaphore工具类提供了一种并发流程控制的手段，Exchanger工具类则提供了在线程间交换数
据的一种手段。本章会配合一些应用场景来介绍如何使用这些工具类。

##### 等待多线程完成的CountDownLatch

CountDownLatch允许一个或多个线程等待其他线程完成操作。

​	假如有这样一个需求：我们需要解析一个Excel里多个sheet的数据，此时可以考虑使用多
线程，每个线程解析一个sheet里的数据，等到所有的sheet都解析完之后，程序需要提示解析完
成。在这个需求中，要实现主线程等待所有线程完成sheet的解析操作，最简单的做法是使用
join()方法，如代码

```java
public class JoinCountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        Thread parser1 = new Thread(new Runnable() {
            @Override
            public void run() {
            }
        });
        Thread parser2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("parser2 finish");
            }
        });
        parser1.start();
        parser2.start();
        parser1.join();
        parser2.join();
        System.out.println("all parser finish");
    }
}
```

join用于让当前执行线程等待join线程执行结束。其实现原理是不停检查join线程是否存
活，如果join线程存活则让当前线程永远等待。其中，wait（0）表示永远等待下去，代码片段如
下。

```java
while (isAlive()) {
wait(0);
}
```

直到join线程中止后，线程的this.notifyAll()方法会被调用，调用notifyAll()方法是在JVM里
实现的，所以在JDK里看不到，大家可以查看JVM源码。

​	在JDK 1.5之后的并发包中提供的CountDownLatch也可以实现join的功能，并且比join的功
能更多，如代码

```java
public class CountDownLatchTest {
    staticCountDownLatch c = new CountDownLatch(2);
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                c.countDown();
                System.out.println(2);
                c.countDown();
            }
        }).start();
        c.await();
        System.out.println("3");
    }
}
```

​	CountDownLatch的构造函数接收一个int类型的参数作为计数器，如果你想等待N个点完
成，这里就传入N。
​	当我们调用CountDownLatch的countDown方法时，N就会减1，CountDownLatch的await方法
会阻塞当前线程，直到N变成零。由于countDown方法可以用在任何地方，所以这里说的N个
点，可以是N个线程，也可以是1个线程里的N个执行步骤。用在多个线程时，只需要把这个
CountDownLatch的引用传递到线程里即可。

​	另外一个带指定时间的await方法——await（long time，TimeUnit unit），这个方法等待特定时
间后，就会不再阻塞当前线程。join也有类似的方法。

##### 同步屏障CyclicBarrier

​	CyclicBarrier的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一
组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会
开门，所有被屏障拦截的线程才会继续运行。

```java
public class CyclicBarrierTest {
staticCyclicBarrier c = new CyclicBarrier(2);
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                }
                System.out.println(1);
            }
        }).start();
        try {
            c.await();
        } catch (Exception e) {
        }
        System.out.println(2);
    }
}
```