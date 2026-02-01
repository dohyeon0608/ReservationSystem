package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user, Pageable pageable);

    List<Reservation> findAllByUserAndReservationStatus(User user, ReservationStatus reservationStatus, Pageable pageable);

    boolean existsByUserAndTimeslotAndReservationStatus(User user, Timeslot timeslot, ReservationStatus status);

    @Query("SELECT r FROM Reservation r JOIN r.timeslot t " +
            "WHERE t.reservationDate = :date AND t.place = :place")
    List<Reservation> findAllByDateAndPlace(@Param("date") LocalDate date, @Param("place") Place place);
}
