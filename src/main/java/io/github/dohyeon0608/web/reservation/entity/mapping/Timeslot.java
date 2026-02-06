package io.github.dohyeon0608.web.reservation.entity.mapping;

import io.github.dohyeon0608.web.reservation.entity.Place;
import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.SlotStatus;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxCapacity = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SlotStatus slotStatus = SlotStatus.OPENED;

    @OneToMany(mappedBy = "timeslot", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Reservation> reservationList = new ArrayList<>();

    @Formula("(SELECT count(*) FROM reservation r WHERE r.timeslot_id = id AND r.reservation_status = 'CONFIRMED')")
    private Integer reservationCount = 0;

    private void open() {
        this.slotStatus = SlotStatus.OPENED;
    }

    private void close() {
        this.slotStatus = SlotStatus.CLOSED;
    }

    // 반드시 Reservation 엔티티 저장 전에 실행할 것.
    public void addReservation(Reservation reservation) {
        if (this.reservationList.contains(reservation)) {
            return;
        }

        if(this.slotStatus == SlotStatus.CLOSED) {
            throw new BusinessException(ErrorCode.RESERVATION_CANNOT_ON_CLOSED_SLOT);
        }

        reservationList.add(reservation);

        reservation.changeTimeslot(this);

        if(this.reservationCount + 1 >= this.maxCapacity) {
            this.close();
        }

    }

    public void removeReservation(Reservation reservation) {
        if (reservation == null || !this.reservationList.contains(reservation)) {
            return;
        }

        this.reservationList.remove(reservation);

        this.open();

    }

}
