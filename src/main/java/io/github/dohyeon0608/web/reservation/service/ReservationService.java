package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    // TODO: 중복 검사
    public Long createReservation(User user, Timeslot timeslot) {
        Reservation reservation = Reservation.builder()
                .user(user)
                .timeslot(timeslot)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .build();

        return reservationRepository.save(reservation).getId();
    }


}
