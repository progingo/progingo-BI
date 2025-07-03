package org.progingo.progingobi.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.progingo.progingobi.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public JsonResult exceptionHandler(Exception e) {
        e.printStackTrace();
        return JsonResult.fail("服务器开小差咯~");
    }

    @ExceptionHandler(TokenException.class)
    public JsonResult tokenExceptionHandler(TokenException e) {
        e.printStackTrace();
        return JsonResult.fail(e.getMessage());
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public JsonResult UnauthenticatedExceptionHandler(UnauthenticatedException e) {
        System.out.println("未携带token");
        e.printStackTrace();
        return JsonResult.ok(222,"用户未登录","");
    }
    @ExceptionHandler(UnauthorizedException.class)
    public JsonResult ClientExceptionHandler(UnauthorizedException e) {
        System.out.println("权限不足");
        e.printStackTrace();
        return JsonResult.ok(223,"权限不足","");
    }

    @ExceptionHandler(UserFormatException.class)
    public JsonResult UserFormatExceptionHandler(UserFormatException e) {
        System.out.println(e);
        e.printStackTrace();
        return JsonResult.fail(e.getMessage());
    }

}
