package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.request.TimeslotRequestDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TimeslotService {
    private final PlaceService placeService;

    private final TimeslotRepository timeslotRepository;

    private void validation(Timeslot timeslot) {
        if(timeslot.getMaxCapacity() < 0) {
            throw new BusinessException(ErrorCode.TIMESLOT_NEGATIVE_CAPACITY);
        }

        if(timeslot.getEndTime().isBefore(timeslot.getStartTime())) {
            throw new BusinessException(ErrorCode.TIMESLOT_INVALID_TIME);
        }

        List<Timeslot> timeslotList = timeslotRepository
                .findTimeslotsByReservationDateAndPlace(timeslot.getReservationDate(), timeslot.getPlace());

        LocalTime startTime = timeslot.getStartTime();
        LocalTime endTime = timeslot.getEndTime();

        for(Timeslot t : timeslotList) {
            if(Objects.equals(t.getId(), timeslot.getId())) continue;
            LocalTime st = t.getStartTime();
            LocalTime et = t.getEndTime();
            boolean isValid = !st.isBefore(endTime) || !et.isAfter(startTime);
            if(!isValid) throw new BusinessException(ErrorCode.TIMESLOT_DUPLICATED_TIME);
        }
    }

    public Long createTimeslot(TimeslotRequestDto dto) {
        Place place = placeService.getPlaceById(dto.getPlaceId());

        Timeslot timeslot = Timeslot.builder()
                .place(place)
                .reservationDate(dto.getReservationDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .maxCapacity(dto.getMaxCapacity())
                .slotStatus(SlotStatus.OPENED)
                .build();

        validation(timeslot);

        return timeslotRepository.save(timeslot).getId();
    }

    public List<Timeslot> getTimeslotByPlaceAndSlotStatus(Place place, SlotStatus status, Pageable pageable) {
        return timeslotRepository.findTimeslotsByPlaceAndSlotStatus(place, status, pageable);
    }

    public List<Timeslot> getTimeslotByDateAndPlace(LocalDate date, Place place) {
        return timeslotRepository.findTimeslotsByReservationDateAndPlace(date, place);
    }

    public List<Timeslot> getTimeslotByDate(LocalDate date) {
        return timeslotRepository.findTimeslotsByReservationDate(date);
    }

    public Timeslot getTimeslotById(Long id) {
        return timeslotRepository.findTimeslotsById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TIMESLOT_NOT_FOUND));
    }

    public List<Timeslot> getAll() {
        return timeslotRepository.findAll();
    }

}
