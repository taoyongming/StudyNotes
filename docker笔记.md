##### 第一章 docker简介

容器经常被认为是精进的技术，因为容器需要的开销有限，和传统的虚拟化和半虚拟化相比，容器运行不需要模拟层和管理层，而是使用操作系统的系统调用接口。这降低了运行单个容器所需要的开销，也使得宿主机中可运行更多的容器。
docker简介：Docker是一个能够把开发的应用程序自动部署到容器的开源引擎。
docker组件：
Docker客户端和服务器，也成为Docker引擎。
Docker镜像
Registry
Docker容器

##### ![docker架构](D:\ideaproject\StudyNotes\images\docker架构.png)


Docker镜像
镜像是构建Docker世界的基石。用户基于镜像构建自己的容器。镜像也是Docker生命周期的“构建”部分。镜像是基于联合文件系统一种层式的结构。由一系列指令一步步构建出来。也可以把镜像当做容器的源代码。镜像体积很小，易于分享，存储和更新。

Registry
Docker用Registry保存用户构建的镜像

Docker入门
运行第一个容器
docker run -i -t unubtu /bin/bash
首先我们告诉Docker执行docker run命令，并指定了-i -t 两个命令行参数。-i标志保证容器中的stdin是开启的，尽管我们没有附着到容器中。持久的标准输入是交互式shell的“半边天”，-t标志则是另外“半边天”，他告诉Docker为什么要创建容器分配一个伪tty终端。这样，新创建的容器才能够提供一个交互式shell。
unubtu是镜像的名字，当容器创建完毕后，Docker就会执行容器中的/bin/bash命令。这时就可以看到容器内的shell了。
使用第一个容器
hostname 检查容器主机名
cat /etc/hosts 检查容器的/etc/hosts文件
ip a  检查容器的接口
ps -aux 检查容器的进程
在第一个容器中安装软件包
apt-get update && apt-get install vim
当所有工作都结束时，输入exit，就回到宿主机的命令行提示符了。

docker ps -a查看当前系统的容器列表

docker run --name  给容器命名

重新启动已经停止运行的容器
docker start

附着到容器上
docker attach 
沿用docker run 时命令时指定的参数来运行。

创建守护式容器
docker run -d
获取守护式容器的日子
docker logs container_name

Docker日志驱动
docker run --log-driver
查看容器的进程
docker top 
查看一个或多个容器的统计信息
docker stats
在容器内部运行进程
docker exec
停止守护式容器
docker stop
自动启动容器
docker run --restart=always

docker inspect来获取更多容器信息
删除容器 
docker rm