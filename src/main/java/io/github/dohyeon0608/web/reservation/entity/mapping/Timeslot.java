package io.github.dohyeon0608.web.reservation.entity.mapping;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Timeslot extends BaseEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(nullable = false)
    private Date reservationDate;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxCapacity = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SlotStatus slotStatus = SlotStatus.OPENED;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservationList;

    private void open() {
        this.slotStatus = SlotStatus.OPENED;
    }

    private void close() {
        this.slotStatus = SlotStatus.CLOSED;
    }

    public void addReservation(Reservation reservation) {
        if(reservationList.size() >= this.maxCapacity) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_FULL);
        }

        reservationList.add(reservation);

        if(reservationList.size() == this.maxCapacity) {
            this.close();
        }

    }

    public void removeReservation(Reservation reservation) {
        if(!reservationList.contains(reservation)) {
            return;
        }

        this.open();
        reservationList.remove(reservation);

    }

}
