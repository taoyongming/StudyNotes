- 早期的服务管理使用 systemV 的机制，通过 /etc/init.d/*, service, chkconfig, setup 等指
  令来管理服务的启动/关闭/默认启动；
- 从 CentOS 7.x 开始，采用 systemd 的机制，此机制最大功能为平行处理，并采单一指
  令管理 （systemctl），开机速度加快！

```cmd
范例一：看看目前 atd 这个服务的状态为何？
[root@study ~]# systemctl status atd.service
atd.service - Job spooling tools
Loaded: loaded （/usr/lib/systemd/system/atd.service; enabled）
Active: active （running） since Mon 2015-08-10 19:17:09 CST; 5h 42min ago
Main PID: 1350 （atd）
CGroup: /system.slice/atd.service
└─1350 /usr/sbin/atd -f
Aug 10 19:17:09 study.centos.vbird systemd[1]: Started Job spooling tools.
# 重点在第二、三行喔～
# Loaded：这行在说明，开机的时候这个 unit 会不会启动，enabled 为开机启动，disabled 开机不会启动
# Active：现在这个 unit 的状态是正在执行 （running） 或没有执行 （dead）
# 后面几行则是说明这个 unit 程序的 PID 状态以及最后一行显示这个服务的登录文件信息！
# 登录文件信息格式为：“时间” “讯息发送主机” “哪一个服务的讯息” “实际讯息内容”
# 所以上面的显示讯息是：这个 atd 默认开机就启动，而且现在正在运行的意思！
范例二：正常关闭这个 atd 服务
[root@study ~]# systemctl stop atd.service
[root@study ~]# systemctl status atd.service
atd.service - Job spooling tools
Loaded: loaded （/usr/lib/systemd/system/atd.service; enabled）
Active: inactive （dead） since Tue 2015-08-11 01:04:55 CST; 4s ago
Process: 1350 ExecStart=/usr/sbin/atd -f $OPTS （code=exited, status=0/SUCCESS）
Main PID: 1350 （code=exited, status=0/SUCCESS）
Aug 10 19:17:09 study.centos.vbird systemd[1]: Started Job spooling tools.
Aug 11 01:04:55 study.centos.vbird systemd[1]: Stopping Job spooling tools...
Aug 11 01:04:55 study.centos.vbird systemd[1]: Stopped Job spooling tools.
# 目前这个 unit 下次开机还是会启动，但是现在是没在运行的
```



- systemd 将各服务定义为 unit，而 unit 又分类为 service, socket, target, path, timer 等不
  同的类别，方便管理与维护
- 启动/关闭/重新启动的方式为： systemctl [start|stop|restart] unit.service
- 设置默认启动/默认不启动的方式为： systemctl [enable|disable] unit.service
- 查询系统所有启动的服务用 systemctl list-units --type=service 而查询所有的服务 （含不
  启动） 使用 systemctl list-unit-files --type=service
- systemd 取消了以前的 runlevel 概念 （虽然还是有相容的 target），转而使用不同的
  target 操作环境。常见操作环境为 multi-user.targer 与 graphical.target。 不重新开机而
  转不同的操作环境使用 systemctl isolate unit.target，而设置默认环境则使用 systemctl
  set-default unit.target
- systemctl 系统默认的配置文件主要放在 /usr/lib/systemd/system，管理员若要修改或自
  行设计时，则建议放在 /etc/systemd/system/ 目录下。
- 管理员应使用 man systemd.unit, man systemd.service, man systemd.timer 查询
  /etc/systemd/system/ 下面配置文件的语法， 并使用 systemctl daemon-reload 载入后，
  才能自行撰写服务与管理服务喔！
- 除了 atd 与 crond 之外，可以 通过 systemd.timer 亦即 timers.target 的功能，来使用
  systemd 的时间管理功能。