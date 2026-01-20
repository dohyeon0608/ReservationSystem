package io.github.dohyeon0608.web.reservation.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    protected ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
