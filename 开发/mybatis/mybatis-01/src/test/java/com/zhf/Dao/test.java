package com.zhf.Dao;

import com.zhf.Utils.MybatisUtils;
import com.zhf.pojo.Users;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author zhf
 * @date 2022/1/6
 */
public class test {
    static Logger logger = Logger.getLogger(test.class);

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
    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Users users = new Users(5,"王五","123");
        mapper.addUser(users);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void editUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Users users = new Users(5,"王qi","123456");
        mapper.editUser(users);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void log4jtest(){
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
    }

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
}
