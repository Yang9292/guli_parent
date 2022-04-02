package com.yang.servicebase.exceptionhandler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuliException extends RuntimeException {

    private Integer code;//异常状态码
    private String msg;//异常信息
}
