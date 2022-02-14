package com.Pojo;

import lombok.Data;

/**
 * @auther harfe
 * @date 2022/2/12 15:22
 */

@Data
public class Students {
    private int id;
    private String name;
    //学生需要关联一个老师
    private Teacher teacher;

}
