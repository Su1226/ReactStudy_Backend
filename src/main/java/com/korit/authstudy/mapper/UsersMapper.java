package com.korit.authstudy.mapper;

import com.korit.authstudy.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {

    public int updateFullNameOrEmailById(User user);
    public int updatePassword(@Param("id") Integer userId, @Param("password") String newPassword);
}

// Mapper를 사용하기
// 1. application.yml에서 mapper-location 설정
// 2. resource 폴더의 mapper에 xml 파일 생성
// 3. Java에서 mapper 폴더의 Mapper interface를 생성한다. -> @Mapper
// 4. xml 파일에서 -> MyBatis 홈페이지에 가서 문법을 가져온다. + 문법 작성
// 4-1. https://mybatis.org/mybatis-3/getting-started.html -> Exploring Mapped SQL Statements 내용을 가져온다.
// 5. 3번과 4번 파일을 연결한다.
