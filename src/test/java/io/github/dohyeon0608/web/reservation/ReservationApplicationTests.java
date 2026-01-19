package io.github.dohyeon0608.web.reservation;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
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
	public void 타임슬롯_시간() {
		LocalDate localDate = LocalDate.of(2025, 1, 1);
		LocalTime specificTime;
		specificTime = LocalTime.of(12, 0, 0);
		Time startTime = Time.valueOf(specificTime);

		specificTime = LocalTime.of(15, 0, 0);
		Time endTime = Time.valueOf(specificTime);

		Place place = placeService.getPlaceById(3L);

		Assertions.assertThrowsExactly(BusinessException.class,
				() -> timeslotService.createTimeslot(
						place,
						Date.valueOf(localDate),
						startTime,
						endTime,
						1,
						SlotStatus.OPENED
				));

//		Assertions.assertAll(
//				() -> timeslotService.createTimeslot(
//						place,
//						Date.valueOf(localDate),
//						endTime,
//						startTime,
//						1,
//						SlotStatus.OPENED
//				));
	}

}
