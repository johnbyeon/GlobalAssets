package com.fourMan.GlobalAssets.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 요청 파라미터 문제 → 400
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(Exception ex) {
        return Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", ex.getMessage()
        );
    }

    // 그 외 모든 예외 → 500 (JSON으로 일관되게)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> internalError(Exception ex) {
        return Map.of(
                "status", 500,
                "error", "Internal Server Error",
                "message", ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }
}
