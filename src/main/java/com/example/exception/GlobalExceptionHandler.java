package com.example.exception;

import com.example.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 所有Controller抛出的异常这里都能收到，并通过ExceptionHandler处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 这里只能返回String -- 值为逻辑视图名（全局错误视图） 但这种方式前后分离的设计中不会用了
     * 通过ResponseBody返回DTO对象,前段看到的是200，看返回体中的JSON数据内容，里面有错误码，信息
     * 不返回也可以
     */
    @ExceptionHandler(TestException.class)
    @ResponseBody
    public ErrorDto handleTestException(TestException e){
        log.warn("test exception", e);
        return new ErrorDto(10086, "this is a test");
    }
}
