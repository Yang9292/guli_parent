package com.yang.serviceedu.entity.frontVo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端条件封装类：前台条件查询课程
 */
@Data
public class CourseFrontVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "讲师id")
    private String teacherId;

    @ApiModelProperty(value = "一级类别id")
    private String subjectParentId;

    @ApiModelProperty(value = "二级类别id")
    private String subjectId;

    @ApiModelProperty(value = "销量排序")
    private String buyCountSort;

    @ApiModelProperty(value = "最新时间排序")
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreateSort;

    @ApiModelProperty(value = "价格排序")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String priceSort;

}
