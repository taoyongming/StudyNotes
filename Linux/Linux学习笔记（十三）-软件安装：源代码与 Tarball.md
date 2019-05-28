##### 什么是开放源码、编译器与可可执行文件

​	源代码其实大多是纯文本文件，需要通过编译器的编译动作后，才能够制作出 Linux 系统能够认识的可执行的 binary file ；开放源代码可以加速软件的更新速度，让软件性能更快、漏洞修补更实时；

​	在 Linux 系统当中，最标准的 C 语言编译器为 gcc ；

开始编译与测试执行

```cmd
[root@study ~]# gcc hello.c
[root@study ~]# ll hello.c a.out
-rwxr-xr-x. 1 root root 8503 Sep 4 11:33 a.out <==此时会产生这个文件名
-rw-r--r--. 1 root root 71 Sep 4 11:32 hello.c
[root@study ~]# ./a.out
Hello World <==呵呵！成果出现了！
```

##### 用 make 进行宏编译

```cmd
# 1\. 先编辑 makefile 这个规则档，内容只要作出 main 这个可执行文件
[root@study ~]# vim makefile
main: main.o haha.o sin_value.o cos_value.o
gcc -o main main.o haha.o sin_value.o cos_value.o -lm
# 注意：第二行的 gcc 之前是 &lt;tab&gt; 按键产生的空格喔！
# 2\. 尝试使用 makefile 制订的规则进行编译的行为：
[root@study ~]# rm -f main *.o &lt;==先将之前的目标文件去除
[root@study ~]# make
cc -c -o main.o main.c
cc -c -o haha.o haha.c
cc -c -o sin_value.o sin_value.c
cc -c -o cos_value.o cos_value.c
gcc -o main main.o haha.o sin_value.o cos_value.o -lm
# 此时 make 会去读取 makefile 的内容，并根据内容直接去给他编译相关的文件啰！
# 3\. 在不删除任何文件的情况下，重新执行一次编译的动作：
[root@study ~]# make
make: `main' is up to date.
# 看到了吧！是否很方便呢！只会进行更新 （update） 的动作而已。
```

##### Tarball 的管理与建议

​	Tarball 为使用 tar 与 gzip/bzip2/xz 压缩功能所打包与压缩的，具有源代码的文件；
一般而言，要使用 Tarball 管理 Linux 系统上的软件，最好需要 gcc, make, autoconfig,
kernel source, kernel header 等前驱软件才行，所以在安装 Linux 之初，最好就能够选择
Software development 以及 kernel development 之类的群组；

Tarball 安装的基本步骤：

1. 取得原始文件：将 tarball 文件在 /usr/local/src 目录下解压缩；
2. 取得步骤流程：进入新创建的目录下面，去查阅 INSTALL 与 README 等相关文件内容
  （很重要的步骤！）；
3. 相依属性软件安装：根据 INSTALL/README 的内容察看并安装好一些相依的软件 （非
  鸟哥的 Linux 私房菜：基础学习篇 第四版
  21.4 Tarball 的管理与建议 1028
  必要）；
4. 创建 makefile：以自动侦测程序 （configure 或 config） 侦测作业环境，并创建 Makefile
  这个文件；
5. 编译：以 make 这个程序并使用该目录下的 Makefile 做为他的参数配置文件，来进行
  make （编译或其他） 的动作；
6. 安装：以 make 这个程序，并以 Makefile 这个参数配置文件，依据 install 这个标的
  （target） 的指定来安装到正确的路径！