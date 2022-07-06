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