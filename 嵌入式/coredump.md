近期的学习过程中碰到了代码调试问题，由于缺乏调试手段，故学习了coredump

1、首先确定coredump是否开启（默认不开启，需要手动开启）

```shell
ulimit -c
```

如果结果为0，则认为没有开启coredump的生成，需要开启。

2、开启coredump

- ​	临时启用

  - ```shell
    #限制生成的coredump文件不超过1024k
    ulimit -c 1024
    #不加限制
    ulimit -c unlimited
    ```


- 修改profile文件

  - 可以使用vim /etc/profile来编辑,在文件中添加如下命令

  - ```shell
    #设置启用coredump
    ulimit -c unlimited
    #将产生的coredump文件存储到自定义文件夹，这里存在/tmp/corefile/，coredump文件名为core-工程名-所dump的进程PID-转储时刻
    /*
    %c 转储文件的大小上限

    %e 所dump的文件名

    %g 所dump的进程的实际组ID

    %h 主机名

    %p 所dump的进程PID

    %s 导致本次coredump的信号

    %t 转储时刻(由1970年1月1日起计的秒数)

    %u 所dump进程的实际用户ID
    */
    echo "/tmp/corefile/core-%e-%p-%t" > /proc/sys/kernel/core_pattern
    ```

3、查看coredump

使用gdb 工程目录 coredump文件目录 ，来查看coredump文件

```shell
gdb /home/code/leetcode/LeetCode100/code_out/exist core-exist-227127-1657095332
```

![coredump查看](E:\gitee笔记\note\嵌入式\pic\coredump查看.png)

为了定位问题，常常需要进行单步跟踪，设置断点之类的操作。

下边列出了GDB一些常用的操作。

- 启动程序：run
- 设置断点：b 行号|函数名
- 删除断点：delete 断点编号
- 禁用断点：disable 断点编号
- 启用断点：enable 断点编号
- 单步跟踪：next (简写 n)
- 单步跟踪：step (简写 s)
- 打印变量：print 变量名字 （简写p）
- 设置变量：set var=value
- 查看变量类型：ptype var
- 顺序执行到结束：cont
- 顺序执行到某一行： util lineno
- 打印堆栈信息：bt

