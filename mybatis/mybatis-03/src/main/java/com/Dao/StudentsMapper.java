package com.Dao;

import com.Pojo.Students;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentsMapper {

    //查询所有学生信息，以及对应的老师的信息
    List<Students> getStudent();

    //按照结果嵌套处理
    List<Students> getStudent2();


}
