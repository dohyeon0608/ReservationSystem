package io.github.dohyeon0608.web.reservation.util;

import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean isSucceed;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> create(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static ApiResponse<String> createError(ErrorCode errorCode) {
        return new ApiResponse<>(true, errorCode.getStatus().value(), errorCode.getMessage(), errorCode.getCode());
    }

    public ResponseEntity<ApiResponse<T>> toResponseEntity(){
        return ResponseEntity
                .status(HttpStatusCode.valueOf(code))
                .body(this);
    }

}
