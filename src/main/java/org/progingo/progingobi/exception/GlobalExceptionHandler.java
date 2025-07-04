package org.progingo.progingobi.exception;
import lombok.extern.slf4j.Slf4j;
import org.progingo.progingobi.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *

 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public JsonResult businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return JsonResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public JsonResult runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return JsonResult.fail(ErrorCode.SYSTEM_ERROR.getCode(), "服务器开小差");
    }
}
