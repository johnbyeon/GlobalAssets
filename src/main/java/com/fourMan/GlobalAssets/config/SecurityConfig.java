//package com.fourMan.GlobalAssets.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 활성화
//public class SecurityConfig {
//    // 비밀번호 암호 처리 기계 추가
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/", "/login", "/signup", "/signupProc", "/home").permitAll()
//                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()//
//                        .requestMatchers("/fragments/header", "/fragments/footer").permitAll()
//                        .requestMatchers("/image/**").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers("/chart/**").permitAll()
//                        .requestMatchers("/*").permitAll()
//                        .requestMatchers("/start/*").permitAll()
//                        .anyRequest().authenticated()
//                );
//        // Login 요청 처리
//        http
//                .formLogin((auth) -> auth
//                        .loginPage("/login")
//                        .loginProcessingUrl("/loginProc")
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                );
//
//        http
//                .logout((auth) -> auth
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                );
//
//        http
//                .csrf((auth) -> auth.disable());
//
//        return http.build();
//    }
//}
package com.fourMan.GlobalAssets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 활성화
public class SecurityConfig {
    // 비밀번호 암호 처리 기계 추가
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 테스트 중 폼 POST 403 방지
                .csrf(csrf -> csrf.disable())
                // (H2 콘솔 쓰면) frame 허용
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/signup", "/signupProc", "/home",
                                "/css/**", "/js/**",  "/webjars/**", "/image/**",
                                "/favicon.ico", "/h2-console/**"
                        ).permitAll()
                        // ★ 여기! 나머지 전부 허용
                        .anyRequest().permitAll()
                )
                // 로그인/로그아웃도 막지 않음
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.permitAll())
                // 굳이 필요없으면 비활성화
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}