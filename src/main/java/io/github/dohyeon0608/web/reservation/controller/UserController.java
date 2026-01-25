package io.github.dohyeon0608.web.reservation.controller;

import io.github.dohyeon0608.web.reservation.dto.UserDto;
import io.github.dohyeon0608.web.reservation.dto.UserRegistrationDto;
import io.github.dohyeon0608.web.reservation.service.UserService;
import io.github.dohyeon0608.web.reservation.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody UserRegistrationDto dto) {
        Long id = userService.createUser(dto);
        UserDto result = UserDto.from(userService.getUserById(id));

        return ApiResponse.create(result).toResponseEntity();
    }

}
