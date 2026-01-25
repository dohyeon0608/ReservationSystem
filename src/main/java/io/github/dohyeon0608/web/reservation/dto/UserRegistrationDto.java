package io.github.dohyeon0608.web.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRegistrationDto {

    private String email;

    private String nickname;

    private String password;

}
