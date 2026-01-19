package io.github.dohyeon0608.web.reservation.exception;

import io.github.dohyeon0608.web.reservation.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException exception) {
        return ApiResponse.createError(exception.errorCode).toResponseEntity();
    }
}
