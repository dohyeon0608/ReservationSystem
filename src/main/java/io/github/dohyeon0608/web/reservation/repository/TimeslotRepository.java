package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    List<Timeslot> findTimeslotBySlotStatus(SlotStatus slotStatus);

    List<Timeslot> findTimeslotsByReservationDateAndPlace(Date reservationDate, Place place);

    List<Timeslot> findTimeslotsByReservationDate(Date reservationDate);
}
