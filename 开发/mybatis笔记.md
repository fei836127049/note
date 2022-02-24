

# mybatis笔记

## 1、配置

使用maven来构建项目，需要再pom.xml文件里导入依赖：

```xml
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.0</version>
        </dependency>
```

使用mybatis第一步：获取SQLSessionFactory对象(固定的xml文件，可以直接移植到别的地方)

```java
package com.zhf.Utils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhf
 * @date 2022/1/5
 */
//由SqlSessionFactory构建SQLSession；
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try{
//           使用mybatis第一步：获取SQLSessionFactory对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
//    获取SQLSession实例；SqlSession 提供了在数据库执行 SQL 命令所需的所有方法
    public static SqlSession getSqlSession(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

}

```

## 2、CRUD

### select

UserMapper

```java
package com.zhf.Dao;

import com.zhf.pojo.Users;

import java.util.List;

/**
 * @author zhf
 * @date 2022/1/6
 */
public interface UserMapper {
    //获取全部用户；
    List<Users> getUserList();
    //根据id获取用户；
    Users getUserListById(int id);
}
```

UserMapper.xml

```xml
<!--对呀接口com.zhf.Dao.UserMapper-->
<mapper namespace="com.zhf.Dao.UserMapper">
<!--    获取全部用户-->
    <select id="getUserList" resultType="com.zhf.pojo.Users">
        select * from Users;
    </select>
<!--    根据id获取用户-->
    <select id="getUserListById" resultType="com.zhf.pojo.Users" parameterType="int">
        select * from Users where id = #{id};
    </select>
    
</mapper>
```

- resultType:返回值的类型；
- parameterType：输入值的类型；



### insert

UserMapper

```java
package com.zhf.Dao;

import com.zhf.pojo.Users;

import java.util.List;

/**
 * @author zhf
 * @date 2022/1/6
 */
public interface UserMapper {
    //插入一条用户数据；
    int addUser(Users users);
}
```

UserMapper.xml

```xml
<!--对呀接口com.zhf.Dao.UserMapper-->
<mapper namespace="com.zhf.Dao.UserMapper">
<!--    插入一条用户数据-->
    <insert id="addUser" parameterType="com.zhf.pojo.Users">
        insert into Users(id,name,pwd) value (#{id},#{name},#{pwd})
    </insert>

</mapper>
```



### update

UserMapper

```java
    //修改用户；
    int editUser(User user);
```

UserMapper.xml

```xml
    <!--    修改用户数据-->
    <update id="editUser" parameterType="com.pojo.User">
        update user set name=#{name},pwd=#{pwd} where id=#{id};
    </update>
```

**在对数据表进行增删改等操作时，必须要提交事务；**



### Delect

```java
    //删除用户：通过用户ID删除；
    int deleteUser(int id);
```

```xml
    <!--    删除用户-->
    <delete id="deleteUser">
        delete from user where id=#{id};
    </delete>
```



### map

如果在实体类或者数据库中的表，字段或者参数过多，我们应当考虑使用map！

map传递参数直接在sql中取出key即可；

对象传递参数，直接在sql中取对象的属性即可；

UserMapper

```java
    //使用map继续添加用户
    int addUserByMap(Map<String,Object> map);

    //使用map修改用户；
    int editUserByMap(Map<String,Object> map);
```

UserMapper.xml

```xml
    <!--    通过map添加用户-->
    <insert id="addUserByMap" parameterType="map">
        insert into user (id,name,pwd) value (#{id},#{name},#{pwd});
    </insert>
    <!--    通过map修改用户数据-->
    <update id="editUserByMap" parameterType="map">
        update user set name=#{name},pwd=#{pwd} where id=#{id};
    </update>
```

test.java

```java
   @Test
    public void addUserByMap() {
        SqlSession sqlSession = Utils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("id",3);
        stringObjectHashMap.put("name","zhangsan");
        stringObjectHashMap.put("pwd","123456");
        int addUserByMap = mapper.addUserByMap(stringObjectHashMap);
        //增删改必须要提交事务；
        sqlSession.commit();
        sqlSession.close();

    }
    @Test
    public void editUserByMap() {
        SqlSession sqlSession = Utils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("id",3);
        stringObjectHashMap.put("name","李四");
        int editUserByMap = mapper.editUserByMap(stringObjectHashMap);

        //增删改必须要提交事务；
        sqlSession.commit();
        sqlSession.close();

    }
```



### 模糊查询

1、在代码执行时，传递通配符% %；

```java
List<User> userList = mapper.getUserLike("%李%");
```

2、在sql拼接中使用通配符防止sql注入；

```xml
select * from user where name like "%"#{value}"%";
```





## 3、配置解析

### 1、核心配置文件

```
mybatis-config.xml
```

从xml中构建SQLSessionFactory

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  <mappers>
    <mapper resource="org/mybatis/example/BlogMapper.xml"/>
  </mappers>
</configuration>
```



### 2、环境配置

MyBatis 可以配置成适应多种环境

**尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

- 默认使用的环境 ID（比如：default="development"）。
- 每个 environment 元素定义的环境 ID（比如：id="development"）。
- 事务管理器的配置（比如：type="JDBC"）。
- 数据源的配置（比如：type="POOLED"）

**事务管理器（transactionManager）**：

**Spring+Mybatis不需要配置事务管理器，因为 Spring 模块会使用自带的管理器来覆盖前面的配置。**



在mybatis中有两种类型的事务管理器

- **JDBC**：这个配置直接使用了 JDBC 的提交和回滚设施，它依赖从数据源获得的连接来管理事务作用域。

  ```xml
  <transactionManager type="JDBC"/>
  ```

  ​

- **MANAGED** ：这个配置几乎没做什么。它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。 默认情况下它会关闭连接。然而一些容器并不希望连接被关闭，因此需要将 closeConnection 属性设置为 false 来阻止默认的关闭行为。

  ```xml
  <transactionManager type="MANAGED">
    <property name="closeConnection" value="false"/>
  </transactionManager>
  ```

**数据源（DataSource）**：大多数情况下使用实例配置，但是**如果要启用延迟加载特性，就必须配置数据源**；

有三种内建的数据源类型：

- **UNPOOLED**：这个数据源的实现会每次请求时打开和关闭连接。

- **POOLED**– 这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。 这种处理方式很流行，能使并发 Web 应用快速响应请求。

- **JNDI** – 这个数据源实现是为了能在如 EJB 或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置一个 JNDI 上下文的数据源引用。这种数据源配置只需要两个属性：`initial_context、data_source`

  ```xml
    <dataSource type="POOLED">
      <property name="driver" value="${driver}"/>
      <property name="url" value="${url}"/>
      <property name="username" value="${username}"/>
      <property name="password" value="${password}"/>
    </dataSource>
  ```

### 3、属性

`dp.properties`

```xml
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&characterEncoding=utf8&autoReconnect=true
username=root
password=123456
```

在mybatis-config.xml中进行配置

```xml
//在同一目录下只用添加文件名dp.properties
<properties resource="org/mybatis/example/config.properties">
  <property name="username" value="dev_user"/> //也可在此添加数据；
  <property name="password" value="F2Fa3!33TYyg"/>
</properties>
```





### 4、映射器（mappers）

方式1：【推荐使用】

```xml
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>
```

方式二：**目前已经完全不用了！！！！！**

```xml
<!-- 使用完全限定资源定位符（URL） -->
<!--目前已经完全不用了！！！！！-->
<mappers>
  <mapper url="file:///var/mappers/AuthorMapper.xml"/>
  <mapper url="file:///var/mappers/BlogMapper.xml"/>
  <mapper url="file:///var/mappers/PostMapper.xml"/>
</mappers>
```

方式3：

**注意点：**

- **1、使用class注册绑定注册时，接口和他的mapper配置文件必须同名；**
- **2、使用class注册绑定注册时，接口和他的mapper配置文件必须在同一个package下；**

```xml
方式3：
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
```



方式4：

**注意点：**

- **1、使用class注册绑定注册时，接口和他的mapper配置文件必须同名；**
- **2、使用class注册绑定注册时，接口和他的mapper配置文件必须在同一个package下；**

```xml
方式4：
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```

### 5、作用域（Scope）和生命周期

作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SQLSessionFactoryBuilder**：

- 一旦创建了SQLSessionFactory就不需要了；
- 一般为局部变量；

**SQLSessionFactory：**

- 可以理解为数据库连接池；
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
- SqlSessionFactory 的最佳作用域是应用作用域。
- 最简单的就是使用**单例模式**或者**静态单例模式**（保证全局只有一个变量）。

**SQLSession**

- **连接**到连接池的一个请求；
- SqlSession 的实例**不是线程安全**的，因此是不能被共享的，所以它的最佳的作用域是**请求或方法作用域**。
- 用完之后需要关闭，否则会造成资源被占用；

### 6、类型别名（typeAliases）

类型别名可为 Java 类型设置一个缩写名字。 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

```xml
<typeAliases>
  <typeAlias alias="Author" type="domain.blog.Author"/>
  <typeAlias alias="Blog" type="domain.blog.Blog"/>
  <typeAlias alias="Comment" type="domain.blog.Comment"/>
  <typeAlias alias="Post" type="domain.blog.Post"/>
  <typeAlias alias="Section" type="domain.blog.Section"/>
  <typeAlias alias="Tag" type="domain.blog.Tag"/>
</typeAliases>
```

也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean；

扫描实体类的包，默认别名就是类名，首字母一般规定为小写；

```xml
<typeAliases>
  <package name="domain.blog"/>
</typeAliases>
```

实体类较少的时候使用第一个，实体类较多的时候一般选第二种，第一种可以自定义别名，第二种正常情况下不可以起别名，但是可以在实体类里面使用注解来进行起别名；

```java
@Alias("author")
public class Author {
    ...
}
```



## 4、解决属性名与字段名不一致的问题

### 1、ResultMap结果集映射:(重难点)

使用结果集映射可以让数据库中的字段和实体类中的属性不是用一个名字;

`resultMap` 元素是 MyBatis 中最重要最强大的元素。它可以让你从 90% 的 JDBC `ResultSets` 数据提取代码中解放出来，并在一些情形下允许你进行一些 JDBC 不支持的操作。

```xml
<!--    结果集映射-->
    <resultMap id="UsersMapper" type="Users">
<!--        column为数据库中的字段，property为实体类中的属性-->
<!--        使用结果集映射可以让数据库中的字段和实体类中的属性不是用一个名字-->
      <!--        只用数据库与实体类不一样的字段即可-->
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="pwd" property="pwd"/>
    </resultMap>
```

**ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。**

## 5、日志

### 1、日志工厂

如果一个数据库操作出现了异常，日志是一种很好的排错手段；

现在使用日志工厂实现，之前用sout、debug；

**名字必须跟官网一致**

- SLF4J
- LOG4J 【掌握】
- LOG4J2
- JDK_LOGGING
- COMMONS_LOGGING
- STDOUT_LOGGING【掌握】
- NO_LOGGING

在mybatis中具体使用哪一种日志实现，在设置（setting）中设定！

日志工厂中没有默认值，均需要自己配；

![img](file:///C:/Users/Administrator/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)



```xml
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
```

### 2、Log4J

- **产生日志信息**
- **将日志信息输出到指定位置**
- **调整输出样式**


- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/%E6%8E%A7%E5%88%B6%E5%8F%B0/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件等；
- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程；
- 这些可以通过一个[配置文件](https://baike.baidu.com/item/%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6/286550)来灵活地进行配置，而不需要修改应用的代码；

先导入Log4Jjar包；

```xml
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

再log4j.properties配置文件：

```properties
#将等级为debug的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/zhf.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-mm-dd}][%c]%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

最后配置log4j为日志的setting实现：

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```


在使用log4j时，导入的包应该是：

```java
import org.apache.log4j.Logger;
```

日志对象，参数为当前类的class：

```java
static Logger logger = Logger.getLogger(test.class);
```

日志级别：

```log
[INFO][22-41-11][com.zhf.Dao.test]info
[DEBUG][22-41-11][com.zhf.Dao.test]debug
[ERROR][22-41-11][com.zhf.Dao.test]error
...有很多日志级别
```

## 6、分页

目的：

- 减少数据的处理量；

### 使用limit实现分页

```mysql
select * from student limit n; #[0,n]
```

### 使用mybatis实现分页

1、接口

```java
    //分页查询；
    List<Users> getUserListByLimit(Map<String,Integer> map);
```

2、mapper.xml

```xml
<!--    使用了分页查询-->
<!--    使用了map传参-->
<select id="getUserListByLimit"  parameterType="map" resultMap="UsersMapper">
    select * from Users  limit #{startPage},#{Pagesize};
</select>
```

3、测试

```java
@Test
public void getUserListByLimit(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    HashMap<String, Integer> Map = new HashMap<String, Integer>();
    Map.put("startPage",1);
    Map.put("Pagesize",2);
    List<Users> userList = mapper.getUserListByLimit(Map);
    for (Users user : userList
    ) {
        System.out.println(user);
    }
    sqlSession.close();
}
```

### 使用RowBounds实现分页

1、接口

```java
//分页查询2；
List<Users> getUserListByRowBounds(Map<String,Integer> map);
```

2、mapper.xml

```xml
<!--    获取全部用户-->
<!--    使用了resultMap后，这里也要使用使用了resultMap后，而不是resultType-->
<!--    使用RowBounds实现分页-->
<select id="getUserListByRowBounds" resultMap="UsersMapper">
    select * from Users;
</select>
```

3、测试

```java
    @Test
    public void getUserListByRowBounds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        RowBounds实现分页；
        RowBounds rowBounds = new RowBounds(0, 2);

//        通过java代码层面实现分页；
        List<Users> users = sqlSession.selectList("com.zhf.Dao.UserMapper.getUserListByRowBounds",null,rowBounds);
        for (Users user : users
        ) {
            System.out.println(user);
        }

        sqlSession.close();
    }
```
## 7、使用注解开发

### 1、面向接口编程

选择面向接口开发的原因：**解耦**，**可拓展**，**提高复用**，上层不需要管具体的实现，大家遵守共同的开发标准，使得开发变得容易，规范性更好；

**附加知识点：类加载器、反射机制**

[]: E:\zhf\笔记\反射机制.md

**接口：**

- 接口从更深层次的理解，应该是定义与实现的分离；
- 接口本身反映了系统设计人员对于系统的理解；
- 接口应该有两类：
  - 一、对一个个体的抽象，他可以对应一个抽象体；
  - 二、对一个个体某一方面的抽象，即形成一个抽象面；


- 一个个体可能有多个抽象面，抽象体和抽象面有区别；



### 2、使用注解开发

1、注解在接口上实现

```java
@select("@select * from User")
List<User> getUsers();
```

2、需要在核心配置文件中绑定接口

```xml
    <mappers>
        <mapper class="com.zhf.Dao.UserMapper"/>
    </mappers>
```

3、测试





本质：反射机制实现

底层：动态代理



mybatis详细执行流程：



3、使用注解进行CRUD

查询：

```java
//使用注解进行crud
@Select("select * from user")
List<Users> getUsers();

//方法存在多个参数，所有的参数前面必须加上@Param("")注解
@Select("select * from user where id = #{id}")
Users getUserById(@Param("id") int id);
```



增删改都类似；



## 8、Lombok

- java库
- 插件
- 构建工具

使用步骤：



可使用的注解：****重点**

```java
//放在类上生成所有的get set方法，放在属性上只生成该属性的get set方法
**@Getter and @Setter
@FieldNameConstants
**@ToString
**@EqualsAndHashCode
**@AllArgsConstructor, @RequiredArgsConstructor and **@NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
**@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
Lombok config system
Code inspections
Refactoring actions (lombok and delombok)
```

@Data：生成无参构造函数、get，set、tostring、hashcode、equals

##  9、多对一

- 多个学生对应一个老师
- 对于学生而言，**关联**，多个学生关联一个老师（**多对一**）
- 对于老师而言，**集合**，一个老师有多个学生（**一对多**）



```sql
###teacher表的ID与students的tid关联###
CREATE TABLE `teacher`(
	`id` int(10) NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO teacher(`id`,`name`)VALUES (1,'王老师');


CREATE TABLE `students`(
		`id` int(10) NOT NULL,
		`name` VARCHAR(30) DEFAULT NULL,
		`tid` INT(10) DEFAULT NULL,
		PRIMARY KEY (`id`),
		KEY `fktid` (`tid`),
		FOREIGN KEY (`tid`) REFERENCES `teacher`(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8 

INSERT INTO `students`(`id`,`name`,`tid`)VALUES (1,'明','1');
INSERT INTO `students`(`id`,`name`,`tid`)VALUES (2,'红','1');
INSERT INTO `students`(`id`,`name`,`tid`)VALUES (3,'张','1');
INSERT INTO `students`(`id`,`name`,`tid`)VALUES (4,'李','1');
INSERT INTO `students`(`id`,`name`,`tid`)VALUES (5,'王','1');
```

### 搭建测试环境：

1. 导入lombok
2. 新建实体类
3. 新建mapper接口
4. 新建Mapper.xml文件
5. 在核心配置文件绑定注册mapper接口或文件（多种方式）
6. 测试



### 两个对象分别为

Students:

```java 
@Data
public class Students {
    private int id;
    private String name;
    //学生需要关联一个老师
    private Teacher teacher;

}
```

Teacher:

```java 
@Data
public class Teacher {
    private int id;
    private String name;
}

```



### 按照嵌套查询处理

```xml 
    <!--
    连表查询思路：
        1、查询所有的学生信息
        2、根据查询出来的学生的tid，寻找对应的老师
     -->
    <select id="getStudent" resultMap="StudentTeacher">
        select * from students
    </select>
    <resultMap id="StudentTeacher" type="Students">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <!--        负责的属性需要单独处理 对象：association 集合：collection-->
        <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
    </resultMap>

    <select id="getTeacher" resultType="Teacher">
        select * from teacher where id=#{id}
    </select>
```

### 按照结果嵌套处理



```sql
    <!--    按照结果嵌套处理-->
    <select id="getStudent2" resultMap="StudentTeacher2">
        select s.tid sid,s.name sname,t.name tname
        from students s,teacher t
        where s.tid = t.id
    </select>
    <resultMap id="StudentTeacher2" type="Students">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <association property="teacher" javaType="Teacher">
            <result property="name" column="tname"/>
        </association>
    </resultMap>
```

## 10、一对多

比如一个老师拥有多个学生，对于老师而言就是一对多



### 两个对象分别为

Students:

```java 
@Data
public class Students {
    private int id;
    private String name;
    private int tid;

}
```

Teacher:

```java 
@Data
public class Teacher {
    private int id;
    private String name;
  	//teacher拥有的学生为一个集合
  	//一个老师拥有多个学生
    private List<Students> students;
}

```



### 按结果嵌套处理

```xml
<!--    按照结果嵌套查询-->
    <select id="getTeacherAndStudent" resultMap="TeacherStudent">
        select s.id sid,s.name sname,t.name tname,t.id tid
        from students s ,teacher t
        where s.tid = t.id and t.id=#{tid}
    </select>
    <resultMap id="TeacherStudent" type="Teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
        <!--        负责的属性需要单独处理 对象：association 集合：collection
        javaType:指定属性的类型
        集合中泛型的信息使用oftype获取
        -->
      <!--        一个老师的students属性是一个集合-->
        <collection property="students" ofType="Students">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="tid"/>
        </collection>
    </resultMap>
```



### 按照嵌套查询处理

```xml
<!--    子查询-->
    <select id="getTeacherAndStudent2" resultMap="TeacherStudent2">
        select *
        from teacher where id=#{tid};
    </select>
    <resultMap id="TeacherStudent2" type="Teacher">
        <collection property="students" javaType="ArrayList" ofType="Students" select="getStudentByTeacherID" column="id"/>
    </resultMap>
    
    <select id="getStudentByTeacherID" resultType="Students">
        select * from students where tid=#{tid};
    </select>
```

### 小结

1、关联-association（多对一）

2、集合-collection（一对多）

3、Javatype & ofType

- javaType:用来指定实体类中属性的类型
- ofType：用来指定映射到List或者容器中的pojo类型，泛型中的约束类型

注意：

- 保证SQL的可读性，保证通俗易懂
- 注意一对多和多对一的中属性名和字段的问题
- 如遇到问题不好排查可以使用日志，建议log4j

### 面试高频：

- MySQL引擎
- InnoDB底层原理
- 索引
- 索引优化



## 11、动态SQL

**动态SQL：指的是根据不同的条件生成不同的SQL语句**



使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。

如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

### 环境:建表

```sql 
CREATE TABLE `blog` (
	`id` INT ( 50 ) NOT NULL COMMENT '博客ID',
	`title` VARCHAR ( 100 ) NOT NULL COMMENT '博客标题',
	`author` VARCHAR ( 30 ) NOT NULL COMMENT '博客作者',
	`create_time` datetime NOT NULL COMMENT '创建时间',
`view` INT ( 50 ) NOT NULL COMMENT '浏览量' 
) ENGINE = INNODB DEFAULT CHARSET = utf8
```

### **IF**:Mapper的写法

```xml
    <select id="queryBlogIF" parameterType="map" resultType="Blog">
        select * from blog where 1=1
        <if test="title != null">
            and title=#{title}
        </if>
        <if test="author != null">
            and author=#{author}
        </if>
    </select>
```

### **choose (when, otherwise)**

我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

```xml
    <select id="queryBlogChose" parameterType="map" resultType="Blog">
        select * from blog
        <where>
            <choose>
                <when test="title != null">
                    title=#{title}
                </when>
                <when test="author != null">
                    and author=#{author}
                </when>
                <otherwise>
                    and views=#{views}
                </otherwise>
            </choose>
        </where>
    </select>
<!-- 选择其中一个条件执行，优先选择最前面的条件-->
```



### **trim (where, set)**

where：*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

### **set语句**

*set* 元素会动态地在行首插入 SET 关键字，并会删掉额外的逗号（这些逗号是在使用条件语句给列赋值时引入的）。

```xml
    <update id="updateBlog" parameterType="map">
        update  blog
        <set>
            <if test="title != null">
                title=#{title},
            </if>
            <if test="author != null">
                author=#{author},
            </if>
        </set>
            where id=#{id}
    </update>
```



**动态SQL还是SQL语句，只是我们可以在SQL层面去执行一个逻辑代码；**



### SQL片段



### foreach

