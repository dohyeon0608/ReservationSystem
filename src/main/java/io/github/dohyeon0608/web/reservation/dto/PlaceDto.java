package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDto {
    private PlaceType placeType;

    @Builder.Default
    private OperationStatus operationStatus = OperationStatus.OPENED;

    private String name;

    @Builder.Default
    private Integer maxCapacity = 1;

    public static PlaceDto from(Place place) {
        return PlaceDto.builder()
                .placeType(place.getPlaceType())
                .operationStatus(place.getOperationStatus())
                .name(place.getName())
                .maxCapacity(place.getMaxCapacity())
                .build();
    }

}
