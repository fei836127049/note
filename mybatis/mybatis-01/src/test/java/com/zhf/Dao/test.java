package com.zhf.Dao;

import com.zhf.Utils.MybatisUtils;
import com.zhf.pojo.Users;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author zhf
 * @date 2022/1/6
 */
public class test {
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<Users> userList = mapper.getUserList();
        for (Users user : userList
             ) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    @Test
    public void test02(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Users userListById = mapper.getUserListById(4);
        System.out.println(userListById);
        sqlSession.close();
    }

}
