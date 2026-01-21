package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.entity.mapping.Timeslot;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Builder
public class TimeslotDto {
    private Long id;

    private PlaceDto place;

    private Date reservationDate;

    private Time startTime;

    private Time endTime;

    @Builder.Default
    private Integer maxCapacity = 1;

    @Builder.Default
    private SlotStatus slotStatus = SlotStatus.CLOSED;

    public static TimeslotDto from(Timeslot timeslot) {
        return TimeslotDto.builder()
                .id(timeslot.getId())
                .place(PlaceDto.from(timeslot.getPlace()))
                .reservationDate(timeslot.getReservationDate())
                .startTime(timeslot.getStartTime())
                .endTime(timeslot.getEndTime())
                .maxCapacity(timeslot.getMaxCapacity())
                .slotStatus(timeslot.getSlotStatus())
                .build();
    }
}
