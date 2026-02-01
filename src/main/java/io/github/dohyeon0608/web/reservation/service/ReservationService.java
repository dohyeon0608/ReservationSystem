package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.request.ReservationRequestDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeslotService timeslotService;
    private final UserService userService;

    private void validation(Reservation reservation) {
        if(reservation.getTimeslot().getSlotStatus() == SlotStatus.CLOSED) {
            throw new BusinessException(ErrorCode.RESERVATION_CANNOT_ON_CLOSED_SLOT);
        }
    }

    @Transactional
    public Long createReservation(ReservationRequestDto dto) {
        User user = userService.getUserById(dto.getUserId());

        Timeslot timeslot = timeslotService.getTimeslotById(dto.getTimeslotId());

        Reservation reservation = Reservation.builder()
                .user(user)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .build();

        reservation.changeTimeslot(timeslot);

        validation(reservation);

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
    public List<Reservation> viewReservationByDateAndPlace(LocalDate date, Place place) {
        List<Timeslot> timeslotList = timeslotService.getTimeslotByDateAndPlace(date, place);

        return timeslotList.stream()
                .map(reservationRepository::findByTimeslot)
                .flatMap(Optional::stream)
                .toList();
    }

    @Transactional
    public List<Reservation> viewReservationByDate(LocalDate date) {
        List<Timeslot> timeslotList = timeslotService.getTimeslotByDate(date);
        return timeslotList.stream()
                .map(reservationRepository::findByTimeslot)
                .flatMap(Optional::stream)
                .toList();
    }

    @Transactional
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Transactional
    public void cancel(User user, Reservation reservation) {
        if(!Objects.equals(reservation.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.RESERVATION_CANCELLED_BY_ANOTHER);
        }

        reservation.cancel();
    }


}
