package io.github.dohyeon0608.web.reservation.controller;

import io.github.dohyeon0608.web.reservation.dto.request.ReservationRequestDto;
import io.github.dohyeon0608.web.reservation.dto.response.ReservationDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Reservation;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.ReservationService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
import io.github.dohyeon0608.web.reservation.service.UserService;
import io.github.dohyeon0608.web.reservation.util.ApiErrorCodes;
import io.github.dohyeon0608.web.reservation.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reservation API", description = "예약 API 그룹")
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final PlaceService placeService;
    private final UserService userService;
    private final TimeslotService timeslotService;

    @Operation(summary = "새 예약 생성", description = "새로운 예약을 생성합니다.")
    @PostMapping("/create")
    @ApiErrorCodes({ ErrorCode.RESERVATION_DUPLICATED, ErrorCode.RESERVATION_CANNOT_ON_CLOSED_SLOT, ErrorCode.RESERVATION_ALREADY_FULL })
    public ResponseEntity<ApiResponse<Long>> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ReservationRequestDto dto) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Long id = reservationService.createReservation(user.getId(), dto);

        return ApiResponse.create(id).toResponseEntity();
    }

    @Operation(summary = "예약 취소", description = "예약을 취소합니다.")
    @DeleteMapping("/cancel")
    @ApiErrorCodes({ ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_CANCELLED_BY_ANOTHER })
    public ResponseEntity<ApiResponse<Boolean>> cancel(@AuthenticationPrincipal UserDetails userDetails, Long timeslotId) {
        Timeslot timeslot = timeslotService.getTimeslotById(timeslotId);
        User user = userService.getUserByEmail(userDetails.getUsername());

        reservationService.cancel(user, timeslot);

        return ApiResponse.create(true).toResponseEntity();
    }

    @Operation(summary = "예약 조회", description = "내 에약을 모두 확인합니다.")
    @GetMapping("/viewMyReservaiton")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> viewMyReservations(@AuthenticationPrincipal UserDetails userDetails, @ParameterObject Pageable pageable) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        List<Reservation> reservationList = reservationService.viewReservationByUser(user, pageable);
        List<ReservationDto> dtoList = reservationList.stream()
                .map(ReservationDto::from)
                .toList();

        return ApiResponse.create(dtoList).toResponseEntity();
    }

    @Operation(summary = "상태별 예약 조회", description = "내 에약을 상태별로 모두 확인합니다.")
    @GetMapping("/viewMyReservaiton/{status}")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> viewMyReservationsByStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable ReservationStatus status, @ParameterObject Pageable pageable) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        List<Reservation> reservationList = reservationService.viewReservationByUserAndStatus(user, status, pageable);
        List<ReservationDto> dtoList = reservationList.stream()
                .map(ReservationDto::from)
                .toList();

        return ApiResponse.create(dtoList).toResponseEntity();
    }

    @Operation(summary = "장소별 예약 조회 (관리자 전용)", description = "특정 장소의 예약을 확인합니다.")
    @GetMapping("/admin/viewReservationsByPlace")
    @ApiErrorCodes({ ErrorCode.PLACE_NOT_FOUND })
    public ResponseEntity<ApiResponse<List<ReservationDto>>> viewReservationsByPlace(LocalDate date, Long placeId) {
        Place place = placeService.getPlaceById(placeId);
        List<Reservation> reservationList = reservationService.viewReservationByDateAndPlace(date, place);
        List<ReservationDto> dtoList = reservationList.stream()
                .map(ReservationDto::from)
                .toList();

        return ApiResponse.create(dtoList).toResponseEntity();
    }

}
