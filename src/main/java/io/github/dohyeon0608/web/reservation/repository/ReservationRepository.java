package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
