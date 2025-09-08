package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    public static User fromDto(UserDto dto){

        return new User(
                dto.getUserId(),
                dto.getEmail(),
                dto.getUserStatus(),
                dto.getPassword(),
                dto.getNickname(),
                dto.getFirstDate(),
                dto.getLastDate()
        );
    }
    public static UserDto fromEntity(User entity){
        return new UserDto(
                entity.getUserId(),
                entity.getEmail(),
                entity.getUserStatus(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getFirstDate(),
                entity.getLastDate()
        );
    }
}
