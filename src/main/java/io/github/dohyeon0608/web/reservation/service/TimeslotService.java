package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeslotService {
    private final TimeslotRepository timeslotRepository;

    private void validation(Timeslot timeslot) {
        if(timeslot.getEndTime().before(timeslot.getStartTime())) {
            throw new BusinessException(ErrorCode.TIMESLOT_INVALID_TIME);
        }

        List<Timeslot> reservationsOnDateAndPlace = timeslotRepository
                .findTimeslotsByReservationDateAndPlace(timeslot.getReservationDate(), timeslot.getPlace());

        Time startTime = timeslot.getStartTime();
        Time endTime = timeslot.getEndTime();

        for(Timeslot t : reservationsOnDateAndPlace) {
            boolean isValid = t.getStartTime().after(endTime) || t.getEndTime().before(startTime);
            if(!isValid) throw new BusinessException(ErrorCode.TIMESLOT_DUPLICATED_TIME);
        }
    }

    public Long createTimeslot(Place place, Date date, Time startTime, Time endTime, Integer maxCapacity, SlotStatus status) {
        Timeslot timeslot = Timeslot.builder()
                .place(place)
                .reservationDate(date)
                .startTime(startTime)
                .endTime(endTime)
                .maxCapacity(maxCapacity)
                .slotStatus(status)
                .build();

        validation(timeslot);

        return timeslotRepository.save(timeslot).getId();
    }

    public List<Timeslot> getTimeslotByDate(Date date) {
        return timeslotRepository.findTimeslotsByReservationDate(date);
    }

}
