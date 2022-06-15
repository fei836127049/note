## ISIS基础概念

IntermediateSystem to Intermediate System中间系统到中间系统最初是ISO（国际标准化组织）为CLNP（ConnectionLess Network Protocol，无连接网络协议）设计的一种动态路由协议，也是一种基于链路状态并使用最短路径优先算法（SPF）进行路由计算的一种IGP协议。



IS-IS是一种链路状态路由协议，每一台路由器都会生成一个LSP（链路状态包），它是该路由器所有使能IS-IS协议接口的链路状态信息的集合。通过跟相邻设备建立IS-IS邻接关系，互相更新本地的LSDB（链路状态数据库），使LSDB与整个IS-IS网络其他设备的LSDB同步，然后根据LSDB运用SPF算法计算出IS-IS路由。如果此IS-IS路由是到目的地址的最优路由，则此路由会记录到IP路由表中，并指导报文的转发。

## ISIS两种网络类型

P2P点到点网络类型（点对点网络不选举DIS）和广播型网络

Ps:由于ISIS在广播网络中和在p2p网络中建立邻居的方式不同，因此，在网络中，链路两端的ISIS接口的网络类型必须一致，否则无法建立起邻居关系

接口修改网络类型命令：

**isis circuit-type**

**undo isis circuit-type**

 

## ISIS拓扑结构

ISIS网络地址结构：区域 ID (1 字节)+系统 ID（6 个字节）+SEL （1 个字节）

![img](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image001.png)

同一个区域的区域ID相同，每一个路由器有属于自己的ID（system Id），SEL的作用类似IP中的“协议标识符”，不同的传输协议对应不同的SEL。在IP上SEL均为00。

 

![https://img-blog.csdnimg.cn/20200409190426556.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NTc4NTQ1,size_16,color_FFFFFF,t_70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image002.png)

ISIS在自制系统类采用骨干区域和非骨干区域两级分层结构

![img](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)

骨干区域区域的路由器级别都为level-2.

level-2路由器负责区域间的路由，它可以与统一区域或者不同区域的level-2路由器，或者level-1-2路由器形成邻居关系。Level-2路由器只维护level-2的LSDB。

非骨干区域的路由级别为level-1和level-1-2。

Level-1负责区域内的路由，他只与同一区域内的level-1和level-1-2路由器形成邻居关系，不同区域的level-1路由器不能形成邻居关系。Level-1路由器只维护本区域内level-1的LSDB，只包含本区域内的路由信息，要发送到区域外的报文都转发给level-1-2路由器。

Level-1-2同时属于level-1和level-2，可以与同一区域的level-1路由器和level-1-2路由器形成邻居，也可以与不同区域的level-2路由器和level-1-2路由器形成邻居，level-1路由器只能通过level-1-2路由器才能与其他区域相连接。Level-1-2路由器维护两个LSDB，Level-1的LSDB用于区域内路由，Level-2的LSDB用于区域间路由。

## ISIS DIS（指定中间系统）

在广播网络中，ISIS需要在所有的路由器中选举出一个路由器作为DIS（指定中间系统）。DIS用来创建和更新伪节点，并负责生成伪节点的链路状态数据单元LSP。

伪节点用来模仿广播型网络的一个虚拟节点。

Level-1和Level-2的DIS是分别选举的，用户可以为不同级别的DIS选举设置不同的优先级。DIS优先级数值最大的被选为DIS。如果优先级数值最大的路由器有多台，则其中MAC地址最大的路由器会被选中。不同级别的DIS可以是同一台路由器，也可以是不同的路由器。

在路由器中，选举DIS依据接口的**priority**，**缺省情况下接口的**priority都是一样的，那么选举DIS依据接口的Mac地址，Mac地址最大的接口被选举为DIS。

可以修改接口的**priority**来配置IS-IS接口的DIS优先级。

**isis dis-priority priority**整数形式，取值范围是0～127

使用dis isis interface查看路由器接口状态：

![img](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)****

使用**dis isis interface verbose**查看接口细节：

![img](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image007.png)****

## ISIS 路由算法（SPF）Dijkstra

![https://img-blog.csdnimg.cn/20190215234743669.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXlhZGFv,size_16,color_FFFFFF,t_70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image009.jpg)

![https://img-blog.csdnimg.cn/20190215234818505.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXlhZGFv,size_16,color_FFFFFF,t_70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image011.jpg)

SPF算法中的三个数据库：

1)树数据库——通过向数据库中添加链路实现向最短路径树中添加分支。当算法完成时，这个数据库可以描述最短路径树。

2)候选对象数据库——按照规定的顺序从链路状态数据库中复制链路到该数据库中，作为向树数据库添加的候选对象。算法完成时该数据库为空。

3)链路状态数据库——保存着网络中的所有链路。

SPF算法计算过程：

第1步：路由器初始化树数据库，将自己作为树的根。代价为0.

第2步：查看链路状态数据库，将所有描述通向根的路由器邻居链路三元组添加到候选对象数据库中。

第3步：计算从根到每条链路的代价，将候选对象数据库中代价最小的链路添加到树数据库中，如果2条或多条链路距离根的最小代价相同，选择其中一条添加。

第4步：检查添加到树数据库中的邻居ID，除了已经添加到树数据库中的三元组之外，将链路状态数据库中描述路由器邻居的三元组都添加到候选对象数据库中。

第5步：如果候选对象数据库中还有剩余的表项，回到第3步。如果候选对象数据库为空，那么终止算法。在算法终止时，树数据库中的每个单一的邻居ID表项将表示一台路由器。此时，最短路径树构建完成。

第6步：添加末梢网络。到此SPF算法完成。

以拓扑图中的路由器A为例，计算最短路径树:

![https://img-blog.csdnimg.cn/20190215234920118.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1ZXlhZGFv,size_16,color_FFFFFF,t_70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image013.jpg)

 

## ISIS的LSP交互过程

LSP产生的原因：

IS-IS路由域内的所有路由器都会产生LSP，以下事件会触发一个新的LSP：

- 邻居Up或Down
- IS-IS相关接口Up或Down
- 引入的IP路由发生变化
- 区域间的IP路由发生变化
- 接口被赋了新的metric值
- 周期性更新

收到邻居新的LSP的处理过程:

将接收的新的LSP合入到自己的LSDB数据库中，并标记为flooding。

发送新的LSP到除了收到该LSP的接口之外的接口。

邻居再扩散到其他邻居。

LSP的"泛洪":

LSP报文的“泛洪”（flooding）是指当一个路由器向相邻路由器通告自己的LSP后，相邻路由器再将同样的LSP报文传送到除发送该LSP的路由器外的其它邻居，并这样逐级将LSP传送到整个层次内所有路由器的一种方式。通过这种“泛洪”，整个层次内的每一个路由器就都可以拥有相同的LSP信息，并保持LSDB的同步。

每一个LSP都拥有一个标识自己的4字节的序列号。在路由器启动时所发送的第一个LSP报文中的序列号为1，以后当需要生成新的LSP时，新LSP的序列号在前一个LSP序列号的基础上加1。更高的序列号意味着更新的LSP。

广播链路中新加入路由器与DIS同步LSDB数据库的过程：

![https://img-blog.csdn.net/20180524172454933?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjY1MTM3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image014.png)

 

图：广播链路数据库更新过程

如上图所示，新加入的路由器RouterC首先发送Hello报文，与该广播域中的路由器建立邻居关系。

建立邻居关系之后，RouterC等待LSP刷新定时器超时，然后将自己的LSP发往组播地址（Level-1：01-80-C2-00-00-14；Level-2：01-80-C2-00-00-15）。这样网络上所有的邻居都将收到该LSP。

该网段中的DIS会把收到RouterC的LSP加入到LSDB中，并等待CSNP报文定时器超时并发送CSNP报文，进行该网络内的LSDB同步。

RouterC收到DIS发来的CSNP报文，对比自己的LSDB数据库，然后向DIS发送PSNP报文请求自己没有的LSP。

DIS收到该PSNP报文请求后向RouterC发送对应的LSP进行LSDB的同步。

在上述过程中DIS的LSDB更新过程如下：

- DIS接收到LSP，在数据库中搜索对应的记录。若没有该LSP，则将其加入数据库，并广播新数据库内容。
- 若收到的LSP序列号大于本地LSP的序列号，就替换为新报文，并广播新数据库内容；若收到的LSP序列号小本地LSP的序列号，就向入端接口发送本地LSP报文。
- 若收到的LSP和本地LSP的序列号相等，则比较Remaining Lifetime。若收到的LSP报文的Remaining Lifetime为0，则将本地的报文替换为新报文，并广播新数据库内容；若收到的LSP报文的RemainingLifetime不为0而本地LSP报文的Remaining Lifetime为0，就向入端接口发送本地LSP报文。
- 若两个序列号和Remaining Lifetime都相等，则比较Checksum。若收到的LSP的Checksum大于本地LSP的Checksum，就替换为新报文，并广播新数据库内容；若收到的LSP的Checksum小于本地LSP的Checksum，就向入端接口发送本地LSP报文。
- 若两个序列号、Remaining Lifetime和Checksum都相等，则不转发该报文。

P2P链路上LSDB数据库同步过程：

![https://img-blog.csdn.net/2018052417250269?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjY1MTM3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image015.png)

图：P2P链路数据库更新过程

RouterA先与RouterB建立邻居关系。

建立邻居关系之后，RouterA与RouterB会先发送CSNP给对端设备。如果对端的LSDB与CSNP没有同步，则发送PSNP请求索取相应的LSP。

如上图所示假定RouterB向RouterA索取相应的LSP。RouterA发送RouterB请求的LSP的同时启动LSP重传定时器，并等待RouterB发送的PSNP作为收到LSP的确认。

如果在接口LSP重传定时器超时后，RouterA还没有收到RouterB发送的PSNP报文作为应答，则重新发送该LSP直至收到PSNP报文。

在P2P链路上PSNP有两种作用：

作为Ack应答以确认收到的LSP。

用来请求所需的LSP。

在P2P链路中设备的LSDB更新过程如下：

- 若收到的LSP比本地的序列号更小，则直接给对方发送本地的LSP，然后等待对方给自己一个PSNP报文作为确认；若收到的LSP比本地的序列号更大，则将这个新的LSP存入自己的LSDB，再通过一个PSNP报文来确认收到此LSP，最后再将这个新LSP发送给除了发送该LSP的邻居以外的邻居。
- 若收到的LSP序列号和本地相同，则比较RemainingLifetime，若收到的LSP报文的Remaining Lifetime为0，则将收到的LSP存入LSDB中并发送PSNP报文来确认收到此LSP，然后将该LSP发送给除了发送该LSP的邻居以外的邻居；若收到的LSP报文的Remaining Lifetime不为0而本地LSP报文的RemainingLifetime为0，则直接给对方发送本地的LSP，然后等待对方给自己一个PSNP报文作为确认。
- 若收到的LSP和本地LSP的序列号相同且Remaining Lifetime都不为0，则比较Checksum，若收到LSP的Checksum大于本地LSP的Checksum，则将收到的LSP存入LSDB中并发送PSNP报文来确认收到此LSP，然后将该LSP发送给除了发送该LSP的邻居以外的邻居；若收到LSP的Checksum小于本地LSP的Checksum，则直接给对方发送本地的LSP，然后等待对方给自己一个PSNP报文作为确认。
- 若收到的LSP和本地LSP的序列号、Remaining Lifetime和Checksum都相同，则不转发该报文。

## ISIS路由渗透

Level-1区域必须只能与骨干区域相连，不同level-1区域之间不能直接相连。level-1区域内的路由信息会通过level-1-2路由器通报给level-2区域，即level-1-2路由器会将学习到的level-1路由信息封装进level-2 LSP，并将这些level-2 LSP传递给其他level-2和level-1-2路由器。因此，level-1-2和level-2路由器是指定整个IS-IS路由域的路由信息的。

另一方面，为了减小路由表的规模，在缺省情况下，level-1-2和level-2路由器并不会将自己知道的路由域中其他level-1区域以及骨干区域的路由信息通报给level-1区域。这样一来，level-1路由器只能通过缺省路由来访问区域以外的任何目的地

通常情况下，level-1路由器只能通过缺省路由来访问本区域以外的目的地，但是如果需求特殊，则这种方式或许不能被接收，例如，如果要求一个level-1路由器必须经由最优路径（也就是总的开销值最小）访问其他某个区域的目的地时，使用缺省路由就无法满足需求。在这种情况下，level-1路由器需要知道并使用其他区域的目的地明细路由，而不是盲目地使用缺省路由。

IS-IS 路由渗透指的是 Level 1-2 和 Level-2 路由将自己知道的其他 Level-1 区域以及 Level-2 区域的路由信息通报给指定的 Level-1 区域的过程。

![https://img-blog.csdnimg.cn/20200409210757544.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NTc4NTQ1,size_16,color_FFFFFF,t_70](file:///C:/Users/zhf/AppData/Local/Temp/msohtmlclip1/01/clip_image017.png)