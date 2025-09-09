package com.fourMan.GlobalAssets.entity;

import com.fourMan.GlobalAssets.dto.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserEntity {
    //빠른검색을 위한 유저아이디 숫자로 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    //직접입력받는 실제 아이디 이메일
    @Column(unique = true, nullable = false)
    private String email;
    //유저권한 권한끄면 사용불가
    private Boolean userStatus;
    @Column(nullable = false)
    private String password;

    private String nickname;
    //가입날짜
    private Timestamp firstDate;
    //마지막로그인
    private Timestamp LastDate;
    //관리자, 일반사용자 구분용
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
