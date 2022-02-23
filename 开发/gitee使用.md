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

