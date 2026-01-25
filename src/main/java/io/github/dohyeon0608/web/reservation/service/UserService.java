package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.UserRegistrationDto;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private void validation(User user) {
        if(userRepository.existsUserByEmail(user.getEmail())) {
            throw new BusinessException(ErrorCode.USER_DUPLICATED_EMAIL);
        }
    }

    public Long createUser(UserRegistrationDto dto) {
        // TODO: 비밀번호 암호화 (아직 Security 구성 전이기에 평문으로 저장. 반드시 암호화 기능 구현할 것!!)

        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .build();

        validation(user);

        return userRepository.save(user).getId();
    }

    public User getUserById(Long id) throws BusinessException{

        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

}
