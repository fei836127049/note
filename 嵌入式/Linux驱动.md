我们要想给ARM板编译出hello程序，需要使用交叉编译工具链，比如：

```shell
book@ubuntu$arm-buildroot-linux-gnueabihf-gcc -o hello hello.c
```

一个C/C++文件要经过预处理(preprocessing)、编译(compilation)、汇编(assembly)和链接(linking)等4步才能变成可执行文件。