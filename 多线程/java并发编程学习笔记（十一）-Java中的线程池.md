​	Java中的线程池是运用场景最多的并发框架，几乎所有需要异步或并发执行任务的程序
都可以使用线程池。在开发过程中，合理地使用线程池能够带来3个好处。
第一：降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
第二：提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。
第三：提高线程的可管理性。线程是稀缺资源，如果无限制地创建，不仅会消耗系统资源，
还会降低系统的稳定性，使用线程池可以进行统一分配、调优和监控。但是，要做到合理利用
线程池，必须对其实现原理了如指掌。

##### 线程池的实现原理

​	当向线程池提交一个任务之后，线程池是如何处理这个任务的呢？本节来看一下线程池
的主要处理流程，处理流程图如图9-1所示。
从图中可以看出，当提交一个新任务到线程池时，线程池的处理流程如下。
1）线程池判断核心线程池里的线程是否都在执行任务。如果不是，则创建一个新的工作
线程来执行任务。如果核心线程池里的线程都在执行任务，则进入下个流程。
2）线程池判断工作队列是否已经满。如果工作队列没有满，则将新提交的任务存储在这
个工作队列里。如果工作队列满了，则进入下个流程。
3）线程池判断线程池的线程是否都处于工作状态。如果没有，则创建一个新的工作线程
来执行任务。如果已经满了，则交给饱和策略来处理这个任务。
ThreadPoolExecutor执行execute()方法的示意图，

![Snipaste_2019-04-10_10-30-21](images/Snipaste_2019-04-10_10-30-21.png)

![Snipaste_2019-04-10_10-31-07](images/Snipaste_2019-04-10_10-31-07.png)

ThreadPoolExecutor执行execute方法分下面4种情况。
1）如果当前运行的线程少于corePoolSize，则创建新线程来执行任务（注意，执行这一步骤
需要获取全局锁）。
2）如果运行的线程等于或多于corePoolSize，则将任务加入BlockingQueue。
3）如果无法将任务加入BlockingQueue（队列已满），则创建新的线程来处理任务（注意，执
行这一步骤需要获取全局锁）。
4）如果创建新线程将使当前运行的线程超出maximumPoolSize，任务将被拒绝，并调用
RejectedExecutionHandler.rejectedExecution()方法。
ThreadPoolExecutor采取上述步骤的总体设计思路，是为了在执行execute()方法时，尽可能
地避免获取全局锁（那将会是一个严重的可伸缩瓶颈）。在ThreadPoolExecutor完成预热之后
（当前运行的线程数大于等于corePoolSize），几乎所有的execute()方法调用都是执行步骤2，而
步骤2不需要获取全局锁。

​	源码分析：上面的流程分析让我们很直观地了解了线程池的工作原理，让我们再通过源代
码来看看是如何实现的，线程池执行任务的方法如下。

```java
   public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
// 如果线程数小于基本线程数，则创建线程并执行当前任务
        if (poolSize >= corePoolSize || !addIfUnderCorePoolSize(command)) {
// 如线程数大于等于基本线程数或线程创建失败，则将当前任务放到工作队列中。
            if (runState == RUNNING && workQueue.offer(command)) {
                if (runState != RUNNING || poolSize == 0)
                    ensureQueuedTaskHandled(command);
            } // 如果线程池不处于运行中或任务无法放入队列，并且当前线程数量小于最大允许的线程数量，
// 则创建一个线程执行任务。
            else if (!addIfUnderMaximumPoolSize(command))
// 抛出RejectedExecutionException异常
                reject(command); // is shutdown or saturated
        }
    }
```

​	工作线程：线程池创建线程时，会将线程封装成工作线程Worker，Worker在执行完任务
后，还会循环获取工作队列里的任务来执行。我们可以从Worker类的run()方法里看到这点。

```java
public void run() {
    try {
        Runnable task = firstTask;
        firstTask = null;
        while (task != null || (task = getTask()) != null) {
            runTask(task);
            task = null;
        }
    } finally {
        workerDone(this);
    }
}
```

##### 线程池的使用

##### 线程池的创建

​	我们可以通过ThreadPoolExecutor来创建一个线程池。
new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
milliseconds,runnableTaskQueue, handler);
创建一个线程池时需要输入几个参数，如下。
1）**corePoolSize（线程池的基本大小）**：当提交一个任务到线程池时，线程池会创建一个线
程来执行任务，即使其他空闲的基本线程能够执行新任务也会创建线程，等到需要执行的任
务数大于线程池基本大小时就不再创建。如果调用了线程池的prestartAllCoreThreads()方法，
线程池会提前创建并启动所有基本线程。
2）**runnableTaskQueue（任务队列）**：用于保存等待执行的任务的阻塞队列。可以选择以下几
个阻塞队列。
·ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按FIFO（先进先出）原
则对元素进行排序。
·LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按FIFO排序元素，吞吐量通
常要高于ArrayBlockingQueue。静态工厂方法Executors.newFixedThreadPool()使用了这个队列。
·SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用
移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于Linked-BlockingQueue，静态工
厂方法Executors.newCachedThreadPool使用了这个队列。
·PriorityBlockingQueue：一个具有优先级的无限阻塞队列。
3）**maximumPoolSize（线程池最大数量）**：线程池允许创建的最大线程数。如果队列满了，并
且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是，如
果使用了无界的任务队列这个参数就没什么效果。
4）**ThreadFactory**：用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设
置更有意义的名字。使用开源框架guava提供的ThreadFactoryBuilder可以快速给线程池里的线
程设置有意义的名字，代码如下。
new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build();
5）**RejectedExecutionHandler（饱和策略）**：当队列和线程池都满了，说明线程池处于饱和状
态，那么必须采取一种策略处理提交的新任务。这个策略默认情况下是AbortPolicy，表示无法
处理新任务时抛出异常。在JDK 1.5中Java线程池框架提供了以下4种策略。
·AbortPolicy：直接抛出异常。
·CallerRunsPolicy：只用调用者所在线程来运行任务。
·DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务。
·DiscardPolicy：不处理，丢弃掉。
当然，也可以根据应用场景需要来实现RejectedExecutionHandler接口自定义策略。如记录
日志或持久化存储不能处理的任务。
·keepAliveTime（线程活动保持时间）：线程池的工作线程空闲后，保持存活的时间。所以，
如果任务很多，并且每个任务执行的时间比较短，可以调大时间，提高线程的利用率。
·TimeUnit（线程活动保持时间的单位）：可选的单位有天（DAYS）、小时（HOURS）、分钟
（MINUTES）、毫秒（MILLISECONDS）、微秒（MICROSECONDS，千分之一毫秒）和纳秒
（NANOSECONDS，千分之一微秒）。

##### 向线程池提交任务

​	可以使用两个方法向线程池提交任务，分别为execute()和submit()方法。
execute()方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功。
通过以下代码可知execute()方法输入的任务是一个Runnable类的实例。

```java
threadsPool.execute(new Runnable() {
        @Override
        public void run() {
// TODO Auto-generated method stub
        }
    });
```

​	submit()方法用于提交需要返回值的任务。线程池会返回一个future类型的对象，通过这个
future对象可以判断任务是否执行成功，并且可以通过future的get()方法来获取返回值，get()方
法会阻塞当前线程直到任务完成，而使用get（long timeout，TimeUnit unit）方法则会阻塞当前线
程一段时间后立即返回，这时候有可能任务没有执行完。

```java
    Future<Object> future = executor.submit(harReturnValuetask);
try {
        Object s = future.get();
        } catch (InterruptedException e) {
// 处理中断异常
        } catch (ExecutionException e) {
// 处理无法执行任务异常
        } finally {
// 关闭线程池
        executor.shutdown();
        }
```

##### 关闭线程池

​	可以通过调用线程池的shutdown或shutdownNow方法来关闭线程池。它们的原理是遍历线
程池中的工作线程，然后逐个调用线程的interrupt方法来中断线程，所以无法响应中断的任务
可能永远无法终止。但是它们存在一定的区别，shutdownNow首先将线程池的状态设置成
STOP，然后尝试停止所有的正在执行或暂停任务的线程，并返回等待执行任务的列表，而
shutdown只是将线程池的状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线
程。

##### 合理地配置线程池

性质不同的任务可以用不同规模的线程池分开处理。

CPU密集型任务应配置尽可能小的线程，如配置Ncpu+1个线程的线程池。

IO密集型任务线程并不是一直在执行任务，则应配置尽可能多的线程，如2*Ncpu。