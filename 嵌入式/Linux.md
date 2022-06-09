Linux程序分为**可执行文件**和**脚本程序**

## 1、shell编程

### 1.1、变量

- shell变量区分大小写
- 可以使用 echo $变量 来将变量内容输出在终端（给变量赋值时，如果字符串有空格，应该用引号括起来）

```shell
salution="yes day"
echo $salution
```



- 使用read命令来将用户的输入赋值给一个变量

```shell
# read salution
wie geht's?
# echo $salution
```

### 1.2、命令

- break命令
  - 使用break命令可以提前跳出for、while、until循环。
  - 默认只跳出一层循环。


- ：冒号命令

  - 冒号命令是一个空命令。偶尔被用于简化条件逻辑，相当于true的一个别名。
  - while：实现了一个无限循环

- continue命令

  - 这个命令会让for、while、until循环跳到下一次循环继续执行，循环变量取循环列表中的下一个值。
  - continue可以带一个可选的参数以希望执行的循环嵌套层数，也就是说可以部分跳出嵌套循环。（很少使用，导致脚本程序极难理解）

- .命令

  - （.）命令用于在当前shell中执行命令：

    - ```shell
      ./shell_script
      ```

- echo命令

  - X/Open建议现代shell中使用printf命令。
  - echo命令用来输出结尾带有换行符的字符串。

- eval命令

  - 允许你对参数进行求值。他是shell的内置命令，通常不会以单独的命令形式存在。

- exit n 命令

  - 使脚本以退出码n结束允许。
  - 退出码0表示成功，退出码1-125是脚本程序可以使用的错误代码。其余数字具有保留含义。（126：文件不可执行，127：命令未找到，128及以上：出现一个信号）

- export命令

  - 将作为他参数的变量导出到子shell中，并使之在子shell中有效。

- expr命令

  - expr命令将它的参数当做一个表达式来求值。它最常见的用法就是进行简单的数学运算。

- return命令

  - 作用是使函数返回

- set命令

  - 作用是为shell设置参数变量。

  - ```shell
    set $(date)
    ```

  - ​


## 2、标准I/O库

- fopen、fclose
  - 主要用于文件和终端的输入输出。
  - fopen在成功是会返回一个非空的FILE *指针，失败是返回NULL值
  - fclose关闭指定的文件流stream，使所有未写出的数据都写出。
- fread、fwrite
  - fread用于从一个文件里读取数据。数据从文件流stream读到由ptr指向的数据缓冲区。返回成功读到数据缓冲区里的记录个数（不是字节数）。
  - fwrite:从指定的数据缓冲区里读取出数据记录，并将它们写到输出流中。返回值为成功写入的记录个数。
  - 不推荐将fread和fwrite用于结构化数据。一部分原因是fwrite写的文件在不同计算机体系结构之间可能不具备可移植性。
- fflush-
  - 把文件流里未写出的数据立刻写出。在调用fclose函数时隐含的进行了一次flush操作，因此不必再调用fclose函数之前调用fflush。
- fseek-
  - lseek系统调用对文件描述符fildes的读写指针进行设置，也就是说可以用它来设置文件的下一个读写位置。读写指针既可以被设置成为文件中的某个绝对位置，也可以把它设置为相对于当前位置或文件尾的某个相对位置。
  - fseek函数是与lseek系统调用对应的文件流函数。他在文件流里为下一次读写操作指定位置。lseek返回的是一个off_t数值，而fseek返回的是一个整数：0-表示成功，-1表示失败并设置error指出错误。
- fgetc、getc、getchar
  - fgetc函数从文件流里取出下一个字节并把它作为一个字符返回。当它到达文件尾或者出现错误时，返回EOF。
  - getchar函数作用相当于getc，它从标准输入里读取下一个字符。
- fputc、putc、putchar
  - fputc函数把一个字符写到一个输出文件流中。返回写入的值，如果写入失败，则返回EOF。
  - putc作用相当于fputc，但他可能被实现为一个宏。
  - putchar相当于putc，把单个字符写到标准输出。putchar和getchar都是将字符当做int型而不是char型来使用的。
- fgets、gets
  - fgets从输入文件流stream里读取一个字符串。将读取到的字符串写到指定的字符串里。
- printf、fprintf、sprintf
  - printf包含一个需要输出的普通字符和成为转换控制符代码的字符串，转换控制符规定了其他的参数应该以何种方式输出到何种地方。
  - fprint函数把自己的输出送到一个指定的文件流。
  - sprintf函数把自己的输出和一个结尾空字符写到作为参数传递过来的字符串里。
  - 转换控制符
    - %d,%i:以十进制格式输出一个整数。
    - %o,%x:以八进制或者十六进制格式输出一个整数。
    - %c:输出一个字符
    - %s：输出一个字符串。
    - %f:输出一个单精度浮点数。
    - %e:以科学计数法格式输出一个双精度浮点数。
    - %g：以通用格式输出一个双精度浮点数。
  - 字段限定符：负值的字段宽度表示数据在该字段里左对齐的格式输出。可变字段宽度用一个（*）来表示。%后以0开头表示数据前面要用0填充。
    - printf不对数据字段进行截断，而是扩充数据字段以适应数据的宽度。因此打印一个比字段宽度长的字符串，数据字段会加宽。
- scanf、fscanf、sscanf
  - scanf函数读入的值将保存在相应的变量中。
  - 转换控制符
    - %d:读取十进制格式整数。
    - %o,%x:读取八进制或者十六进制格式整数。
    - %f、%e、%g:读取一个浮点数
    - %c:输出一个字符
    - %s：输出一个字符串。
    - %[]:读取一个字符集合
    - %e:以科学计数法格式输出一个双精度浮点数。
    - %g：以通用格式输出一个双精度浮点数。


## 3、使用curses函数库管理基于文本的屏幕

### 3.1 curses函数库的使用

- curses函数库能够优化光标的移动并最小化需要对屏幕进行的刷新，从而也减少了必须向字符终端发送字符数目。


- curses函数库用两个数据结构来映射终端屏幕，它们是stdscr和curscr。两者中，stdscr更重要。会在curses函数产生输出时被刷新。
- 所有的curses程序必须以initscr函数开始，以endwin函数结束。
  - initscr函数在一个程序中只能调用一次。成功返回指向stdscr结构的指针；失败则会输出诊断错误信息并推出程序。
  - endwin成功返回OK，失败返回ERR。
- ​

## 4、数据管理

- 动态内存管理：Linux可以做什么以及Linux不允许做什么。
- 文件锁定：协调锁、共享文件的锁定区域和避免死锁。
- dbm数据库：一个大多数Linux系统都提供、基本的不基于SQL的数据库函数库。

### 4.1 内存管理

**除了一些特殊的嵌入式应用程序外，Linux决不允许直接访问物理内存。**

**简单的内存分配**：

```c
#include<stdlib.h>
void *malloc(size_t size);/*最常用的内存分配函数*/
void *calloc(size_t number_of_elements,size_telement_size);
void *realloc(void *existing_memory,size_t new_size);
```

- Linux不要求包含mallac.c头文件，并且用来指定待分配内存字节数量的参数size不是一个简单的整型，通常是一个无符号整型。
- malloc返回的是void指针，并且可以保证地址对齐，所以可以被转换为任何类型的指针

**分配大量的内存**：

- 刚刚开始时，内核只是通过使用空闲的物理内存来满足应用程序的内存请求，当物理内存耗尽时，会开始交换空间。
  - 交换空间是一个在安装系统时分配的独立的磁盘区域。
  - 内核会在物理内存和交换空间之间移动数据和程序代码，使得每次读写内存时数据看起来总像是已存在于物理内存中。
  - 当应用程序耗尽所有物理内存和交换空间或者当最大栈长度被超过时，内核将拒绝此后的内存请求，并可能提前终止程序的运行。
- 每个Linux系统中运行的程序都只能看到属于自己的内存映像，不同程序看到的内存映像不同。

**calloc**：为一个结构数组分配内存，需要把元素个数和每个元素的大小作为参数。他所分配的内存将全部初始化为0.calloc调用成功，会返回第一个元素的指针。

**realloc**：用来改变先前已经分配的内存块的长度。他需要传递一个指向先前通过malloc、calloc或者realloc调用分配的内存的指针，然后根据new_size的值来改变其长度。**内存被重新分配以后，应该使用新的指针去访问内存**。

### 4.2 文件锁定

### 4.2.1 创建锁文件

- 许多应用程序只需要能够针对某个资源创建一个锁文件即可。然后其他程序就可以通过检查这个文件来判断他们自己是否被允许访问这个资源。


- 锁文件只是充当一个指示器的角色，程序间需要通过相互协作来使用他们。锁文件只是**建议锁**而不是**强制锁**。

（适合用来控制对串行口等**不经常访问的文件**之类的资源独占式访问，不适合用于访问**大型的共享文件**。）

### 4.2.2 区域锁定（文件段锁定/文件区域锁定）

文件中某个特定的部分被锁定，但是其他程序可以访问这个文件中的其他部分。

使用**fcntl系统调用**和使用lockf调用。

```c
/*
/*command:设置完成不同的任务选项--F_GETLK/F_SETLK/F_SETLKW*/
/*flock_structrue:指向flock结构的指针*/
*/
int fcntl(int fildes, int command, struct flock *flock_structrue);
//flock结构依赖具体的实现，至少包含成员
{
  short l_type;
  short l_whence;
  off_t l_start;
  off_t l_len;
  pid_t l_pid;
}

```

**l_type**成员取值定义在fcntl.h头文件中

- F_RDLCK:共享（或读）锁。多个不同的进程可以拥有文件同一个区域上的共享锁。只要有一个进程拥有了共享锁，那么就不会有另外的进程可以拥有该区域的独占锁。以“读”或者“读/写”方式打开文件。
- F_UNLCK:解锁，清除锁。
- F_WRLCK:独占（写）锁。一个进程可以拥有文件任意一个特定区域的独占锁。一旦有进程拥有了独占锁，其他的进程都无法在该区域获得任何类型的锁。以“写”或者“读/写”方式打开文件。

**l_whence、l_start、l_len:**定义了文件中的一个区域，即一个连续的字节集合。

- l_whence：通常设置为SEEK_SET
  - SEEK_SET:对应文件头；
  - SEEK_CUR：当前位置；
  - SEEK_END：文件尾
- l_start：该区域的第一个字节
- l_len：定义了该区域的字节数。

l_pid：用来记录持有锁的进程。

command:设置完成不同的任务选项

- F_GETLK：用于获取fildes打开的文件的锁信息。
- F_SETLK：对fildes指向的文件的某个区域加锁或解锁。
- F_SETLKW：与F_SETLK命令作用相同，但是在无法获取锁时，这个调用会一直等到能获取到锁或者受到信号为止。

文件区域加锁之后，必须使用底层的**read和write**调用来访问文件中的数据，而不要使用更高级的fread和fwrite调用。fread和fwrite会对读写的数据进行缓存，执行一次fread会读取超过size的数据，并将多余的数据在函数库中进行缓存。

## 5、进程和信号

- 进程的结构、类型和调度
- 用不同的方法启动新进程
- 父进程、子进程和僵尸进程
- 什么是信号，怎么使用

### 5.1、进程的结构

**系统进程**：使用ps 命令查看进程

![进程说明](.\pic\进程说明.png)

stat一列是用来表明进程的当前状态。

**进程调度**：Linux内核用进程调度器来决定下一个时间片应该分配给哪个进程。判断依据为进程的优先级。

​	在一台单处理器计算机上，同一时间只能有一个进程可以运行，其他进程处于等待运行状态。每个程序轮到的运行时间是相当短暂的，故产生一种多个程序同时运行的假象。

### 5.2、启动新进程

在程序内部可以采用system库函数来创建一个新进程。(在代码中使用**并不理想**)

```c
#include<stdlib.h>
int system(const char * string);
```

替换进程映像：**exec系列函数**可以将当前进程替换为一个新进程，新进程有**path**和**file**参数决定。可以使用exec函数将程序的执行从一个程序切换到另一个程序。（**exec函数比system函数更有效**）

复制进程映像：要想让进程同时执行多个函数，我么们可以使用多线程或者从原程序中创建一个完全分离的进程。

可以通过调用**fork**创建一个新进程。这个系统调用复制当前进程，在进程表中创建一个新的表项，新表项中的许多属性与当前进程相同。新进程几乎与原进程一模一样，执行代码也完全相同，但是新进程有自己的数据空间、环境和文件描述符。

```c
#include<sys/types.h>
#include<unistd.h>
pid_t fork(void);
```

![fork调用](https://gitee.com/harfe/mybatis/blob/master/%E5%B5%8C%E5%85%A5%E5%BC%8F/pic/fork%E8%B0%83%E7%94%A8.png)

fork调用返回的是新的子进程的PID。新进程继续执行，子进程中的fork调用返回的是0。

#### 5.2.1、等待一个进程

可以通过在父进程中调用wait函数让父进程等待子进程的结束。

```c
#include<sys/types.h>
#include<sys/wait.h>
/*返回子进程的PID*/
pid_t wait(int * stat_loc);
```

#### 5.2.2僵尸进程

子进程终止时，它与父进程之间的关联还会保持，直到父进程也正常终止，或者父进程调用wait才告结束，因此进程表中代表子进程的表项不会立马释放。虽然子进程以及不再运行，但他任然存在与系统中，这时就成为了一个死进程（僵尸进程）。

### 5.3信号

信号是UNIX和Linux系统响应某些条件而产生的一个事件。信号是由某些错误条件而生产的，如内存段冲突、浮点处理器错误或者非法指令等。它们由shell和终端处理器生成来引起中断，它们还可以作为在进程间传递消息或者修改行为的一种方式，明确的由一个进程发送给另一个进程。信号可以被**生成**、**捕获**、**响应**或者**忽略**。

![信号](.\pic\信号.png)

如果进程接收到这些信号中的一个，但是事先没有安排捕获它，进程将会立刻终止。

程序可以用signal库函数来处理信号

```c
#include<signal.h>
/*
signal是一个带sig和func两个参数的函数。准备捕获或忽略的信号由参数sig给出，接受到指定的信号后将要调用的函数由参数func给出，信号处理函数必须有一个int类型的参数（接受到的信号代码），并且返回类型为void
*/
void (*signal(int sig, void (*func)(int)))(int);
```

```c
#include<signal.h>
#include<stdlib.h>
#include<stdio.h>
#include<unistd.h>
/*对通过参数sig传递进来的信号做出相应。信号出现时，程序调用该函数。*/
void ouch(int sig)
{
    printf("OUCH - I got signal %d\r\n", sig);
    (void) signal(SIGINT,SIG_DFL);
}
int main()
{
    (void) signal(SIGINT,ouch);
    while (1)
    {
        /* code */
        printf("Hello World!\r\n");
        sleep(1);
    }
    
}
```

一般不使用signal接口，在程序中应该使用**sigaction**函数，它定义更清晰、执行更可靠。

```c
#include<signal.h>
int sigaction(int sig, const struct sigaction *act, struct sigaction *oact);
```

结构定义在signal.h中，它的作用是定义在接受到参数sig指定的信号后应该采取的行动。该结构至少包含以下几个成员：

![sigaction](.\pic\sigaction.png)



#### 5.3.1发送信号

进程可以通过调用**kill**函数向包含它本身在内的其他进程发送一个信号。如果程序没有发送该信号的权限，对kill函数的调用就会失败。（原因通常是目标进程由另一个用户所拥有。）

alarm函数：用来在second秒之后安排发送一个SIGALRM信号。但是由于处理的延时和时间调度的不确定性，实际闹钟时间将比预先安排的要稍微拖后一点。将seconds设置为0，将取消所有已设置的闹钟请求。如果在接受到SIGALRM信号之前再次调用alarm函数，则闹钟开始重新计时。每个进程只能有一个闹钟时间。

pause函数：将程序挂起直到程序接收到一个信号时，预设好的信号处理函数将开始运行。

#### 5.3.2信号集

```c
#include<signal.h>
/*从信号集中增加给定的信号signo*/
int sigaddset(sigset_t *set, int signo);
/*将信号集初始化为空*/
int sigemptyset(sigset_t *set);
/*将信号集初始化为包含所有已定义的信号*/
int sigfillset(sigset_t *set);
/*从信号集中删除给定的信号signo*/
int sigdelset(sigset_t * set, int signo);
/*判断一个给定信号是否是一个信号集的成员:是返回1，不是返回0，无效信号返回-1*/
int sigismember(sigset_t * set, int signo);
/*负责进程的信号屏蔽字的设置或者检查*/
int sigprocmask(int how,const sigset_t *set,sigset_t *oset);
/*将被阻塞的信号中停留在待处理状态的一组信号写到参数set指向的信号集中*/
int sigpending(sigset_t *set);
/*将自己挂起，指导信号集中的一个信号到达为止*/
int sigsuspend(const sigset_t *sigmask);
```

## 6、线程

- 在进程中创建新线程
- 在一个进程中同步线程之间的数据访问
- 修改线程的属性
- 在同一个进程中，从一个线程中控制另一个线程

在一个程序中的多个执行路线叫**线程**：进程内部的一个控制序列。

线程优缺点：

- 优点：
  - 新线程的创建代价比新进程小
  - 能让程序看起来在同时做多件事情
  - 能将程序的几个部分分成多个线程执行
  - 线程之间的切换需要操作系统做的工作要比进程之间的切换少得多
- 缺点：
  - 编程需要很仔细
  - 调试困难
  - 对硬件有一定的要求

创建一个新线程：

```c
#include<pthread.h>
/*
*thread:指向pthread_t类型数据结构的指针
*attr:用于设置线程的属性，一般不需要特殊的属性，只需要设置为NULL即可
*
*/
int pthread_create(pthread_t *thread, pthread_attr_t *attr, void *(*start_routine)(void *),void *arg);
```

终止线程：

```c
void pthread_exit(void *retval);
```

### 6.1同步

#### 6.1.1用信号量同步

- 二进制信号量
  - 只有0和1两种取值。保护一段代码，使其每次只能被一个执行线程运行。
- 计数信号量
  - 有更大的取值范围，可以允许有限数目的线程执行一段指定的代码。（不常用）

```c
#include<semaphore.h>
/*初始化函数*/
/*
*pshared:控制信号量的类型，0：表示信号量是当前进程的局部信号量，否则这个信号量可以在多个进程之间共享。
*/
int sem_init(sem_t *sem, int pshared, unsigned int value);
/*以原子操作的方式给信号量的值减1，等到信号量出现非零值才开始操作，否则会一直等待。*/
int sem_wait(sem_t * sem);
/*以原子操作的方式给信号量的值加1：两个线程企图给一个信号量加1，两个进程之间互不干扰，最后信号量值被加2*/
int sem_post(sem_t * sem);
```

### 6.1.2用互斥量进行同步

它允许程序员锁住某个对象，使得每次只能有一个线程访问它。为了控制对关键代码的访问，必须在进入这段代码之前锁住一个互斥量，然后在完成操作之后解锁它。

```c
#include<pthread.h>
int pthread_mutex_init(pthread_mutex * mutex,const pthread_mutexattr_t *mutexattr);
int pthread_mutex_lock(pthread_mutex * mutex);
int pthread_mutex_unlock(pthread_mutex * mutex);
int pthread_mutex_destroy(pthread_mutex * mutex);
```

如果程序试图对一个已经加了锁的互斥量调用pthread_mutex_lock函数，程序就会被阻塞，而又因为拥有互斥量的这个线程正是现在被阻塞的线程，所以互斥量就永远不会被解锁，程序也就进入了**死锁状态**。

### 6.2线程的属性

**脱离线程**：在主线程为用户提供服务的同时创建第二个进程，第二个进程的工作结束可以直接终止，并不需要再回到主线程中.

相关函数：

```c
#include<pthread.h>
int pthread_attr_init(pthread_attr_t * attr);
int pthread_attr_setdetachstate(pthread_attr_t * attr, int detachstate);
int pthread_attr_getdetachstate(const pthread_attr_t * attr, int detachstate);
int pthread_attr_setschedpolicy(pthread_attr_t * attr, int policy);
int pthread_attr_getschedpolicy(pthread_attr_t * attr, int policy);
int pthread_attr_setschedparam(pthread_attr_t * attr, const struct sched_param *param);
int pthread_attr_getschedparam(pthread_attr_t * attr, const struct sched_param *param);
int pthread_attr_setinheritsched(pthread_attr_t * attr, int *inherit);
int pthread_attr_getinheritsched(pthread_attr_t * attr, int *inherit);
int pthread_attr_setscope(pthread_attr_t * attr, int *scope);
int pthread_attr_getscope(pthread_attr_t * attr, int *scope);
int pthread_attr_setstacksize(pthread_attr_t * attr, int *scope);
int pthread_attr_getstacksize(pthread_attr_t * attr, int *scope);
```

- detachstate:允许我们无需对线程进行重新合并。
- schedpolicy：控制线程的调度方式。
- schedparam：这个属性是和schedpolicy属性结合使用的，它可以对以sched_other策略运行的线程的调度进行控制。
- inheritsched：有两个取值
  - pthread_explicit：表示调度由属性明确的设置（默认取值）
  - pthread_inherit_sched：新线程将沿用其创建者所使用的参数。
- scope：控制一个线程调度的计算方式。
- stacksize：控制线程创建的栈大小（字节）。

### 6.3取消一个线程

线程可以在被要求终止时改变其行为。

线程终止的函数：

```c
#include<pthread.h>
/*提供一个线程标识符，我们就能发送请求来取消它*/
int pthread_cancel(pthread_t thread);
/*
*state:PTHREAD_CANCEL_ENABLE,允许线程接收取消请求；*PTHREAD_CANCEL_DISABLE，忽略取消请求。
*oldstate：用于获取先前的取消状态。
*/
int pthread_setcancelstate(int state, int *oldstate);
```

如果取消请求被接受了，线程就可以进入到第二个控制层次。

```c
#include<pthread.h>
/*
*type的取值：
*PTHREAD_CANCEL_ASYNCHRONOUS:将使得在接受到取消请求后立即采取行动；
*PTHREAD_CANCEL_DEFERRED：它将使得在接收到取消请求后一直等待直到线程执行了一些函数之一才会采取行动。
*old参数可以保存先前的状态，如果不想直到先前的状态可以穿NULL；
*/
int pthread_setcanceltype(int type,int *oldtype);
```

## 7、进程间通信：管道

- 管道的定义
- 进程管道
- 管道调用
- 父进程和子进程
- 命名管道：FIFO
- 客户/服务器架构

管道：当从一个进程连接数据流到另一个进程时，就称为**管道**

### 7.1进程管道

- popen函数
- PClose函数

```c
#include<stdio.h>
/*
*open_mode:只能取r和w
****r:被调用程序的输出就可以被调用程序使用，调用程序利用popen函数返回的FILE*文件流指针就可以通过常用的stdio库函数来读取被调用程序的输出。
****w:调用程序就可以用fwrite调用向被调用程序发送数据，而被调用程序可以在自己的标准输入上读取这些数据。
*
*/
FILE *popen(const char * command, const char *open_mode);
/*
*pclose函数：用popen启动的进程结束时，我们可以用PClose函数关闭与之关联的文件流。
*/
int pclose(FILE * stream_close);
```



popen函数：允许一个程序将另一个程序作为新进程来启动，并且可以传递数据给它或者通过它接收数据。

#### 7.1.1实现popen

请求popen调用运行一个程序时，它首先启动shell，然后将command字符串作为一个参数传递给它。

- 优点：在启动程序之前，先启动shell来分析命令字符串，就可以使用各种shell拓展在程序启动之前就全部完成。它允许我们通过popen启动非常复杂的shell命令。
- 缺点：针对每一个popen调用，不仅要启动一个被请求的程序，还要启动一个shell，每个popen调用将多启动两个进程，导致popen函数的调用成本较高，对目标命令的调用比正常方式慢一些。

### 7.2pipe调用

pipe函数在两个程序之间传递数据时不需要启动一个shell来解释请求的命令。该函数在数组中填写两个新的文件描述符后返回0，如果失败则返回-1并设置errno来表明失败的原因。

- EMFILE：进程使用的文件描述符过多
- ENFILE：系统的文件表已满
- EFAULT：文件描述符无效

```c
#include<unistd.h>
/*
*参数是一个由两个整数类型的文件描述符组成的数组的指针。
*/
int pipe(int file_descriptor[2]);
```

两个返回的文件描述符以一种特殊的方式连接起来，写在file_descriptor[1]的所有数据都可以从file_descriptor[0]读回来。处理方式采用**先进先出**。一般采用file_descriptor[1]写数据，file_descriptor[0]读数据，如果不这样可能会调用失败并返回-1。

pipe函数调用采用的是**文件描述符**而不是文件流，因此必须使用底层的**read和write**调用来访问数据，而不能用文件流库函数fread和fwrite。

### 7.3父进程和子进程

父进程和子进程之间的数据读写关系：父进程将数据通过文件描述符file_pipes[1]写进管道，子进程通过file_pipes[0]从管道中读取数据

![pipe](E:\zhf\笔记\Gitee\mybatis\嵌入式\pic\pipe.png)



