##### 什么是线程

现代操作系统调度的最小单元是线程，也叫轻量级进程（LightWeight Process）。

##### 线程优先级

在Java线程中，通过一个整型成员变量priority来控制优先级，优先级的范围从1~10，在线
程构建的时候可以通过setPriority(int)方法来修改优先级，默认优先级是5，优先级高的线程分
配时间片的数量要多于优先级低的线程。设置线程优先级时，针对频繁阻塞（休眠或者I/O操
作）的线程需要设置较高优先级，而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较
低的优先级，确保处理器不会被独占。

注意，线程优先级不能作为程序正确性的依赖。

##### 线程的状态

![Snipaste_2019-03-25_15-54-27](images/Snipaste_2019-03-25_15-54-27.png)

下面我们使用jstack工具（可以选择打开终端，键入jstack或者到JDK安装目录的bin目录下
执行命令），尝试查看示例代码运行时的线程信息，更加深入地理解线程状态。

```java
public class ThreadState {
public static void main(String[] args) {
new Thread(new TimeWaiting (), "TimeWaitingThread").start();
new Thread(new Waiting(), "WaitingThread").start();
// 使用两个Blocked线程，一个获取锁成功，另一个被阻塞
new Thread(new Blocked(), "BlockedThread-1").start();
new Thread(new Blocked(), "BlockedThread-2").start();
}
// 该线程不断地进行睡眠
static class TimeWaiting implements Runnable {
@Override
public void run() {
while (true) {
    try {
        TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
    }
}
}
}
// 该线程在Waiting.class实例上等待
static class Waiting implements Runnable {
@Override
public void run() {
while (true) {
synchronized (Waiting.class) {
try {
Waiting.class.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
}
// 该线程在Blocked.class实例上加锁后，不会释放该锁
static class Blocked implements Runnable {
public void run() {
synchronized (Blocked.class) {
while (true) {
    try {
        TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
    }
}
}
}
}

}
```

运行该示例，打开终端或者命令提示符，键入“jps”，输出如下。

```cmd
611
935 Jps
929 ThreadState
270
```

可以看到运行示例对应的进程ID是929，接着再键入“jstack 929”（这里的进程ID需要和读
者自己键入jps得出的ID一致），部分输出如下所示。

可以看到运行示例对应的进程ID是929，接着再键入“jstack 929”（这里的进程ID需要和读
者自己键入jps得出的ID一致），部分输出如下所示。

```cmd
// BlockedThread-2线程阻塞在获取Blocked.class示例的锁上
"BlockedThread-2" prio=5 tid=0x00007feacb05d000 nid=0x5d03 waiting for monitor
entry [0x000000010fd58000]
java.lang.Thread.State: BLOCKED (on object monitor)
// BlockedThread-1线程获取到了Blocked.class的锁
"BlockedThread-1" prio=5 tid=0x00007feacb05a000 nid=0x5b03 waiting on condition
[0x000000010fc55000]
java.lang.Thread.State: TIMED_WAITING (sleeping)
// WaitingThread线程在Waiting实例上等待
"WaitingThread" prio=5 tid=0x00007feacb059800 nid=0x5903 in Object.wait()
[0x000000010fb52000]
java.lang.Thread.State: WAITING (on object monitor)
// TimeWaitingThread线程处于超时等待
"TimeWaitingThread" prio=5 tid=0x00007feacb058800 nid=0x5703 waiting on condition
[0x000000010fa4f000]
java.lang.Thread.State: TIMED_WAITING (sleeping)
```



![Snipaste_2019-03-25_16-10-47](images/Snipaste_2019-03-25_16-10-47.png)

##### Daemon线程

Daemon线程是一种支持型线程，因为它主要被用作程序中后台调度以及支持性工作。这
意味着，当一个Java虚拟机中不存在非Daemon线程的时候，Java虚拟机将会退出。可以通过调
用Thread.setDaemon(true)将线程设置为Daemon线程。
Daemon属性需要在启动线程之前设置，不能在启动线程之后设置。

Daemon线程被用作完成支持性工作，但是在Java虚拟机退出时Daemon线程中的finally块
并不一定会执行，

##### 构造线程

```java
    private void init(ThreadGroup g, Runnable target, String name,long stackSize,
                 AccessControlContext acc) {
      if (name == null) {
         throw new NullPointerException("name cannot be null");
      }
// 当前线程就是该线程的父线程
      Thread parent = currentThread();
      this.group = g;
// 将daemon、priority属性设置为父线程的对应属性
      this.daemon = parent.isDaemon();
      this.priority = parent.getPriority();
      this.name = name.toCharArray();
      this.target = target;
      setPriority(priority);
// 将父线程的InheritableThreadLocal复制过来
      if (parent.inheritableThreadLocals != null)
         this.inheritableThreadLocals=ThreadLocal.createInheritedMap(parent.
               inheritableThreadLocals);
// 分配一个线程ID
      tid = nextThreadID();
   }
```

在上述过程中，一个新构造的线程对象是由其parent线程来进行空间分配的，而child线程
继承了parent是否为Daemon、优先级和加载资源的contextClassLoader以及可继承的
ThreadLocal，同时还会分配一个唯一的ID来标识这个child线程。至此，一个能够运行的线程对
象就初始化好了，在堆内存中等待着运行。

启动线程

线程对象在初始化完成之后，调用start()方法就可以启动这个线程。线程start()方法的含义
是：当前线程（即parent线程）同步告知Java虚拟机，只要线程规划器空闲，应立即启动调用
start()方法的线程。
注意　启动一个线程前，最好为这个线程设置线程名称，因为这样在使用jstack分析程
序或者进行问题排查时，就会给开发人员提供一些提示，自定义的线程最好能够起个名字。

理解中断

中断可以理解为线程的一个标识位属性，它表示一个运行中的线程是否被其他线程进行
了中断操作。中断好比其他线程对该线程打了个招呼，其他线程通过调用该线程的interrupt()
方法对其进行中断操作。
线程通过检查自身是否被中断来进行响应，线程通过方法isInterrupted()来进行判断是否
被中断，也可以调用静态方法Thread.interrupted()对当前线程的中断标识位进行复位。如果该
线程已经处于终结状态，即使该线程被中断过，在调用该线程对象的isInterrupted()时依旧会返
回false。
从Java的API中可以看到，许多声明抛出InterruptedException的方法（例如Thread.sleep(long
millis)方法）这些方法在抛出InterruptedException之前，Java虚拟机会先将该线程的中断标识位
清除，然后抛出InterruptedException，此时调用isInterrupted()方法将会返回false。
在代码清单4-7所示的例子中，首先创建了两个线程，SleepThread和BusyThread，前者不停
地睡眠，后者一直运行，然后对这两个线程分别进行中断操作，观察二者的中断标识位。

```java
public class Interrupted {
      public static void main(String[] args) throws Exception {
// sleepThread不停的尝试睡眠
         Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
         sleepThread.setDaemon(true);
// busyThread不停的运行
         Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
         busyThread.setDaemon(true);
         sleepThread.start();
         busyThread.start();
// 休眠5秒，让sleepThread和busyThread充分运行
         TimeUnit.SECONDS.sleep(5);
         sleepThread.interrupt();
         busyThread.interrupt();
         System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
         System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
// 防止sleepThread和busyThread立刻退出
         SleepUtils.second(2);
      }
      static class SleepRunner implements Runnable {
         @Override
         public void run() {
            while (true) {
               SleepUtils.second(10);
            }
         }
      }
      static class BusyRunner implements Runnable {
         @Override
         public void run() {
            while (true) {
            }
         }
      }
   }
```

输出如下。
SleepThread interrupted is false
BusyThread interrupted is true
从结果可以看出，抛出InterruptedException的线程SleepThread，其中断标识位被清除了，
而一直忙碌运作的线程BusyThread，中断标识位没有被清除。

过期的suspend()、resume()和stop()