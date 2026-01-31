package io.github.dohyeon0608.web.reservation.dto.request;

import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장소 요청 데이터")
public class PlaceRequestDto {

    @Schema(description = "장소 종류")
    private PlaceType placeType;

    @Builder.Default
    @Schema(description = "운영 상태")
    private OperationStatus operationStatus = OperationStatus.OPENED;

    @Schema(description = "장소 이름", example = "참슬기식당")
    private String name;

    @Builder.Default
    @Schema(description = "최대 수용 인원", example = "3", defaultValue = "1")
    private Integer maxCapacity = 1;
}
