package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    // username으로 검색 한 결과 리턴
    UserEntity findByEmail(String email);

}
