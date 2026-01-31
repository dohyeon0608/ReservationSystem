package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.request.PlaceRequestDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    private void validation(Place place) {
        if(place.getMaxCapacity() <= 0) {
            throw new BusinessException(ErrorCode.PLACE_NONPOSITIVE_CAPACITY);
        }
    }

    public Long createPlace(PlaceRequestDto dto) {
        Place place = Place.builder()
                .placeType(dto.getPlaceType())
                .operationStatus(dto.getOperationStatus())
                .name(dto.getName())
                .maxCapacity(dto.getMaxCapacity())
                .build();

        validation(place);

        return placeRepository.save(place).getId();
    }

    public List<Place> getPlacesByStatus(OperationStatus status, Pageable pageable) {
        return placeRepository
                .findPlaceByOperationStatus(status, pageable);
    }

    public Place getPlaceById(Long id) throws BusinessException {
        return placeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));
    }



}
