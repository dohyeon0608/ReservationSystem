package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import lombok.Builder;

@Builder
public class PlaceDto {
    private PlaceType placeType;

    private OperationStatus operationStatus;

    private String name;

    private Integer maxCapacity;

    public static PlaceDto from(Place place) {
        return PlaceDto.builder()
                .placeType(place.getPlaceType())
                .operationStatus(place.getOperationStatus())
                .name(place.getName())
                .maxCapacity(place.getMaxCapacity())
                .build();
    }

}
