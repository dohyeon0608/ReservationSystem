package io.github.dohyeon0608.web.reservation.entity.mapping;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Date;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Timeslot extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(nullable = false)
    private Date reservation_date;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Column(nullable = false)
    private Integer max_capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus slotStatus;
}
