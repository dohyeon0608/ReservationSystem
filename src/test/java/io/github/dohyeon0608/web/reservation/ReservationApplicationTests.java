package io.github.dohyeon0608.web.reservation;

import io.github.dohyeon0608.web.reservation.dto.PlaceDto;
import io.github.dohyeon0608.web.reservation.dto.TimeslotDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.repository.PlaceRepository;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
class ReservationApplicationTests {

	@Autowired
	private PlaceService placeService;

	@Autowired
	private TimeslotService timeslotService;

	@Test
	void contextLoads() {
	}

	@Test
	public void 타임슬롯_시간_겹침방지() {
		LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 20, 13, 0, 0);

		PlaceDto placeDto = PlaceDto.builder()
				.placeType(PlaceType.STUDY_ROOM)
				.operationStatus(OperationStatus.OPENED)
				.name("의혈스터디룸")
				.build();

		Long placeId = placeService.createPlace(placeDto);

		Place place = placeService.getPlaceById(placeId);

		TimeslotDto timeslot1 = TimeslotDto.builder()
				.place(place)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime()))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(2)))
				.maxCapacity(1)
				.slotStatus(SlotStatus.OPENED)
				.build();

		TimeslotDto timeslot2 = TimeslotDto.builder()
				.place(place)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime().plusHours(1)))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(3)))
				.maxCapacity(1)
				.slotStatus(SlotStatus.OPENED)
				.build();

		TimeslotDto timeslot3 = TimeslotDto.builder()
				.place(place)
				.reservationDate(Date.valueOf(localDateTime.toLocalDate()))
				.startTime(Time.valueOf(localDateTime.toLocalTime().plusHours(2)))
				.endTime(Time.valueOf(localDateTime.toLocalTime().plusHours(4)))
				.maxCapacity(1)
				.slotStatus(SlotStatus.OPENED)
				.build();


		Assertions.assertAll(
				() -> timeslotService.createTimeslot(timeslot1));

		Assertions.assertThrowsExactly(BusinessException.class,
				() -> timeslotService.createTimeslot(timeslot2));

		Assertions.assertAll(
				() -> timeslotService.createTimeslot(timeslot3));

	}


}
