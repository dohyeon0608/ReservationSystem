package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findPlaceByOperationStatus(OperationStatus operationStatus, Pageable pageable);

}
