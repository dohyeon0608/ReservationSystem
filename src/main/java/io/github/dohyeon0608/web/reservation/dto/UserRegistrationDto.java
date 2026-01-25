package io.github.dohyeon0608.web.reservation.dto;

import io.github.dohyeon0608.web.reservation.entity.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "이름을 입력해주세요")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotNull(message = "역할을 선택해주세요")
    private UserRole role;

}
