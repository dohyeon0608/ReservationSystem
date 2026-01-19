package io.github.dohyeon0608.web.reservation.entity;

import io.github.dohyeon0608.web.reservation.entity.common.BaseEntity;
import io.github.dohyeon0608.web.reservation.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
