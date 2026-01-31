package io.github.dohyeon0608.web.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 슬롯 요청 데이터")
public class TimeslotRequestDto {

    @Schema(description = "장소 ID", example = "1")
    private Long placeId;

    @Schema(description = "예약 날짜", example = "2026-01-30")
    private Date reservationDate;

    @Schema(description = "예약 시작 시각", example = "13:00:00")
    private Time startTime;

    @Schema(description = "예약 종료 시각", example = "15:00:00")
    private Time endTime;

    @Builder.Default
    @Schema(description = "최대 예약 인원", example = "3", defaultValue = "1")
    private Integer maxCapacity = 1;
}
