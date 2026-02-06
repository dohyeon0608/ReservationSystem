package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.request.ReservationRequestDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeslotService timeslotService;
    private final UserService userService;

    private void validation(User user, Timeslot timeslot) {
        if(reservationRepository.existsByUserAndTimeslotAndReservationStatus(user, timeslot, ReservationStatus.CONFIRMED)) {
            throw new BusinessException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    @Transactional
    public Long createReservation(Long userId, ReservationRequestDto dto) {
        User user = userService.getUserById(userId);
        Timeslot timeslot = timeslotService.getTimeslotById(dto.getTimeslotId());

        validation(user, timeslot);

        Reservation reservation = Reservation.builder()
                .user(user)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .build();

        reservation.changeTimeslot(timeslot);

        return reservationRepository.save(reservation).getId();
    }

    @Transactional
    public List<Reservation> viewReservationByUser(User user, Pageable pageable) {
        return reservationRepository.findAllByUser(user, pageable);
    }

    @Transactional
    public List<Reservation> viewReservationByUserAndStatus(User user, ReservationStatus status, Pageable pageable) {
        return reservationRepository.findAllByUserAndReservationStatus(user, status, pageable);
    }

    @Transactional
    public List<Reservation> viewReservationByDateAndPlace(LocalDate date, Place place) {
        return reservationRepository.findAllByDateAndPlace(date, place);
    }

    @Transactional
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Transactional
    public Reservation getReservationByTimeslotAndUser(Timeslot timeslot, User user) {
        return reservationRepository.findByTimeslotAndReservationStatusAndUser(timeslot, ReservationStatus.CONFIRMED, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Transactional
    public void cancel(User user, Timeslot timeslot) {
        Reservation reservation = this.getReservationByTimeslotAndUser(timeslot, user);

        if(!Objects.equals(reservation.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.RESERVATION_CANCELLED_BY_ANOTHER);
        }

        reservation.cancel();
    }


}
