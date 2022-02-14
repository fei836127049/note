package com.zhf.Dao;

import com.zhf.pojo.Users;

import java.util.List;
import java.util.Map;

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

    //分页查询；
    List<Users> getUserListByLimit(Map<String,Integer> map);

    //分页查询2；
    List<Users> getUserListByRowBounds(Map<String,Integer> map);
}
