package com.syl.servicebase.exception;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.syl.commonutils.R;
import com.syl.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.SocketTimeoutException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //出现什么异常执行这个方法
    @ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public R error(MySQLIntegrityConstraintViolationException e) {
        e.printStackTrace();
        System.out.println("<===============名字重复异常===============>");
        return R.error().message("名字重复，添加失败");
    }

    //出现什么异常执行这个方法
    @ExceptionHandler(value = SocketTimeoutException.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("正在加载,请稍后...");
    }

    //自定义异常
    @ExceptionHandler(value = GuLiException.class)
    @ResponseBody
    public R error(GuLiException e) {
        log.info("执行了自定义异常");
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
