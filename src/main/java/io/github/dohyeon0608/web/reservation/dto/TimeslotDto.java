package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import lombok.Builder;

import java.sql.Date;
import java.sql.Time;

@Builder
public class TimeslotDto {
    private Place place;

    private Date reservationDate;

    private Time startTime;

    private Time endTime;

    private Integer maxCapacity;

    public static TimeslotDto from(Timeslot timeslot) {
        return TimeslotDto.builder()
                .place(timeslot.getPlace())
                .reservationDate(timeslot.getReservationDate())
                .startTime(timeslot.getStartTime())
                .endTime(timeslot.getEndTime())
                .maxCapacity(timeslot.getMaxCapacity())
                .build();
    }
}
