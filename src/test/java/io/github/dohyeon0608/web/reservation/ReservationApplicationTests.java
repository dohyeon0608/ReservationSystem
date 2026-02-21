package io.github.dohyeon0608.web.reservation;

import io.github.dohyeon0608.web.reservation.dto.request.PlaceRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.ReservationRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.TimeslotRequestDto;
import io.github.dohyeon0608.web.reservation.dto.request.UserRegistrationDto;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import io.github.dohyeon0608.web.reservation.entity.enums.UserRole;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.repository.PlaceRepository;
import io.github.dohyeon0608.web.reservation.repository.ReservationRepository;
import io.github.dohyeon0608.web.reservation.repository.TimeslotRepository;
import io.github.dohyeon0608.web.reservation.repository.UserRepository;
import io.github.dohyeon0608.web.reservation.service.PlaceService;
import io.github.dohyeon0608.web.reservation.service.ReservationService;
import io.github.dohyeon0608.web.reservation.service.TimeslotService;
import io.github.dohyeon0608.web.reservation.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ReservationApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private TimeslotService timeslotService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private TimeslotRepository timeslotRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	@DisplayName("타임슬롯 시간 겹침 방지")
	public void preventTimeslotTimeDuplicated() {
		LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 20, 13, 0, 0);

		PlaceRequestDto placeDto = PlaceRequestDto.builder()
				.placeType(PlaceType.STUDY_ROOM)
				.operationStatus(OperationStatus.OPENED)
				.name("의혈스터디룸")
				.build();

		Long placeId = placeService.createPlace(placeDto);

		TimeslotRequestDto timeslot1 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime())
				.endTime(localDateTime.toLocalTime().plusHours(2))
				.maxCapacity(1)
				.build();

		TimeslotRequestDto timeslot2 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime().plusHours(1))
				.endTime(localDateTime.toLocalTime().plusHours(3))
				.maxCapacity(1)
				.build();

		TimeslotRequestDto timeslot3 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime().plusHours(2))
				.endTime(localDateTime.toLocalTime().plusHours(4))
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
	@Transactional
	@DisplayName("중복 예약 방지")
	public void preventDuplicatedReservations() {
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
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime())
				.endTime(localDateTime.toLocalTime().plusHours(2))
				.maxCapacity(1)
				.build();

		Long timeslotId = timeslotService.createTimeslot(timeslot1);

        ReservationRequestDto reservationDto1 = ReservationRequestDto.builder()
				.timeslotId(timeslotId)
				.build();

		ReservationRequestDto reservationDto2 = ReservationRequestDto.builder()
				.timeslotId(timeslotId)
				.build();


		Assertions.assertAll(() -> reservationService.createReservation(userId1, reservationDto1));
		Assertions.assertThrowsExactly(BusinessException.class, () -> reservationService.createReservation(userId2, reservationDto2));

		TimeslotRequestDto timeslotDto2 = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime().plusHours(7))
				.endTime(localDateTime.toLocalTime().plusHours(8))
				.maxCapacity(2)
				.build();

		Long timeslotId2 = timeslotService.createTimeslot(timeslotDto2);

		ReservationRequestDto reservationDto3 = ReservationRequestDto.builder()
				.timeslotId(timeslotId2)
				.build();

		ReservationRequestDto reservationDto4 = ReservationRequestDto.builder()
				.timeslotId(timeslotId2)
				.build();

		Assertions.assertAll(() -> {
			reservationService.createReservation(userId1, reservationDto3);
			reservationService.createReservation(userId2, reservationDto4);
		});


	}

	@Test
	@DisplayName("예약 동시성 방지")
	public void testReservationSynchronicity() throws InterruptedException {
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// 데이터 세팅
		List<Long> userIdList = new ArrayList<>();

		for(int i = 0; i < threadCount; i++) {
			UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
					.email(String.format("user%d@example.com", i))
					.password("1234")
					.nickname(String.format("테스트 사용자 %d", i))
					.role(UserRole.USER)
					.build();

			userIdList.add(userService.createUser(userRegistrationDto));
		}

		PlaceRequestDto placeDto = PlaceRequestDto.builder()
				.placeType(PlaceType.STUDY_ROOM)
				.operationStatus(OperationStatus.OPENED)
				.name("의혈스터디룸")
				.build();

		Long placeId = placeService.createPlace(placeDto);

		LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 20, 13, 0, 0);

		TimeslotRequestDto timeslotRequestDto = TimeslotRequestDto.builder()
				.placeId(placeId)
				.reservationDate(localDateTime.toLocalDate())
				.startTime(localDateTime.toLocalTime())
				.endTime(localDateTime.toLocalTime().plusHours(2))
				.maxCapacity(1)
				.build();

		Long timeslotId = timeslotService.createTimeslot(timeslotRequestDto);

		ReservationRequestDto reservationDto = ReservationRequestDto.builder()
				.timeslotId(timeslotId)
				.build();

		List<Long> reservationIdList = new ArrayList<>();

		for (int i = 0; i < threadCount; i++) {
			Long userId = userIdList.get(i);
			executorService.execute(() -> {
				try {
					reservationIdList.add(reservationService.createReservation(userId, reservationDto));
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Timeslot timeslot = timeslotService.getTimeslotById(timeslotId);

		Assertions.assertSame(1, timeslot.getReservationCount());

		// 삭제
		for(Long reservationId : reservationIdList) {
			reservationRepository.delete(reservationService.getReservationById(reservationId));
		}

		timeslotRepository.delete(timeslot);

		placeRepository.delete(placeService.getPlaceById(placeId));

		for(Long userId : userIdList) {
			User user = userService.getUserById(userId);
			userRepository.delete(user);
		}
	}

}
