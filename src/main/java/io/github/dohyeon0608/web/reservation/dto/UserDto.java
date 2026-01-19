package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDto {
    private String email;
    private String nickname;

    public static UserDto from(User user) {
        return new UserDto(user.getEmail(), user.getNickname());
    }
}
