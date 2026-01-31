package io.github.dohyeon0608.web.reservation.controller;

import io.github.dohyeon0608.web.reservation.dto.PlaceDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Place API", description = "장소 API 그룹")
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "예약 가능한 장소 조회", description = "현재 예약 가능한 장소를 조회합니다.")
    @GetMapping("/availablePlaces")
    public ResponseEntity<ApiResponse<List<Long>>> getAvailablePlaces(@ParameterObject Pageable page) {
        List<Place> placeList = placeService.getPlacesByStatus(
                OperationStatus.OPENED,
                page
        );

        List<Long> placeDtoList = placeList.stream()
                .map(Place::getId)
                .toList();

        return ApiResponse.create(placeDtoList).toResponseEntity();
    }

    @Operation(summary = "장소 상세 정보 조회", description = "특정 장소의 상세 정보를 조회합니다.")
    @GetMapping("/info/{id}")
    public ResponseEntity<ApiResponse<PlaceDto>> getDetailedPlaceInfo(@PathVariable Long id) {
        Place place = placeService.getPlaceById(id);
        PlaceDto placeDto = PlaceDto.from(place);

        return ApiResponse.create(placeDto).toResponseEntity();
    }

    @Operation(summary = "장소 추가 (관리자 전용)", description = "새로운 장소를 만듭니다.")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createPlace(@RequestBody PlaceDto dto) {
        Long id = placeService.createPlace(dto);

        return ApiResponse.create(id).toResponseEntity();
    }

}
