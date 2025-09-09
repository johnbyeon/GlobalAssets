package com.fourMan.GlobalAssets.service;


import com.fourMan.GlobalAssets.config.ADMIN;
import com.fourMan.GlobalAssets.dto.LoginDto;
import com.fourMan.GlobalAssets.dto.SignupDto;
import com.fourMan.GlobalAssets.dto.UserRole;
import com.fourMan.GlobalAssets.entity.UserEntity;
import com.fourMan.GlobalAssets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void SignupProcess(SignupDto signupDto) {
        // 1. 받은 Dto 안에 username 을 가진 회원이 존재하는지 확인
        boolean isUser = userRepository.existsByEmail(signupDto.getEmail());
        if (isUser) {
            return;
        }
        // 2. 없으면 비밀번호를 암호화 해서 저장하기
        UserEntity newUser = new UserEntity();
        newUser.setEmail(signupDto.getEmail());
        // 비밀번호를 암호화 해서 엔티티에 넣자
        newUser.setPassword(bCryptPasswordEncoder
                .encode(signupDto.getPassword()));
        // Role 추가
        newUser.setUserStatus(Boolean.TRUE);
        newUser.setNickname(signupDto.getNickName());
        newUser.setFirstDate(Timestamp.valueOf(LocalDateTime.now()));
        newUser.setLastDate(Timestamp.valueOf(LocalDateTime.now()));

        if(signupDto.getEmail().equals(ADMIN.ADMIN_ID))//config에 정의된 admin 아이디
            newUser.setRole(UserRole.ROLE_ADMIN);
        else
            newUser.setRole(UserRole.ROLE_USER);
        // 저장
        userRepository.save(newUser);
    }


}
