package io.github.dohyeon0608.web.reservation.entity.mapping;

import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation extends BaseEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    public void cancel() {
        this.reservationStatus = ReservationStatus.CANCELLED;
        this.timeslot.removeReservation(this);
        this.delete();
    }

    public void changeTimeslot(Timeslot timeslot) {
        if (this.timeslot == timeslot) {
            return;
        }

        if (this.timeslot != null) {
            Timeslot oldTimeslot = this.timeslot;
            this.timeslot = null; // 무한 루프 방지를 위해 나를 먼저 비움
            oldTimeslot.removeReservation(this);
        }

        this.timeslot = timeslot;

        if (timeslot != null) {
            timeslot.addReservation(this);
        }
    }
}
