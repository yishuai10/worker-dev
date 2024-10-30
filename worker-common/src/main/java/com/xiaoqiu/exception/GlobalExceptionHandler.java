package com.xiaoqiu.exception;

import com.xiaoqiu.resp.R;
import com.xiaoqiu.resp.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * @author xiaoqiu
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理算数异常
     */
//    @ExceptionHandler(value = ArithmeticException.class)
//    public R<String> bizArithmeticExceptionHandler(ArithmeticException e) {
//        log.error("发生算数异常！ msg: -> ", e);
//        return R.failed(e.getMessage());
//    }

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = XiaoQiuException.class)
    public R<String> bizExceptionHandler(XiaoQiuException e) {
        ResponseStatusEnum statusEnum = e.getResponseStatusEnum();
        log.error("发生业务异常！ msg: -> ", e);
        return R.failed(statusEnum.status(), statusEnum.msg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Map<String, String>> exceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数校验异常！ msg: -> ", e);
        return R.failed(convertBindException(e));
    }

    private Map<String, String> convertBindException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorMap;
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public R<String> exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！ msg: -> ", e);
        return R.failed("发生空指针异常!");
    }

    /**
     * 服务器异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(Exception e) {
        log.error("服务器异常！ msg: -> ", e);
        return R.failed("服务器异常!");
    }
}
