package com.korit.authstudy.dto;


import com.korit.authstudy.domain.entity.User;
import lombok.Data;

@Data
public class UserModifyDto {
    private String fullName;
    private String email;

    public User toEntity(Integer userId) {
        return User.builder()
                .id(userId)
                .fullName(fullName)
                .email(email)
                .build();
}
}
