package io.github.dohyeon0608.web.reservation;

import io.github.dohyeon0608.web.reservation.dto.request.PlaceRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.ReservationRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.TimeslotRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.UserRegistrationDto;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.ReservationService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
import io.github.dohyeon0608.web.reservation.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class ReservationApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private TimeslotService timeslotService;

	@Autowired
	private ReservationService reservationService;

	@Test
	void contextLoads() {
	}

	@Test
	public void 타임슬롯_시간_겹침방지() {
		LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 20, 13, 0, 0);

		PlaceRequestDto placeDto = PlaceRequestDto.builder()
				.placeType(PlaceType.STUDY_ROOM)
				.operationStatus(OperationStatus.OPENED)
				.name("의혈스터디룸")
				.build();

		Long placeId = placeService.createPlace(placeDto);

		TimeslotRequestDto timeslot1 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime()))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(2)))
				.maxCapacity(1)
				.build();

		TimeslotRequestDto timeslot2 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime().plusHours(1)))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(3)))
				.maxCapacity(1)
				.build();

		TimeslotRequestDto timeslot3 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime().plusHours(2)))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(4)))
				.maxCapacity(1)
				.build();


		Assertions.assertAll(
				() -> timeslotService.createTimeslot(timeslot1));

		Assertions.assertThrowsExactly(BusinessException.class,
				() -> timeslotService.createTimeslot(timeslot2));

		Assertions.assertAll(
				() -> timeslotService.createTimeslot(timeslot3));

	}

	@Test
	public void 중복_예약_방지_테스트() {
		LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 20, 13, 0, 0);

		UserRegistrationDto registerationDto1 = UserRegistrationDto.builder()
				.email("pooang@cau.ac.kr")
				.nickname("pooang")
				.password("pooang")
				.build();

		UserRegistrationDto registerationDto2 = UserRegistrationDto.builder()
				.email("causw@cau.ac.kr")
				.nickname("causw")
				.password("causw")
				.build();

		Long userId1 = userService.createUser(registerationDto1);

		Long userId2 = userService.createUser(registerationDto2);

		PlaceRequestDto placeDto = PlaceRequestDto.builder()
				.placeType(PlaceType.STUDY_ROOM)
				.operationStatus(OperationStatus.OPENED)
				.name("의혈스터디룸")
				.build();

		Long placeId = placeService.createPlace(placeDto);

        System.out.println("Length is " + timeslotService.getAll().size());

		TimeslotRequestDto timeslot1 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime()))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(2)))
				.maxCapacity(1)
				.build();

		Long timeslotId = timeslotService.createTimeslot(timeslot1);

        ReservationRequestDto reservationDto1 = ReservationRequestDto.builder()
				.userId(userId1)
				.timeslotId(timeslotId)
				.build();

		ReservationRequestDto reservationDto2 = ReservationRequestDto.builder()
				.userId(userId2)
				.timeslotId(timeslotId)
				.build();


		Assertions.assertAll(() -> reservationService.createReservation(reservationDto1));
		Assertions.assertThrowsExactly(BusinessException.class, () -> reservationService.createReservation(reservationDto2));

		TimeslotRequestDto timeslotDto2 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime().plusHours(7)))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(8)))
				.maxCapacity(2)
				.build();

		Long timeslotId2 = timeslotService.createTimeslot(timeslotDto2);

		ReservationRequestDto reservationDto3 = ReservationRequestDto.builder()
				.userId(userId1)
				.timeslotId(timeslotId2)
				.build();

		ReservationRequestDto reservationDto4 = ReservationRequestDto.builder()
				.userId(userId2)
				.timeslotId(timeslotId2)
				.build();

		Assertions.assertAll(() -> {
			reservationService.createReservation(reservationDto3);
			reservationService.createReservation(reservationDto4);
		});


	}

}
