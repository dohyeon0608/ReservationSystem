package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    // TODO: 유효성 검사 추가 예정
    public Long createPlace(PlaceType placeType, OperationStatus operationStatus, String name, Integer max_capacity) {
        Place place = Place.builder()
                .placeType(placeType)
                .operationStatus(operationStatus)
                .name(name)
                .max_capacity(max_capacity)
                .build();

        return placeRepository.save(place).getId();
    }

    public List<Place> getOpenedPlaces(int pages, int size) {
        return placeRepository.findPlaceByOperationStatus(OperationStatus.OPENED, PageRequest.of(pages, size));
    }

    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));
    }



}
