package io.github.dohyeon0608.web.reservation.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // TODO: 테스트를 위한 임시 개방, 이후 주석 처리
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests((requests) -> requests
//                            .anyRequest().permitAll());

        http
                // TODO: 테스트로 csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 1. URL별 접근 권한 설정
                .authorizeHttpRequests((requests) -> requests
                        // 인증 없이 접근 가능한 경로
                        .requestMatchers("/", "/home", "/register", "/user/register", "/css/**").permitAll()
                        // ADMIN 권한이 있어야 접근 가능한 경로
                        .requestMatchers(new RegexRequestMatcher(".*admin.*", null)).hasRole("ADMIN")
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 2. 폼 로그인 설정
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()  // 로그인 페이지는 누구나 접근 가능
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
