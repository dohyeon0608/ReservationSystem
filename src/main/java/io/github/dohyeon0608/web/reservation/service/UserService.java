package io.github.dohyeon0608.web.reservation.service;

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

    public Long createUser(String email, String password, String nickname) {
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        validation(user);

        return userRepository.save(user).getId();
    }

    public User getUserById(Long id) throws BusinessException{

        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

}
