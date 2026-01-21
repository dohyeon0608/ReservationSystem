package io.github.dohyeon0608.web.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_DUPLICATED_EMAIL("USER-001", "중복된 이메일이 존재합니다.", HttpStatus.CONFLICT),
    USER_UNAUTHORIZED("USER-002", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER-003", "알 수 없는 사용자입니다.", HttpStatus.NOT_FOUND),
    PLACE_NOT_FOUND("PLACE-001", "존재하지 않는 장소입니다.", HttpStatus.NOT_FOUND),
    PLACE_NEGATIVE_CAPACITY("PLACE-002", "최대 인원은 음수가 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    TIMESLOT_DUPLICATED_TIME("TIMESLT-001", "해당 시간에 이미 슬롯이 존재합니다.", HttpStatus.CONFLICT),
    TIMESLOT_INVALID_TIME("TIMESLT-002", "유효하지 않은 시간대입니다.", HttpStatus.BAD_REQUEST),
    TIMESLOT_NOT_FOUND("TIMESLT-003", "존재하지 않는 슬롯입니다.", HttpStatus.NOT_FOUND),
    TIMESLOT_NEGATIVE_CAPACITY("TIMESLT-003", "최대 인원은 음수가 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    RESERVATION_DUPLICATED("RSV-001", "중복된 예약입니다.", HttpStatus.CONFLICT),
    RESERVATION_CANCELLED_BY_ANOTHER("RSV-002", "다른 사용자의 예약은 취소할 수 없습니다.", HttpStatus.UNAUTHORIZED),
    RESERVATION_CANNOT_ON_CLOSED_SLOT("RSV-003", "닫힌 슬롯에 대해 예약할 수 없습니다.", HttpStatus.BAD_REQUEST),
    RESERVATION_ALREADY_FULL("RSV-004", "예약이 모두 찼습니다.", HttpStatus.CONFLICT),
    RESERVATION_NOT_FOUND("RSV-005", "알 수 없는 예약입니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
