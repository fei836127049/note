最近在Linux学习过程中需要用到Cmake，前段时间也初略接触过Cmake，于是决定系统的学习一下Cmake。

## Cmake

**优点：**

- 开源代码，使用类BSD许可发布。
- 跨平台，并可以生成native编译配置文件，在linux/Unix平台，生成makefile,在苹果平台可以生成Xcode,在windows平台，可以生成MSVC的工程文件。
- 能够管理大型项目。
- 简化编译构建过程和编译过程。cmake的工具链：cmake+make。
- 高效率，因为cmake在工具链中没有libtool。
- 可扩展，可以为cmake编写特定功能的模块，扩展cmake功能。

**缺点：**

- cmake只是看起来比较简单，使用并不简单；
- 每个项目使用一个CMakeLists.txt（每个目录一个），使用的是cmake语法。
- cmake跟已有体系配合不是特别的理想，比如pkgconfig。

一个简单的例子：

程序结构：

```
├── include
	├──hello.h
├── src
	├──hello.c
	├──main.c
├──CMakeLists.txt
├──make.sh
```

CmakeLists.txt中的内容：

```cmake
#设置CMake最小版本
cmake_minimum_required(VERSION 3.5) 
#用变量PRJ指代hello_cmake
SET(PRJ hello_cmake)
#设置工程名为上述变量指代的hello_cmake
project (${PRJ}) 
#设置当前变量SRC_DIR为当前CmakeList文件夹下的src路径
SET(SRC_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src")
#设置当前变量INCLUDE_DIR为当前CmakeList文件夹下的include路径
SET(INCLUDE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/include")
#寻找上述变量SRC_DIR路径下所有.c文件
FILE(GLOB SRC_LIST ${SRC_DIR}/*.c)
#寻找上述变量INCLUDE_DIR路径下所有.h文件
FILE(GLOB INCLUDE_LIST ${SRC_DIR}/*.h)
#将上述所有的文件和操作用SOURCE_EXE表示
SET(SOURCE_EXE ${SRC_LIST} ${INCLUDE_LIST})
#编译时将前面定义的头文件目录包含进来
INCLUDE_DIRECTORIES(${INCLUDE_DIR})
#添加可执行文件
ADD_EXECUTABLE(${PRJ} ${SOURCE_EXE})
#将生成的可执行文件定位保存在该CmakeList文件所在目录平级的code-out文件夹中
#CMAKE_CURRENT_SOURCE_DIR：表示当前CmakeList所在的目录
SET(EXECUTABLE_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/code-out)
```

在进行编译前，一般需要在工程目录下新建一个build文件夹来存放Cmake产生的中间件，这里可以采用脚本来实现。

```sh
###make.sh
mkdir build
cd build/
cmake ..
make
```

这里将新建文件夹，运行Cmake和make编译的命令同时放在脚本中，第一次编译时，只需要运行脚本文件即可。后续修改代码需要编译可进build文件夹中使用make指令进行编译。

用到的一些CMake命令：

- PROJECT（name）：设置project的名字为name；
- SET（）：用于设置变量；
- ADD_EXECUTABLE（）：添加可执行文件
- FILE（）：文件操作命令；
  - FILE(GLOB SRC_LIST ${SRC_DIR}/*.c)：将搜索到的所有文件列表保存在变量中；
- INCLUDE_DIRECTORIES（）：将指定目录添加到编译器的头文件搜索路径之下，指定的目录被解释成当前源码路径的相对路径。
- ADD_SUBDIRECTORY（）：添加子目录,并可以指定中间二进制和目标二进制存放的位置。

一些CMake中预定义的变量：

- PROJECT_SOURCE_DIR：工程根目录；
- PROJECT_BINARY_DIR：运行cmake命令的目录。
- CMAKE_INCLUDE_PATH：环境变量，非cmake变量；
- CMAKE_LIBRARY_PATH：环境变量；
- **CMAKE_CURRENT_SOURCE_DIR**：当前处理的CMakeLists.txt文件所在路径；
- 使用ADD_SURDIRECTORY指令可以更改该变量的值；
- SET(EXECUTABLE_OUTPUT_PATH < dir >) 指令不会对该变量有影响，但改变了最终目标文件的存储路径；
- CMAKE_CURRENT_LIST_FILE：输出调用该变量的CMakeLists.txt的完整路径；
- CMAKE_CURRENT_LIST_LINE：输出该变量所在的行；
- CMAKE_MODULE_PATH：定义自己的cmake模块所在路径；
- **EXECUTABLE_OUTPUT_PATH**：重新定义目标二进制可执行文件的存放位置；
- LIBRARY_OUTPUT_PATH：重新定义目标链接库文件的存放位置；
- PROJECT_NAME：返回由PROJECT指令定义的项目名称；

当前对于CMake的使用的了解处于入门阶段，能满足日常的自动化编译，更复杂的一些操作就需要查询Cmake文档了；