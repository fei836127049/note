我们要想给ARM板编译出hello程序，需要使用交叉编译工具链，比如：

```shell
book@ubuntu$arm-buildroot-linux-gnueabihf-gcc -o hello hello.c
```

一个C/C++文件要经过预处理(preprocessing)、编译(compilation)、汇编(assembly)和链接(linking)等4步才能变成可执行文件。

## Uboot

Linux系统要启动就必须要一个BootLoader程序，也就是说芯片上电以后先运行一段BootLoader程序，这段BootLoader程序会先初始化时钟、看门狗、中断、SDRAM等外设，然后将Linux内核从flash（NAND，NOR FLASH,MMC等）拷贝到SDRAM中，最后启动Linux内核。**最主要的工作是启动Linux内核**

- 第一阶段
  - 初始化时钟，关闭看门狗，关中断，启动cache，关闭cache和tlb，关闭mmu初始化SDRAM，初始化nand flash，重定位。
- 第二阶段
  - 初始化一个串口，检测系统内存映射，将内核映像和根文件系统映像从flash上读到SDRAM空间中，为内核设置启动参数，调用内核。

（1）**CPU寄存器的设置：**

R0=0

R1=机器类型ID

R2=启动参数标记列表在RAM中起始基地址

（2）**CPU工作模式：**

必须禁止中断

CPU必须为SVC模式

（3）**Cache和MMU的设置：**

MMU必须关闭

指令cache可以打开也可以关闭

数据cache必须关闭

**关闭caches：**

caches是CPU内部的一个2级缓存，它的作用是将常用的数据和指令放在CPU内部。caches是通过CP15管理的，刚刚上电的时候，CPU还不能管理caches。上电的时候指令cache可关闭也可不关闭，但是数据cache一定关闭，否则可能导致刚刚开始的代码里面去取数据的时候，从cache里面去取，而这个时候RAM中数据还没有caches过来，导致数据预期异常。

**Uboot和内核之间的参数传递**

uboot启动后已经完成了基本的硬件初始化（内存串口等），接下来就是加载Linux内核到开发板的内存，然后跳转到Linux内核所在的地址运行。跳转方法：直接修改PC寄存器的值为Linux内核所在的地址，这样CPU就会从Linux内核所在的地址去取指令从而执行内核代码。

在给内核传递参数之前uboot已经完成了硬件的初始化，已经适应了开发板。但是内核并不是所有的开发板都能完美适配的，内核对于开发板的环境一无所知，所以要启动Linux内核，必须要给内核传递一些必要的信息来告诉内核当前所处的环境。

uboot把机器ID通过R1传递给内核，Linux内核运行的时候首先就从R1中读取机器ID来判断是否支持当前机器。这个机器ID实际上就是开发板CPU的ID。

**R2存放的是块内存的基地址**，这块内存中存放的是uboot给Linux内核的其他参数。这些参数有**内存的起始地址、内存大小、Linux内核启动后挂载文件系统的方式等信息**。不同的参数有不同的内容，为了让Linux内核能精确的解析出这些参数，双方在传递参数的时候要求参数在存放的时候需要**按照双方规定的格式存放**。

**除了约定好参数存放的地址外，还要规定参数的结构**，Linux2.4.x以后的内核都期待以**标记列表**（tagged_list）的形式来传递启动参数。标记，就是一种数据结构；标记列表就是挨着存放多个标记。标记列表以标记**ATAG_CORE**开始，以标记**ATAG_NONE**结束

### 文件系统

根文件系统：不仅具有普通文件系统的存储数据文件的功能，但是相对于普通的文件系统来说，他是内核启动时所挂载的第一个文件系统，没有根文件系统，其他的文件系统也没办法进行加载。根文件系统包含系统启动时所必须的目录和关键性的文件，以及使其他文件文件系统挂载所必要的文件。

- init进程的应用程序必须运行在根文件系统上。
- 根文件系统提供了根目录“/"。
- linux挂载分区时所依赖的信息存放在根文件系统/etc/fstab这个文件中。
- shell命令程序必须运行在根文件系统上。



### 中断

#### 硬中断

- 硬中断是由硬件产生，每个设备或者设备集都有他自己的IRQ（中断请求）。基于IRQ，CPU可以将相应的请求分发到对应的硬件驱动上。
- 处理中断的驱动是需要运行在CPU上的，因此，当中断产生的时候，CPU会中断当前正在运行的任务来处理中断。在多核心系统上，一个中断通常只能中断一颗CPU。
- 硬中断可以直接中断CPU。它会引起内核中相关的代码被触发。对于那些需要花费一些时间去处理的进程，中断代码本身也可以被其他的硬中断中断。
- 对于时钟中断，内核调度代码会将当前正在运行的进程挂起，从而让其他的进程来运行。它的存在是为了让调度代码（调度器）可以调度多任务。

#### 软中断

- 软中断的处理非常像硬中断。他们仅仅是由当前正在运行的进程所产生。
- 软中断是一些对I/O的请求。这些请求会调用内核中可以调度I/O发送的程序。对于某些设备，I/O请求需要被立即处理而磁盘I/O请求通常可以排队并且可以稍后处理。
- 软中断仅与内核相连系。
- 软中断并不会直接中断CPU，只有当前正在运行的代码或进程才可以产生软中断。

