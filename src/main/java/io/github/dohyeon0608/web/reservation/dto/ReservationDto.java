package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import lombok.Builder;

@Builder
public class ReservationDto {
    private User user;

    private Timeslot timeslot;

    private ReservationStatus reservationStatus;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .user(reservation.getUser())
                .timeslot(reservation.getTimeslot())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
