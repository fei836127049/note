使用git commit -m "提交jenkins测试项目代码" 将代码进行提交：

![img](https://img2018.cnblogs.com/blog/1236645/201907/1236645-20190715213324467-1266019528.png)

使用git push命令把本地代码推送到gitee上的远程仓库上



如果出现

```sql
Untracked files:         
"gitee\344\275\277\347\224\250.md"
nothing added to commit but untracked f
```

解决方法就是使用

```git
git add  .
```





gitee显示乱码

```git
git commit -m 中文乱码问题解决方案

1.设置git 的界面编码：

git config --global gui.encoding utf-8

2.设置 commit log 提交时使用 utf-8 编码：

git config --global i18n.commitencoding utf-8

3.使得在 $ git log 时将 utf-8 编码转换成 gbk 编码：

git config --global i18n.logoutputencoding gbk

4.使得 git log 可以正常显示中文：

export LESSCHARSET=utf-8

经过以上步骤，我们会发现可以正常显示中文注释了。
```

### 一、部署环境与准备

##### 1.环境

本地Windows10操作系统

腾讯云Cenots7.6服务器

##### 2.准备

Hexo 本地博客

Xshell 链接到你的服务器

### 二、云服务器配置Git

1.安装依赖库

```
yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel 
```

2.安装编译工具

```
yum install gcc perl-ExtUtils-MakeMaker package
```

3.查看git的版本y

```
git version
```

4.删除git

```
yum remove git -y
```

5.下载解压最新版

```
cd /usr/local/src    #下载的目录
wget https://www.kernel.org/pub/software/scm/git/git-2.28.0.tar.gz    #下载最新版
tar -zxvf git-2.28.0.tar.gz        #解压到当前文件夹
```

6.编辑并安装

```
cd git-2.28.0    #进入文件夹
make prefix=/usr/local/git all    #编译源码
make prefix=/usr/local/git install    #安装路径
```

7.配置git的环境变量

```
echo 'export PATH=$PATH:/usr/local/git/bin' >> /etc/bashrc
```

8.刷新环境变量

```
source /etc/bashrc
```

9.查看版本号

```
git --version
```

10创建git用户并且修改权限

```
adduser fuchen
passwd fuchen
chmod 740 /etc/sudoers
vim /etc/sudoers

root    ALL=(ALL)       ALL
fuchen     ALL=(ALL)       ALL
```

11.本地windows10使用Gitbash创建密钥

```
ssh-keygen -t rsa
```

12.将本地创建id_rsa.pub中文件复制

```
su fuchen
mkdir ~/.ssh
vim ~/.ssh/authorized_keys
```

13.本地测试

```
ssh -v fuchen@服务器ip
```

### 三、云服务器网站配置

1.创建网站目录并且设置权限

```
su root
mkdir /home/hexo
chown fuchen:fuchen -R /home/hexo
```

2.安装Nginx

```
yum install -y nginx
systemctl start nginx.service    #启动服务
```

3.修改Nginx配置文件

```
vim /etc/nginx/nginx.conf 

 38     server {
 39         listen       80 default_server;
 40         listen       [::]:80 default_server;
 41         server_name  fuchenchenle.cn;        #域名
 42         root         /home/hexo;        #网站目录
```

4.重启服务器

```
systemctl restart nginx.service
```

5.建立git仓库

```
su root
cd /home/fuchen
git init --bare blog.git
chown fuchen:fuchen -R blog.git
```

6.同步网站根目录

```
vim blog.git/hooks/post-receive

#!/bin/sh
git --work-tree=/home/hexo --git-dir=/home/fuchen/blog.git checkout -f
```

7.修改权限

```
chmod +x /home/fuchen/blog.git/hooks/post-receive
```

8.在windows10本地hexo目录修改_config.yml文件

```
deploy:
  type: git
  repository: fuchen@49.232.59.235:/home/fuchen/blog.git    #用户名@服务器Ip:git仓库位置
  branch: master
```

9.在本机gitbash部署

```
hexo clean
hexo g -d
```

### 四、常见报错

##### 1. git-upload-pack: 未找到命令

```
bash: git-upload-pack: command not found
fatal: Could not read from remote repository.
```

解决方法

```
sudo ln -s  /usr/local/git/bin/git-upload-pack  /usr/bin/git-upload-pack
```

##### 2.git-receive-pack: 未找到命令

```
bash: git-receive-pack: command not found
fatal: Could not read from remote repository.
```

解决方法

```
sudo ln -s /usr/local/git/bin/git-receive-pack  /usr/bin/git-receive-pack
```

##### 3.无法远程连接获取

```
fatal: Could not read from remote repository.
```

解决方法

```
重试或者 删掉本地ssh公钥重新上传至服务器
```

##### 4.key出错

```
Host key verification failed.
```

解决方法

```
ssh-keygen -R 你要访问的IP地址
```