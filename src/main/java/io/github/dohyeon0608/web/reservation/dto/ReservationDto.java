package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationDto {
    private Long id;

    private UserDto user;

    private TimeslotDto timeslot;

    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.CONFIRMED;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .user(UserDto.from(reservation.getUser()))
                .timeslot(TimeslotDto.from(reservation.getTimeslot()))
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
