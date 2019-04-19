# Linux 文件与目录管理

#### 目录与路径

. 代表此层目录
.. 代表上一层目录
-代表前一个工作目录
~ 代表“目前使用者身份”所在的主文件夹
~account 代表 account 这个使用者的主文件夹（account是个帐号名称）

下面我们就来谈一谈几个常见的处理目录的指令吧：

- **cd**：变换目录
- **pwd**：显示目前的目录
- **mkdir**：创建一个新的目录
- **rmdir**：删除一个空的目录
- **cd** （change directory, 变换目录）

##### **关于可执行文件路径的变量： $PATH**

范例：先用root的身份列出搜寻的路径为何？

```cmd
[root@study ~]# echo $PATH
/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin
```

如果想要让root在任何目录均可执行/root下面的ls，那么就将/root加入PATH当中即可。
加入的方法很简单，就像下面这样：

```cmd
[root@study ~]# PATH="${PATH}:/root"
```

#### 文件与目录管理

文件与目录的检视： ls

复制、删除与移动： cp, rm, mv

##### 文件内容查阅

- cat 由第一行开始显示文件内容
- tac 从最后一行开始显示，可以看出 tac 是 cat 的倒着写！
- nl 显示的时候，顺道输出行号！
- more 一页一页的显示文件内容
- less 与 more 类似，但是比 more 更好的是，他可以往前翻页！
- head 只看头几行
- tail 只看尾巴几行
- od 以二进制的方式读取文件内容！

##### 修改文件时间或创建新文件： touch

touch 这个指令最常被使用的情况是：

- 创建一个空的文件；
- 将某个文件日期修订为目前 （mtime 与 atime）

##### 文件与目录的默认权限与隐藏权限

文件默认权限：umask

```cmd
[root@study ~]# umask
0022 &lt;==与一般权限有关的是后面三个数字！
[root@study ~]# umask -S
u=rwx,g=rx,o=rx
```

要注意的是，umask 的分数指的是“该默认值需要减掉的权限！

##### 文件隐藏属性

chattr （设置文件隐藏属性）

最重要的当属 +i 与 +a 这个属性了。+i 可以让
一个文件无法被更动，对于需要强烈的系统安全的人来说， 真是相当的重要的

##### 文件特殊权限： SUID, SGID, SBIT

文件具有SUID的特殊权限时，代表当使用者执行此一binary程序时，在执行过程中使用
者会暂时具有程序拥有者的权限
目录具有SGID的特殊权限时，代表使用者在这个目录下面新建的文件之群组都会与该目
录的群组名称相同。
目录具有SBIT的特殊权限时，代表在该目录下使用者创建的文件只有自己与root能够删
除！

4 为 SUID
2 为 SGID
1 为 SBIT
假设要将一个文件权限改为“-rwsr-xr-x”时，由于 s 在使用者权限中，所以是 SUID ，因此，
在原先的 755 之前还要加上 4 ，也就是：“ chmod 4755 filename ”来设置

除了数字法之外，你也可以通过符号法来处理喔！其中 SUID 为 u+s ，而 SGID 为 g+s ，
SBIT 则是 o+t 。

##### 观察文件类型：file

file ~/.bashrc

#### 指令与文件的搜寻

##### which （寻找“可执行文件”）

例子：which ifconfig	

​	在 Linux 下面也有相当优异的搜寻指令呦！通常 find 不很常用的！因为速度慢之外， 也很操硬盘！一般我们都是先使用 whereis 或者是 locate 来检查，如果真的找不到了，才以 find 来搜寻呦！ 为什么呢？因为 whereis 只找系统中某些特定目录下面的文件而已，locate 则是利用数据库来搜寻文件名，当然两者就相当的快速， 并且没有实际的搜寻硬盘内的文件系统状态，

##### whereis （由一些特定的目录中寻找文件文件名）

whereis ifconfig

##### locate / updatedb

范例：找出系统中所有与 passwd 相关的文件名，且只列出 5 个
[root@study ~]# locate -l 5 passwd
/etc/passwd
/etc/passwd-
/etc/pam.d/passwd
/etc/security/opasswd
/usr/bin/gpasswd

##### find

范例：寻找 /etc 下面的文件，如果文件日期比 /etc/passwd 新就列出
[root@study ~]# find /etc -newer /etc/passwd