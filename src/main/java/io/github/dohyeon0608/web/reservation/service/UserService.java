package io.github.dohyeon0608.web.reservation.service;

import io.github.dohyeon0608.web.reservation.dto.request.UserRegistrationDto;
import io.github.dohyeon0608.web.reservation.entity.User;
import io.github.dohyeon0608.web.reservation.exception.BusinessException;
import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserRegistrationDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .role(dto.getRole())
                .build();

        user.encodePassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user).getId();
    }

    public User getUserById(Long id) throws BusinessException{
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User getUserByEmail(String email) throws BusinessException{
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

}
