#### 命令行工具

**JDK 工具之 jstat 命令**

- -class：显示 ClassLoad      的相关信息；

- -compiler：显示 JIT      编译的相关信息；

- -gc：显示和 gc 相关的堆信息；

- -gccapacity：显示各个代的容量以及使用情况；

- -gcmetacapacity：显示      Metaspace 的大小；

- -gcnew：显示新生代信息；

- -gcnewcapacity：显示新生代大小和使用情况；

- -gcold：显示老年代和永久代的信息；

- -gcoldcapacity      ：显示老年代的大小；

- -gcutil：显示垃圾收集信息；

- -gccause：显示垃圾回收的相关信息（通      -gcutil），同时显示最后一次或当前正在发生的垃圾回收的诱因；

- -printcompilation：输出      JIT 编译的方法信息。

  它的功能比较多，在这里我例举一个常用功能，如何使用 jstat 查看堆内存的使用情况。我们可以用 jstat -gc pid 查看：

  - S0C：年轻代中 To Survivor      的容量（单位 KB）；
  - S1C：年轻代中 From Survivor      的容量（单位 KB）；
  - S0U：年轻代中 To Survivor      目前已使用空间（单位 KB）；
  - S1U：年轻代中 From Survivor      目前已使用空间（单位 KB）；
  - EC：年轻代中 Eden 的容量（单位      KB）；
  - EU：年轻代中 Eden 目前已使用空间（单位      KB）；
  - OC：Old 代的容量（单位 KB）；
  - OU：Old 代目前已使用空间（单位 KB）；
  - MC：Metaspace 的容量（单位      KB）；
  - MU：Metaspace 目前已使用空间（单位      KB）；
  - YGC：从应用程序启动到采样时年轻代中 gc      次数；
  - YGCT：从应用程序启动到采样时年轻代中 gc      所用时间 (s)；
  - FGC：从应用程序启动到采样时 old 代（全      gc）gc 次数；
  - FGCT：从应用程序启动到采样时 old      代（全 gc）gc 所用时间 (s)；
  - GCT：从应用程序启动到采样时 gc      用的总时间 (s)。

**JDK 工具之 jstack 命令**

1. jps命令或者top命令查看那个pid占用cpu高，再通过top -Hp pid 查看线程Id
2. `jstack pid`命令查看当前java进程的堆栈状态.
3. 将该pid转成16进制的值，在thread dump中每个线程都有一个nid，找到对应的nid即可；隔段时间再执行一次stack命令获取thread dump，区分两份dump是否有差别。

**JDK 工具之 jmap ：**

- 命令：jmap -heap pid
  描述：显示Java堆详细信息
- 命令：jmap -histo:live pid
  描述：显示堆中对象的统计信息
- 命令：jmap -clstats pid
  描述：打印类加载器信息

- 命令：jmap -dump:format=b,file=heapdump.phrof pid
  描述：生成堆转储快照dump文件。可以使用MemoryAnalyzer工具分析dump文件。

#### 图形化工具

##### jvisualvm，JConsole 

1 服务器tomcat开启jms,修改配置文件 catalina.sh内容如下:

```sh
export JAVA_OPTS="-Xms256m -Xmx512m -Xss256m -XX:PermSize=512m -XX:MaxPermSize=1024m  -Djava.rmi.server.hostname=136.64.45.24 -Dcom.sun.management.jmxremote.port=9315 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
```

基本包括以下基本功能：`概述`、`内存`、`线程`、`类`、`VM概要`、`MBean`

##### GCViewer工具

1. 使用jps命令查看当前有哪些java进程在运行，找到我们要查看的java程序的进程pid

2. 使用命令jinfo pid 来查看这个进程对应的java 信息，可以看到大概在最下面的地方有个参数-Xloggc:，他对应的就是gc log的位置。

3. 用gcviewer 打开gc log可以很直观的查看gc log

##### MemoryAnalyzer工具

用来分析dump文件，主要用于分析内存泄漏等问题。