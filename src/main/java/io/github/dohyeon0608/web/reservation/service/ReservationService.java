package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.ReservationDto;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeslotService timeslotService;
    private final UserService userService;

    @Transactional
    public Long createReservation(ReservationDto dto) {
        User user = userService.getUserById(
                dto.getUser().getId()
        );

        Timeslot timeslot = timeslotService.getTimeslotById(
                dto.getTimeslot().getId()
        );

        Reservation reservation = Reservation.builder()
                .user(user)
                .reservationStatus(dto.getReservationStatus())
                .build();

        reservation.changeTimeslot(timeslot);

        return reservationRepository.save(reservation).getId();
    }

    @Transactional
    public List<Reservation> viewReservationByUser(User user, Pageable pageable) {
        return reservationRepository.findByUser(user, pageable);
    }

    @Transactional
    public List<Reservation> viewReservationByUserAndStatus(User user, ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByUserAndReservationStatus(user, status, pageable);
    }

    @Transactional
    public List<Reservation> viewReservationByDate(Date date) {
        List<Timeslot> timeslots = timeslotService.getTimeslotByDate(date);
        return timeslots.stream()
                .map(reservationRepository::findByTimeslot)
                .flatMap(Optional::stream)
                .toList();
    }

    public void cancel(User user) {

    }


}
