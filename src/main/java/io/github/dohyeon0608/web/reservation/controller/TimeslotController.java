package io.github.dohyeon0608.web.reservation.controller;

import io.github.dohyeon0608.web.reservation.dto.request.TimeslotRequestDto;
import io.github.dohyeon0608.web.reservation.dto.response.TimeslotDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
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
@Tag(name = "Timeslot API", description = "예약 슬롯 API 그룹")
@RequestMapping("/timeslot")
public class TimeslotController {

    private final PlaceService placeService;
    private final TimeslotService timeslotService;

    @Operation(summary = "새 예약 슬롯 생성 (관리자 전용)", description = "새로운 예약 슬롯을 생성합니다.")
    @PostMapping("/admin/create")
    public ResponseEntity<ApiResponse<Long>> createTimeslot(@RequestBody TimeslotRequestDto dto) {
        Long id = timeslotService.createTimeslot(dto);

        return ApiResponse.create(id).toResponseEntity();
    }

    @Operation(summary = "장소별 예약 가능한 슬롯 검색", description = "해당 장소에서 예약 가능한 슬롯을 검색합니다.")
    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<List<TimeslotDto>>> getAvailableTimeslotByPlace(@PathVariable Long placeId, @ParameterObject Pageable page) {
        Place place = placeService.getPlaceById(placeId);

        List<Timeslot> timeslotList = timeslotService.getTimeslotByPlaceAndSlotStatus(place, SlotStatus.OPENED, page);

        List<TimeslotDto> dtoList = timeslotList.stream()
                .map(TimeslotDto::from)
                .toList();

        return ApiResponse.create(dtoList).toResponseEntity();
    }

}
