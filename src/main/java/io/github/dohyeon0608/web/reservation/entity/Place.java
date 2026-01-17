package io.github.dohyeon0608.web.reservation.entity;

import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.OperationStatus;
import io.github.dohyeon0608.web.reservation.entity.enums.PlaceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Place extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationStatus operationStatus;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer max_capacity;
}
