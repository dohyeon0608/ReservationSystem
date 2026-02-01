package io.github.dohyeon0608.web.reservation.dto.request;

import io.github.dohyeon0608.web.reservation.dto.response.TimeslotDto;
import io.github.dohyeon0608.web.reservation.dto.response.UserDto;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 요청 데이터")
public class ReservationRequestDto {

    @Schema(description = "예약 슬롯 ID", example = "1")
    private Long timeslotId;

}
