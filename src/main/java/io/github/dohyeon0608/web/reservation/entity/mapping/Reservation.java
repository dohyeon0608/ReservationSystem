package io.github.dohyeon0608.web.reservation.entity.mapping;

import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;
}
