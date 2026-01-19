package io.github.dohyeon0608.web.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    protected ErrorCode errorCode;

    public BusinessException(String message) {
        super(message);
    }
}
