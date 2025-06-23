package com.korit.authstudy.repository;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.UserRegisterDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);

    // JPQL : SQL로 변환해준다.
    // Table 이름만 들어가는 곳에는 User 객체가 들어가야 한다.
    @Query("""
    update User
    set fullName = :#{#user.fullName}, email = :#{#user.email}
    where id = :#{#user.id}
    """)

    @Modifying(clearAutomatically = true)
    int updateFullNameOrEmailById(@Param("user") User user);

}

// JPA를 사용하기 위해서는?
// 1. application.yml에서 설정
// 2. Entity(테이블 기준)으로 생성
// 3. Repository interface 생성
