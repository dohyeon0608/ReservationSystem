package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.User;
import lombok.Builder;

@Builder
public class UserDto {
    private String email;

    private String nickname;

    public static UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
