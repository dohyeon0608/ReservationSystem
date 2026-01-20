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

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeslotService timeslotService;

    private void validation(Reservation reservation) {
        if(reservation.getTimeslot().getSlotStatus() == SlotStatus.CLOSED) {
            throw new BusinessException(ErrorCode.RESERVATION_CANNOT_ON_CLOSED_SLOT);
        }
    }

    // TODO: 다대일 매핑 LazyInitializationException 고치기
    public Long createReservation(ReservationDto dto) {
        Reservation reservation = Reservation.builder()
                .user(dto.getUser())
                .timeslot(dto.getTimeslot())
                .reservationStatus(dto.getReservationStatus())
                .build();

        reservation.changeTimeslot(dto.getTimeslot());

        validation(reservation);

        return reservationRepository.save(reservation).getId();
    }

    public List<Reservation> viewReservationByUser(User user, Pageable pageable) {
        return reservationRepository.findByUser(user, pageable);
    }

    public List<Reservation> viewReservationByUserAndStatus(User user, ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByUserAndReservationStatus(user, status, pageable);
    }

    public List<Reservation> viewReservationByDate(Date date) {
        List<Timeslot> timeslots = timeslotService.getTimeslotByDate(date);
        return timeslots.stream()
                .map(reservationRepository::findByTimeslot)
                .toList();
    }

    public void cancel(User user) {

    }


}
