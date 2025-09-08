package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class User {
    //빠른검색을 위한 유저아이디 숫자로 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    //직접입력받는 실제 아이디 이메일
    private String email;
    //권한끄면 사용불가
    private Boolean userStatus;
    @Column(nullable = false)
    private String password;

    private String nickname;
    //가입날짜
    private Timestamp firstDate;
    //마지막로그인
    private Timestamp LastDate;
}
