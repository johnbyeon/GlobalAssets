package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //빠른검색을 위한 유저아이디 숫자로 생성

    private Long userId;
    @NotNull
    //직접입력받는 실제 아이디 이메일
    private String email;
    //권한끄면 사용불가
    private Boolean userStatus;
    @NotNull
    private String password;

    private String nickname;
    //가입날짜
    private Timestamp firstDate;
    //마지막로그인
    private Timestamp LastDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static UserEntity fromDto(UserDto dto){
        UserEntity entity = new UserEntity();
        entity.setUserId(dto.getUserId());
        entity.setEmail(dto.getEmail());
        entity.setUserStatus(dto.getUserStatus());
        entity.setPassword(dto.getPassword());
        entity.setNickname(dto.getNickname());
        entity.setFirstDate(dto.getFirstDate());
        entity.setLastDate(dto.getLastDate());
        entity.setRole(dto.getRole());
        return entity;
    }

    public static UserDto fromEntity(UserEntity entity){
        return new UserDto(
                entity.getUserId(),
                entity.getEmail(),
                entity.getUserStatus(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getFirstDate(),
                entity.getLastDate(),
                entity.getRole()
        );
    }
}
