package io.github.dohyeon0608.web.reservation.dto.request;

import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequestDto {
    private PlaceType placeType;

    @Builder.Default
    private OperationStatus operationStatus = OperationStatus.OPENED;

    private String name;

    @Builder.Default
    private Integer maxCapacity = 1;
}
