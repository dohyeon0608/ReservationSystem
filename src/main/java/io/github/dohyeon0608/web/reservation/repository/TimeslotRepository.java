package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    Optional<Timeslot> findTimeslotsById(Long id);

    List<Timeslot> findAllTimeslotsByReservationDateAndPlace(LocalDate reservationDate, Place place);

    @Lock(LockModeType.OPTIMISTIC)
    List<Timeslot> findAllTimeslotsByPlaceAndSlotStatus(Place place, SlotStatus slotStatus, Pageable pageable);

}
