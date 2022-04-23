package com.syl.servicebase.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuLiException extends RuntimeException {

    private Integer Code; //状态码

    private String msg; //异常信息
}
