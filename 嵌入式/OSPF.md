## 一、OSPF定义：基于链路状态的协议

OSPF（Open Shortest Path First）是IETF组织开发的一个基于链路状态的**内部网关协议**（Interior Gateway Protocol）。

- ipv4协议使用的是OSPF v2
- ipv6协议使用的是OSPF v3

几种IGP协议：

| 对比项  | RIP                                      | OSPF                                     | IS-IS                                    |
| ---- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| 协议类型 | IP层协议。                                   | IP层协议。                                   | 链路层协议。                                   |
| 适用范围 | 应用于规模较小的网络中。例如，校园网等结构简单的地区性网络。           | 应用于规模适中的网络中，最多可支持几百台路由器。例如，中小型企业网络。      | 应用于规模较大的网络中。例如，大型ISP（Internet Service Provider）中 |
| 路由算法 | 采用距离矢量（Distance-Vector）算法，通过UDP报文进行路由信息的交换。 | 采用最短路径SPF（Shortest Path First）算法。通过链路状态通告LSA（Link State Advertisement）描述网络拓扑，依据网络拓扑生成一棵最短路径树SPT（Shortest Path Tree），计算出到网络中所有目的地的最短路径，进行路由信息的交换。 | 采用最短路径SPF算法。依据网络拓扑生成一棵最短路径树SPT，计算出到网络中所有目的地的最短路径。在IS-IS中，SPF算法分别独立的在Level-1和Level-2数据库中运行。 |
| 收敛速度 | 收敛速度慢。                                   | 收敛速度快，小于1s。                              | 收敛速度快，小于1s。                              |
| 扩展性  | 不能扩展。                                    | 通过划分区域扩展网路支撑能力。                          | 通过Level路由器扩展网路支撑能力。                      |

**RIP协议**：由于RIP是**基于距离矢量算法**的路由协议，存在着**收敛慢**、**路由环路**、**可扩展性差**等问题，所以逐渐被OSPF取代。



**OSPF特性：**

- 适应范围广：应用于规模适中的网络，最多可支持几百台路由器。例如，中小型企业网络。
- 支持掩码：由于OSPF报文中携带掩码信息，所以OSPF协议不受自然掩码的限制，对可变长子网掩码VLSM（Variable Length Subnet Mask）提供很好的支持。
- 快速收敛：在网络的拓扑结构发生变化后立即发送更新报文，使这一变化在自治系统中同步。
- 无自环：由于OSPF根据收集到的链路状态用最短路径树算法计算路由，从算法本身保证了不会生成自环路由。
  - 区域划分：允许自治系统的网络被划分成区域来管理，区域间传送的路由信息被进一步抽象，从而减少了占用的网络带宽。
- 等价路由：支持到同一目的地址的多条等价路由。
- 路由分级：使用四类不同的路由，按优先顺序来说分别是：区域内路由、区域间路由、第一类外部路由、第二类外部路由。
- 支持验证：支持基于区域和接口的报文验证，以保证报文交互的安全性。
- 组播发送：在某些类型的链路上以组播地址发送协议报文，减少对其他设备的干扰。



## 二、OSPF基本概念

### 1、Router ID

一个路由器要想运行OSPF协议，必须存在Router ID。Router ID 是一台路由器在自制系统中唯一的标识。

- 手动配置Router ID
- 路由器自动获取Router ID：如果没有手动配置Router ID，路由器会从当前接口中自动获取一个作为Router ID
  - 优先从Loopback地址中选择最大的IP地址作为Router ID。
  - 如果没有配置Loopback接口，则在接口地址中选取最大的IP地址作为Router ID。

### 2、区域

- 普通区域：缺省情况下，OSPF区域被定义为普通区域。
  - 标准区域：最通用的区域，它传输区域内路由，区域间路由和外部路由。
  - 骨干区域：连接所有其他OSPF区域的中央区域，通常用**Area 0**表示。骨干区域负责区域之间的路由，非骨干区域之间的路由信息必须通过骨干区域来转发。
    - 骨干区域自身必须保持连通。
    - 所有非骨干区域必须与骨干区域保持连通。
- Stub区域
  - Stub区域是一些特定的区域，Stub区域的ABR不传播它们接收到的自治系统外部路由，因此这些区域中路由器的路由表规模以及路由信息传递的数量都会大大减少。
  - 一般情况下，Stub区域位于自治系统的边界，是只有一个ABR的非骨干区域，为保证到自治系统外的路由依旧可达，Stub区域的ABR将生成一条缺省路由，并发布给Stub区域中的其他非ABR路由器。
  - Totally Stub区域允许ABR发布的Type3缺省路由，不允许发布自治系统外部路由和区域间的路由，只允许发布区域内路由。
    - 骨干区域不能配置成Stub区域。
    - Stub区域内不能存在ASBR，因此自治系统外部的路由不能在本区域内传播。
    - 虚连接不能穿过Stub区域。
- NSSA（Not-So-Stubby Area）区域
  - **NSSA是Stub区域的一个变形，它和Stub区域有许多相似的地方。**
  - NSSA区域不允许存在[Type5 LSA](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_05)。
  - NSSA区域允许引入自治系统外部路由，携带这些外部路由信息的[Type7 LSA](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_06)由NSSA的ASBR产生，仅在本NSSA内传播。
  - Totally NSSA区域不允许发布自治系统外部路由和区域间的路由，只允许发布区域内路由。
  - ​
    - 该区域的ABR发布Type7 LSA缺省路由传播到区域内，所有域间路由都必须通过ABR才能发布。
    - 虚连接不能穿过NSSA区域。



### 3、路由器类型

![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630501.png)

| 路由器类型                                 | 含义                                       |
| ------------------------------------- | :--------------------------------------- |
| **区域内路由器**（Internal Router）           | 该类路由器的所有接口都属于同一个OSPF区域。                  |
| **区域边界路由器**ABR（Area Border Router）    | 该类路由器可以同时属于两个以上的区域，但其中一个必须是[骨干区域](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_01)。ABR用来连接骨干区域和非骨干区域，它与骨干区域之间既可以是物理连接，也可以是逻辑上的连接。 |
| **骨干路由器**（Backbone Router）            | 该类路由器至少有一个接口属于骨干区域。所有的ABR和位于骨干区域Area0内部的路由器都是骨干路由器。 |
| **自治系统边界路由器**ASBR（AS Boundary Router） | 与其他AS交换路由信息的路由器称为ASBR。ASBR并不一定位于AS的边界，它可能是区域内路由器，也可能是ABR。 |

### 4、LSA	

OSPF中对路由信息的描述都是封装在**链路状态通告LSA**（Link State Advertisement）中发布出去的。

| LSA类型                           | LSA作用                                    |
| ------------------------------- | ---------------------------------------- |
| Router-LSA（Type1）               | 每个路由器都会产生，描述了路由器的链路状态和开销，在发布路由器所属的区域内传播。 |
| Network-LSA（Type2）              | 由[DR](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_08)产生，描述本网段的链路状态，在DR所属的区域内传播。 |
| Network-summary-LSA（Type3）      | 由[ABR](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_04)产生，描述区域内某个网段的路由，并通告给发布或接收此LSA的非Totally Stub或NSSA区域。 |
| ASBR-summary-LSA（Type4）         | 由[ABR](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_04)产生，描述本区域到其他区域中的ASBR的路由，通告给除ASBR所在区域的其他区域。 |
| AS-external-LSA（Type5）          | 由[ASBR](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_07)产生，描述到AS外部的路由，通告到所有的区域（除了[Stub区域](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_02)和[NSSA区域](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_03)）。 |
| NSSA LSA（Type7）                 | 由[ASBR](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_07)产生，描述到AS外部的路由，仅在NSSA区域内传播。 |
| Opaque LSA（Type9/Type10/Type11） | Opaque LSA提供用于OSPF的扩展的通用机制。其中：Type9 LSA仅在接口所在网段范围内传播。用于支持GR的Grace LSA就是Type9 LSA的一种。Type10 LSA在区域内传播。用于支持TE的LSA就是Type10 LSA的一种。Type11 LSA在自治域内传播，目前还没有实际应用的例子。 |

### 5、报文类型

**OSPF用IP报文直接封装协议报文，协议号为89。OSPF分为5种报文：Hello报文、DD报文、LSR报文、LSU报文和LSAck报文。**

| 报文类型                                     | 报文作用                                     |
| ---------------------------------------- | ---------------------------------------- |
| Hello报文                                  | 周期性发送，用来发现和维持OSPF邻居关系。                   |
| DD报文（Database Description packet）        | 描述本地LSDB的摘要信息，用于两台路由器进行**数据库同步**。        |
| LSR报文（Link State Request packet）         | 用于向对方请求所需的LSA。路由器只有在OSPF邻居双方成功交换DD报文后才会向对方发出LSR报文。 |
| LSU报文（Link State Update packet）          | 用于向对方发送其所需要的LSA。                         |
| LSAck报文（Link State Acknowledgment packet） | 用来对收到的LSA进行确认。                           |



### 6、路由类型

​	

| 路由类型                    | 含义                                       |
| ----------------------- | ---------------------------------------- |
| Intra Area              | 区域内路由。                                   |
| Inter Area              | 区域间路由。                                   |
| 第一类外部路由（Type1 External） | 这类路由的可信程度高一些。到第一类外部路由的开销=本路由器到相应的ASBR的开销+ASBR到该路由目的地址的开销。存在多个ASBR时，每条路径的开销值分别按照“第一类外部路由的开销=本路由器到相应的ASBR的开销+ASBR到该路由目的地址的开销”计算，得到的开销值用于路由选路。 |
| 第二类外部路由（Type2 External） | 这类路由的可信度比较低，所以OSPF协议认为从ASBR到自治系统之外的开销远远大于在自治系统之内到达ASBR的开销。所以，OSPF计算路由开销时只考虑ASBR到自治系统之外的开销，即到第二类外部路由的开销=ASBR到该路由目的地址的开销。存在多个ASBR时，先比较引入路由的开销值，选取开销值最小的ASBR路径进行路由引入。如果引入路由的开销值相同，再比较本路由器到相应的ASBR的开销值，选取开销值最小的路径进行路由引入。无论选择哪条路径引入路由，第二类外部路由的开销都等于ASBR到该路由目的地址的开销。 |

### 7、OSPF支持的网络类型

| 网络类型                                     | 链路层协议                                    | 报文发送形式                                   | 图示                                       |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| 广播类型（Broadcast）                          | EthernetFDDIToken Ring                   | 通常以组播形式（224.0.0.5）发送[Hello报文](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_10)和[LSAck报文](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_14)。对于[LSU报文](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_13)，通常以组播形式首次发送，以单播形式进行重传。其中，224.0.0.5的组播地址为OSPF设备的预留IP组播地址，224.0.0.6的组播地址为OSPF DR的预留IP组播地址。以单播形式发送[DD报文](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_11)和[LSR报文](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__ph_12)。 | ![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630502.png) |
| NBMA类型（Non-broadcast multiple access）：非广播且多点可达的网络 | 帧中继ATMX.25                               | 在该类型的网络中，以单播形式发送协议报文（Hello报文、DD报文、LSR报文、LSU报文、LSAck报文）。 | ![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630503.png) |
| 点到多点P2MP类型（Point-to-Multipoint）          | 没有一种链路层协议会被缺省的认为是Point-to-Multipoint类型。点到多点必须是由其他的网络类型强制更改的。常用做法是将非全连通的NBMA改为点到多点的网络。 | 以组播形式（224.0.0.5）发送Hello报文。以单播形式发送其他协议报文（DD报文、LSR报文、LSU报文、LSAck报文）。 | ![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630504.png) |
| 点到点P2P类型（point-to-point）                 | PPPHDLCLAPB                              | 以组播形式（224.0.0.5）发送协议报文（Hello报文、DD报文、LSR报文、LSU报文、LSAck报文）。以组播形式重传LSU报文。 | ![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630505.png) |

### 8、DR和BDR

在广播网和NBMA网络中，任意两台路由器之间都要传递路由信息。如[图2](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/dc_vrp_feature_new_006305.html?ft=0&fe=10&hib=3.3.8.7.2.1&id=31187784_07_11681#dc_vrp_feature_new_006305__fig_dc_vrp_feature_new_00630506)所示，网络中有n台路由器，则需要建立n*(n-1)/2个邻接关系。这使得任何一台路由器的路由变化都会导致多次传递，浪费了带宽资源。

![img](http://localhost:7890/pages/31187784/07/31187784/07/resources/dc_8090_feature_zh/vrp/images/fig_dc_vrp_feature_new_00630506.png)

为了解决浪费带宽资源问题，提出了DR和BDR概念；

- DR（Designated Router 指定路由器）
  - 通过选举出DR，所有路由器只将信息发送给DR，然后DR以广播的形式将当前链路状态LSA广播出去。这样可以减少广播网和NBMA网络的中邻居建立的数量；
  - 除了DR和BDR之间的路由器将不再建立邻居；
- BDR（Backup Designated Router 备份指定路由器）
  - BDR是对DR的一个备份，在选举DR的同时也选举出BDR，BDR也和本网段内的所有路由器建立邻接关系并交换路由信息。
  - 当DR失效后，BDR将立即会成为DR，同时会重新选举出一个新的BDR。
- DR和BDR的选举
  - DR和BDR不是人为指定的，而是由本网段中所有的路由器共同选举出来的。
  - 路由器接口的DR优先级决定了该接口在选举DR、BDR时所具有的资格。
  - 本网段内DR优先级大于0的路由器都可作为“候选人”。选举中使用的“选票”就是Hello报文。每台路由器将自己选出的DR写入Hello报文中，发给网段上的其他路由器。
  - 当处于同一网段的两台路由器同时宣布自己是DR时，DR优先级高者胜出。
  - 如果优先级相等，则Router ID大者胜出。
  - 如果一台路由器的优先级为0，则它不会被选举为DR或BDR。

### 9、OSPF多进程

- OSPF支持多进程，在同一台路由器上可以运行多个不同的OSPF进程，它们之间互不影响，彼此独立。
- 不同OSPF进程之间的路由交互相当于不同路由协议之间的路由交互。
- 路由器的一个接口只能属于某一个OSPF进程。

### 10、OSPF缺省路由

缺省路由是指目的地址和掩码都是0的路由。当路由器无精确匹配的路由时，就可以通过缺省路由进行报文转发。





## 三、OSPF基本原理

OSPF协议路由的计算过程可简单描述如下：

1. 建立邻居关系，过程如下：

   1. 本端设备通过接口向外发送Hello报文与对端设备建立邻居关系。
   2. 两端设备进行主/从关系协商和DD报文交换。
   3. 两端设备通过更新LSA完成链路数据库LSDB的同步。

   此时，邻居关系建立成功。

2. OSPF采用SPF（Shortest Path First）算法计算路由，可以达到路由快速收敛的目的。

**OSPF邻居状态机**

OSPF网络中，**邻居设备**之间首先要建立**邻接关系**才能交换路由信息。

- 邻居关系
  - OSPF设备启动后，会通过OSPF接口向外发送Hello报文，收到Hello报文的OSPF设备会检查报文中所定义的参数，如果双方一致就会形成邻居关系，两端设备互为邻居。
- 邻接关系
  - 形成邻居关系后，如果两端设备成功交换**DD报文**和**LSA**，才建立邻接关系。

OSPF有8种邻居状态机

| 状态机      | 含义                                       |
| -------- | ---------------------------------------- |
| Down     | 邻居会话的初始阶段。表明没有在邻居失效时间间隔内收到来自邻居设备的Hello报文。 |
| Attempt  | 处于本状态时，定期向手工配置的邻居发送Hello报文。说明：Attempt状态只适用于NBMA类型的接口。 |
| Init     | 本状态表示已经收到了邻居的Hello报文，但是对端并没有收到本端发送的Hello报文。 |
| 2-way    | **互为邻居**。本状态表示双方互相收到了对端发送的Hello报文，建立了邻居关系。如果不形成邻接关系则邻居状态机就停留在此状态，否则进入Exstart状态。 |
| Exstart  | 协商主/从关系。建立主/从关系主要是为了保证在后续的DD报文交换中能够有序的发送。 |
| Exchange | 交换DD报文。本端设备将本地的LSDB用DD报文*来描述，并发给邻居设备。    |
| Loading  | 正在同步LSDB。两端设备发送LSR报文向邻居请求对方的LSA，同步LSDB。  |
| Full     | 建立邻接。两端设备的LSDB已同步，本端设备和邻居设备建立了**邻接关系**。  |



