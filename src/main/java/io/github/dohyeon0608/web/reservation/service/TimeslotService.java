package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.TimeslotDto;
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
            Time st = t.getStartTime();
            Time et = t.getEndTime();
            boolean isValid = st.compareTo(endTime) >= 0 || et.compareTo(startTime) <= 0;
            if(!isValid) throw new BusinessException(ErrorCode.TIMESLOT_DUPLICATED_TIME);
        }
    }

    public Long createTimeslot(TimeslotDto dto) {
        Timeslot timeslot = Timeslot.builder()
                .place(dto.getPlace())
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

}
