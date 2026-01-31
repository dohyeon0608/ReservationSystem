package io.github.dohyeon0608.web.reservation.dto.request;

import io.github.dohyeon0608.web.reservation.dto.response.TimeslotDto;
import io.github.dohyeon0608.web.reservation.dto.response.UserDto;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    private Long userId;

    private Long timeslotId;

    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.CONFIRMED;
}
