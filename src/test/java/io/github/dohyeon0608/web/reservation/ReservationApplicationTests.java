package io.github.dohyeon0608.web.reservation;

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

		Long placeId = placeService.createPlace(
				PlaceType.STUDY_ROOM,
				OperationStatus.OPENED,
				"의혈스터디룸",
				1
		);

		Place place = placeService.getPlaceById(placeId);

		Assertions.assertAll(
				() -> timeslotService.createTimeslot(
						place,
						Date.valueOf(localDateTime.toLocalDate()),
						Time.valueOf(localDateTime.toLocalTime()),
						Time.valueOf(localDateTime.toLocalTime().plusHours(2)),
						1,
						SlotStatus.OPENED
				));

		Assertions.assertThrowsExactly(BusinessException.class,
				() -> timeslotService.createTimeslot(
						place,
						Date.valueOf(localDateTime.toLocalDate()),
						Time.valueOf(localDateTime.toLocalTime().plusHours(1)),
						Time.valueOf(localDateTime.toLocalTime().plusHours(2)),
						1,
						SlotStatus.OPENED
				));

	}


}
