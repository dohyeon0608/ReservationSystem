package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.TimeslotDto;
import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
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

        if(timeslot.getEndTime().before(timeslot.getStartTime())) {
            throw new BusinessException(ErrorCode.TIMESLOT_INVALID_TIME);
        }

        List<Timeslot> timeslotList = timeslotRepository
                .findTimeslotsByReservationDateAndPlace(timeslot.getReservationDate(), timeslot.getPlace());

        Time startTime = timeslot.getStartTime();
        Time endTime = timeslot.getEndTime();

        for(Timeslot t : timeslotList) {
            if(Objects.equals(t.getId(), timeslot.getId())) continue;
            Time st = t.getStartTime();
            Time et = t.getEndTime();
            boolean isValid = st.compareTo(endTime) >= 0 || et.compareTo(startTime) <= 0;
            if(!isValid) throw new BusinessException(ErrorCode.TIMESLOT_DUPLICATED_TIME);
        }
    }

    public Long createTimeslot(TimeslotDto dto) {
        Place place = placeService.getPlaceById(
                dto.getPlace().getId()
        );

        Timeslot timeslot = Timeslot.builder()
                .place(place)
                .reservationDate(dto.getReservationDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .maxCapacity(dto.getMaxCapacity())
                .slotStatus(dto.getSlotStatus())
                .build();

        validation(timeslot);

        return timeslotRepository.save(timeslot).getId();
    }

    public List<Timeslot> getTimeslotByDate(Date date) {
        return timeslotRepository
                .findTimeslotsByReservationDate(date);
    }

    public Timeslot getTimeslotById(Long id) {
        return timeslotRepository.findTimeslotsById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TIMESLOT_NOT_FOUND));
    }

    public List<Timeslot> getAll() {
        return timeslotRepository.findAll();
    }

}
