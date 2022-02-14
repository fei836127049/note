package com.zhf.Dao;

import com.zhf.pojo.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    //插入一条用户数据；
    int addUser(Users users);
    //修改用户数据；
    int editUser(Users users);


    //使用注解进行crud
    @Select("select * from user")
    List<Users> getUsers();

    //方法存在多个参数，所有的参数前面必须加上@Param("")注解
    @Select("select * from user where id = #{id}")
    Users getUserById(@Param("id") int id);
}
