package com.example.exception;

import com.example.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 这里只能返回String -- 值为逻辑视图名（全局错误视图） 但这种方式前后分离的设计中不会用了
     * 返回Long -- 没搞懂能返回什么值
     * 返回其他类型的值都是错的
     * 不返回可以
     */
    @ExceptionHandler(TestException.class)
    @ResponseBody
    public ErrorDto handleTestException(TestException e){
        log.warn("test exception", e);
        return new ErrorDto(10086, "this is a test");
    }
}
