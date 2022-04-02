package com.yang.serviceedu.entity.vo;


import lombok.Data;

import java.io.Serializable;

//课程查询条件封装
@Data
public class CourseQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String parentId;

    private String subjectId;

    private String teacherId;
}
