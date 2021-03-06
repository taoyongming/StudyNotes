# 初识nginx

nginx编译安装

tar -zxf nginx-xxx

./configure --prefix=/usr/local/nginx

cd objs

make

语法

1. 每条指令以；分号结尾，指令与参数间以空格符号分隔
2. 配置文件由指令与指令块构成
3. 指令块以｛｝大括号将多条指令组织在一起
4. 使用#符号添加注释，提高可读性
5. include语句允许组合多个配置文件以提升可维护性
6. 使用$符号使用变量
7. 部分指令的参数支持正则表达式

命令

nginx  -s reload

静态项目

location / {

alias docs/;

}

反向代理

参考<http://nginx.org/en/docs/http/ngx_http_proxy_module.html>

upstream local {

​	server 127.0.0.1:8080

}

location / {	

​	proxy_pass http://local

}

配置缓存服务器

access-log

goaccess可视化工具

SSL/TLS

![](images/1.png)

TLS通讯过程

1. 验证身份
2. 达成安全套件共识
3. 传递密钥
4. 加密通讯

设置HTTPS

**用免费ssl证书实现一个https站点**

安装python2-certbot-nginx工具

certbot --nginx --nginx-server-root=/usr/local/openresty/nginx/conf/ -d geektime.taohui.pub

基于OpenResty用Lua语言实现简单服务

