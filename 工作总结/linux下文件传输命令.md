Linux下几种文件传输命令 sz rz sftp scp  

最近在部署系统时接触了一些文件传输命令，分别做一下简单记录：sftp

1. Secure Ftp 是一个基于SSH安全协议的文件传输管理工具。由于它是基于SSH的，会在传输过程中对用户的密码、数据等敏感信息进行加密，因此可以有效的防止用户信息在传输的过程中被窃取，比FTP有更高的安全性。在功能方面与FTP很类似，不仅可以传输文件数据，而且可以进行远程的文件管理（如建立，删除，查看文件列表等操作）。Sftp与ftp虽然只有一字之差，但基于的传输协议却是不同的。因此不能用sftp client去连接ftp server 也不能用 ftp client 去连接 sftp
   server。

   建立连接：sftp user@host

   从本地上传文件：put localpath

   下载文件：get remotepath

   与远程相对应的本地操作，只需要在命令前加上”l” 即可，方便好记。

2. 例如：lcd lpwd lmkdir

3. scp

4. SCP ：secure copy (remote file copy program) 也是一个基于SSH安全协议的文件传输命令。与sftp不同的是，它只提供主机间的文件传输功能，没有文件管理的功能。

5. 复制local_file 到远程目录remote_folder下

6. scp local_file remote_user@host:remote_folder

7. 复制local_folder 到远程remote_folder（需要加参数 -r 递归）

8. scp –r local_folder remote_user@host:remote_folder

9. 以上命令反过来写就是远程复制到本地

10. sz/rz

11. sz/rz 是基于ZModem传输协议的命令。对传输的数据会进行核查，并且有很好的传输性能。使用起来更是非常方便，但前提是window端需要有能够支持ZModem的telnet或者SSH客户端，例如secureCRT。

12. 首先需要在secureCRT中可以配置相关的本地下载和上传目录，然后用rz、sz命令即可方便的传输文件数据。

13. 下载数据到本地下载目录：sz filename1 filename2 …

14. 上传数据到远程：执行rz –be 命令，客户端会弹出上传窗口，用户自行选择(可多选)要上传的文件即可。