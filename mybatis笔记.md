# mybatis笔记

## 1、配置

使用maven来构建项目，需要再pom.xml文件里导入依赖：

```java
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>x.x.x</version>
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

```JAVA
    //删除用户：通过用户ID删除；
    int deleteUser(int id);
```

```XML
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



### 4、类型别名（typeAliases）

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

### 5、置（settings）

| cacheEnabled       | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。             | true \| false | true  |
| ------------------ | ---------------------------------------- | ------------- | ----- |
| lazyLoadingEnabled | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | true \| false | false |

| useColumnLabel   | 使用列标签代替列名。实际表现依赖于数据库驱动，具体可参考数据库驱动的相关文档，或通过对比测试来观察。 | true \| false | true  |
| ---------------- | ---------------------------------------- | ------------- | ----- |
| useGeneratedKeys | 允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）。 | true \| false | False |

| mapUnderscoreToCamelCase | 是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。 | true \| false                            | False |
| ------------------------ | ---------------------------------------- | ---------------------------------------- | ----- |
| logImpl                  | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。          | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置   |