## 本文让你理解

1. 什么是IO多路复用
2. IO多路复用解决什么问题
3. 目前有哪些IO多路复用的方案
4. 具体怎么用
5. 不同IO多路复用方案优缺点

## 1. 什么是IO多路复用

一句话解释：单线程或单进程同时监测若干个文件描述符是否可以执行IO操作的能力。

## 2. 解决什么问题

## 说在前头

应用程序通常需要处理来自多条事件流中的事件，比如我现在用的电脑，需要同时处理键盘鼠标的输入、中断信号等等事件，再比如web服务器如nginx，需要同时处理来来自N个客户端的事件。

> 逻辑控制流在时间上的重叠叫做 **并发**

而CPU单核在同一时刻只能做一件事情，一种解决办法是对CPU进行时分复用(多个事件流将CPU切割成多个时间片，不同事件流的时间片交替进行)。在计算机系统中，我们用线程或者进程来表示一条执行流，通过不同的线程或进程在操作系统内部的调度，来做到对CPU处理的时分复用。这样多个事件流就可以并发进行，不需要一个等待另一个太久，在用户看起来他们似乎就是并行在做一样。

但凡事都是有成本的。线程/进程也一样，有这么几个方面：

1. 线程/进程创建成本
2. CPU切换不同线程/进程成本 [Context Switch](https://link.zhihu.com/?target=https%3A//en.wikipedia.org/wiki/Context_switch)
3. 多线程的资源竞争

有没有一种可以在单线程/进程中处理多个事件流的方法呢？一种答案就是IO多路复用。

因此IO多路复用解决的本质问题是在**用更少的资源完成更多的事**。

为了更全面的理解，先介绍下在Linux系统下所有IO模型。

## I/O模型

目前Linux系统中提供了5种IO处理模型

1. 阻塞IO
2. 非阻塞IO
3. IO多路复用
4. 信号驱动IO
5. 异步IO

### 阻塞IO

这是最常用的简单的IO模型。阻塞IO意味着当我们发起一次IO操作后一直等待成功或失败之后才返回，在这期间程序不能做其它的事情。阻塞IO操作只能对单个文件描述符进行操作，详见[read](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/read.2.html)或[write](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/write.2.html)。

### 非阻塞IO

我们在发起IO时，通过对文件描述符设置O_NONBLOCK flag来指定该文件描述符的IO操作为非阻塞。非阻塞IO通常发生在一个for循环当中，因为每次进行IO操作时要么IO操作成功，要么当IO操作会阻塞时返回错误EWOULDBLOCK/EAGAIN，然后再根据需要进行下一次的for循环操作，这种类似轮询的方式会浪费很多不必要的CPU资源，是一种糟糕的设计。和阻塞IO一样，非阻塞IO也是通过调用[read](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/read.2.html)或write[write](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/write.2.html)来进行操作的，也只能对单个描述符进行操作。

### IO多路复用

IO多路复用在Linux下包括了三种，[select](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/select.2.html)、[poll](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/poll.2.html)、[epoll](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man7/epoll.7.html)，抽象来看，他们功能是类似的，但具体细节各有不同：首先都会对一组文件描述符进行相关事件的注册，然后阻塞等待某些事件的发生或等待超时。更多细节详见下面的 "具体怎么用"。IO多路复用都可以关注多个文件描述符，但对于这三种机制而言，不同数量级文件描述符对性能的影响是不同的，下面会详细介绍。

### 信号驱动IO

[信号驱动IO](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man7/signal.7.html)是利用信号机制，让内核告知应用程序文件描述符的相关事件。这里有一个信号驱动IO相关的[例子](https://link.zhihu.com/?target=https%3A//github.com/troydhanson/network/blob/master/tcp/server/sigio-server.c)。

但信号驱动IO在网络编程的时候通常很少用到，因为在网络环境中，和socket相关的读写事件太多了，比如下面的事件都会导致SIGIO信号的产生：

1. TCP连接建立
2. 一方断开TCP连接请求
3. 断开TCP连接请求完成
4. TCP连接半关闭
5. 数据到达TCP socket
6. 数据已经发送出去(如：写buffer有空余空间)

上面所有的这些都会产生SIGIO信号，但我们没办法在SIGIO对应的信号处理函数中区分上述不同的事件，SIGIO只应该在IO事件单一情况下使用，比如说用来监听端口的socket，因为只有客户端发起新连接的时候才会产生SIGIO信号。

### 异步IO

异步IO和信号驱动IO差不多，但它比信号驱动IO可以多做一步：相比信号驱动IO需要在程序中完成数据从用户态到内核态(或反方向)的拷贝，异步IO可以把拷贝这一步也帮我们完成之后才通知应用程序。我们使用 [aio_read](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man3/aio_read.3.html) 来读，[aio_write](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man3/aio_write.3.html) 写。

> 同步IO vs 异步IO 1. 同步IO指的是程序会一直阻塞到IO操作如read、write完成 2. 异步IO指的是IO操作不会阻塞当前程序的继续执行
> 所以根据这个定义，上面阻塞IO当然算是同步的IO，非阻塞IO也是同步IO，因为当文件操作符可用时我们还是需要阻塞的读或写，同理IO多路复用和信号驱动IO也是同步IO，只有异步IO是完全完成了数据的拷贝之后才通知程序进行处理，没有阻塞的数据读写过程。

## 3. 目前有哪些IO多路复用的方案

## 解决方案总览

Linux: select、poll、epoll

MacOS/FreeBSD: kqueue

Windows/Solaris: [IOCP](https://link.zhihu.com/?target=https%3A//en.wikipedia.org/wiki/Input/output_completion_port)

## 常见软件的IO多路复用方案

redis: Linux下 epoll(level-triggered)，没有epoll用select

nginx: Linux下 epoll(edge-triggered)，没有epoll用select

## 4. 具体怎么用

我在工作中接触的都是Linux系统的服务器，所以在这里只介绍Linux系统的解决方案

### select

相关函数定义如下

```
/* According to POSIX.1-2001, POSIX.1-2008 */
    #include <sys/select.h>

    /* According to earlier standards */
    #include <sys/time.h>
    #include <sys/types.h>
    #include <unistd.h>

    int select(int nfds, fd_set *readfds, fd_set *writefds,
                fd_set *exceptfds, struct timeval *timeout);

    int pselect(int nfds, fd_set *readfds, fd_set *writefds,
                fd_set *exceptfds, const struct timespec *timeout,
                const sigset_t *sigmask);

    void FD_CLR(int fd, fd_set *set);
    int  FD_ISSET(int fd, fd_set *set);
    void FD_SET(int fd, fd_set *set);
    void FD_ZERO(fd_set *set);
```

select的调用会阻塞到有文件描述符可以进行IO操作或被信号打断或者超时才会返回。

select将监听的文件描述符分为三组，每一组监听不同的需要进行的IO操作。readfds是需要进行读操作的文件描述符，writefds是需要进行写操作的文件描述符，exceptfds是需要进行[异常事件](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/poll.2.html)处理的文件描述符。这三个参数可以用NULL来表示对应的事件不需要监听。

当select返回时，每组文件描述符会被select过滤，只留下可以进行对应IO操作的文件描述符。

FD_xx系列的函数是用来操作文件描述符组和文件描述符的关系。

FD_ZERO用来清空文件描述符组。每次调用select前都需要清空一次。

```
fd_set writefds;
FD_ZERO(&writefds)
```

FD_SET添加一个文件描述符到组中，FD_CLR对应将一个文件描述符移出组中

```
FD_SET(fd, &writefds);
FD_CLR(fd, &writefds);
```

FD_ISSET检测一个文件描述符是否在组中，我们用这个来检测一次select调用之后有哪些文件描述符可以进行IO操作

```
if (FD_ISSET(fd, &readfds)){
  /* fd可读 */
}
```

select可同时监听的文件描述符数量是通过FS_SETSIZE来限制的，在Linux系统中，该值为1024，当然我们可以增大这个值，但随着监听的文件描述符数量增加，select的效率会降低，我们会在『不同IO多路复用方案优缺点』一节中展开。

pselect和select大体上是一样的，但有一些细节上的[区别](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/select.2.html)。

[打开链接查看完整的使用select的例子](https://link.zhihu.com/?target=https%3A//gist.github.com/hechen0/c134d2722fe8861288e060dd11e0f9c4)

### poll

相关函数定义

```
    #include <poll.h>

    int poll(struct pollfd *fds, nfds_t nfds, int timeout);

    #include <signal.h>
    #include <poll.h>

    int ppoll(struct pollfd *fds, nfds_t nfds,
            const struct timespec *tmo_p, const sigset_t *sigmask);

    struct pollfd {
        int fd; /* file descriptor */
        short events; /* requested events to watch */
        short revents; /* returned events witnessed */
    };
```

和select用三组文件描述符不同的是，poll只有一个pollfd数组，数组中的每个元素都表示一个需要监听IO操作事件的文件描述符。events参数是我们需要关心的事件，revents是所有内核监测到的事件。合法的事件可以参考[这里](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/poll.2.html)。

[打开链接查看完整的使用poll的例子](https://link.zhihu.com/?target=https%3A//gist.github.com/hechen0/1bbc107793ec6cc3b00cbe7d3a54dd29)

### [epoll](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man7/epoll.7.html)

相关函数定义如下

```
    #include <sys/epoll.h>

    int epoll_create(int size);
    int epoll_create1(int flags);

    int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event);

    int epoll_wait(int epfd, struct epoll_event *events,
                int maxevents, int timeout);
    int epoll_pwait(int epfd, struct epoll_event *events,
                int maxevents, int timeout,
                const sigset_t *sigmask);
```

[epoll_create](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/epoll_create.2.html)&[epoll_create1](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/epoll_create.2.html)用于创建一个epoll实例，而[epoll_ctl](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/epoll_ctl.2.html)用于往epoll实例中增删改要监测的文件描述符，[epoll_wait](https://link.zhihu.com/?target=http%3A//man7.org/linux/man-pages/man2/epoll_wait.2.html)则用于阻塞的等待可以执行IO操作的文件描述符直到超时。

[打开链接查看完整的使用epoll的例子](https://link.zhihu.com/?target=https%3A//github.com/millken/c-example/blob/master/epoll-example.c)

### level-triggered and edge-triggered

这两种底层的事件通知机制通常被称为水平触发和边沿触发，真是翻译的词不达意，如果我来翻译，我会翻译成：状态持续通知和状态变化通知。

这两个概念来自电路，triggered代表电路激活，也就是有事件通知给程序，level-triggered表示只要有IO操作可以进行比如某个文件描述符有数据可读，每次调用epoll_wait都会返回以通知程序可以进行IO操作，edge-triggered表示只有在文件描述符状态发生变化时，调用epoll_wait才会返回，如果第一次没有全部读完该文件描述符的数据而且没有新数据写入，再次调用epoll_wait都不会有通知给到程序，因为文件描述符的状态没有变化。

select和poll都是状态持续通知的机制，且不可改变，只要文件描述符中有IO操作可以进行，那么select和poll都会返回以通知程序。而epoll两种通知机制可选。

### 状态变化通知(edge-triggered)模式下的epoll

在epoll状态变化通知机制下，有一些的特殊的地方需要注意。考虑下面这个例子

1. 服务端文件描述符rfd代表要执行read操作的TCP socket，rfd已被注册到一个epoll实例中
2. 客户端向rfd写了2kb数据
3. 服务端调用epoll_wait返回，rfd可执行read操作
4. 服务端从rfd中读取了1kb数据
5. 服务端又调用了一次epoll_wait

在第5步的epoll_wait调用不会返回，而对应的客户端会因为服务端没有返回对应的response而超时重试，原因就是我上面所说的，epoll_wait只会在状态变化时才会通知程序进行处理。第3步epoll_wait会返回，是因为客户端写了数据，导致rfd状态被改变了，第3步的epoll_wait已经消费了这个事件，所以第5步的epoll_wait不会返回。

我们需要配合非阻塞IO来解决上面的问题：

1. 对需要监听的文件描述符加上非阻塞IO标识
2. 只在read或者write返回EAGAIN或EWOULDBLOCK错误时，才调用epoll_wait等待下次状态改变发生

通过上述方式，我们可以确保每次epoll_wait返回之后，我们的文件描述符中没有读到一半或写到一半的数据。

## 5. 不同IO多路复用方案优缺点

## poll vs select

poll和select基本上是一样的，poll相比select好在如下几点：

1. poll传参对用户更友好。比如不需要和select一样计算很多奇怪的参数比如nfds(值最大的文件描述符+1)，再比如不需要分开三组传入参数。
2. poll会比select性能稍好些，因为select是每个bit位都检测，假设有个值为1000的文件描述符，select会从第一位开始检测一直到第1000个bit位。但poll检测的是一个数组。
3. select的时间参数在返回的时候各个系统的处理方式不统一，如果希望程序可移植性更好，需要每次调用select都初始化时间参数。

而select比poll好在下面几点

1. 支持select的系统更多，兼容更强大，有一些unix系统不支持poll
2. select提供精度更高(到microsecond)的超时时间，而poll只提供到毫秒的精度。

但总体而言 select和poll基本一致。

## epoll vs poll&select

epoll优于select&poll在下面几点：

1. 在需要同时监听的文件描述符数量增加时，select&poll是O(N)的复杂度，epoll是O(1)，在N很小的情况下，差距不会特别大，但如果N很大的前提下，一次O(N)的循环可要比O(1)慢很多，所以高性能的网络服务器都会选择epoll进行IO多路复用。
2. epoll内部用一个文件描述符挂载需要监听的文件描述符，这个epoll的文件描述符可以在多个线程/进程共享，所以epoll的使用场景要比select&poll要多。

## 总结

本文从使用者的角度，从问题出发，介绍了多种IO多路复用方案，有任何问题欢迎在下方留言交流，或扫描二维码/微信搜索『技术成长之道』关注公众号后留言私信。

PS：代码永远是最正确的，man文档其次，更多细节可以多看代码和文档。