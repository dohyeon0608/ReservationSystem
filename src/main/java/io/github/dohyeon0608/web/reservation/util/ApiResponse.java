package io.github.dohyeon0608.web.reservation.util;

import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Schema(description = "공통 응답 규격")
public class ApiResponse<T> {
    private boolean succeed;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> create(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> create(String message, T data) {
        return create(200, message, data);
    }

    public static <T> ApiResponse<T> create(T data) {
        return create(200, "성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<String> createError(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getStatus().value(), errorCode.getMessage(), errorCode.getCode());
    }

    public ResponseEntity<ApiResponse<T>> toResponseEntity(){
        return ResponseEntity
                .status(HttpStatusCode.valueOf(code))
                .body(this);
    }

}
