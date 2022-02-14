# mybatis核心工厂

26种设计模式

# 1、Builder模式

1.1、builder属于创建类模式，如果一个对象的构建比较复杂，超出了构造函数所能包含的范围，就可以使用工厂模式和Builder模式，相对于工厂模式会产出一个完整的产品，Builder应用于更加复杂的对象的构建，甚至只会构建产品的一个部分。

1.2、在mybatis初始化的过程中，SqlSessionFactoryBuilder会调用**XMLConfigBuilder**来读取所有的MybatisMapConfig.xml和所有的*mapper.xml文件来构建mybatis运行的核心对象configuration对象，然后将该configuration对象作为参数构建一个SqlSessionFactory对象；

1.3、XMLConfigBuilder在构建configuration对象时，也会调用**XMLMapperBuilder**用于读取*mapper文件，XMLMapperBuilder会使用XMLStatementBuilder来读取和build所有的sql语句；

比如SQLSessionFactoryBuilder：根据不同的输入参数来构建SQLSessionFactory这个工厂对象。

![img](https://img-blog.csdnimg.cn/img_convert/a57619783c9054090489dabafe9506fb.png)

