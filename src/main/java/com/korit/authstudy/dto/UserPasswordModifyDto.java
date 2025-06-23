package com.korit.authstudy.dto;

import com.korit.authstudy.domain.entity.User;
import lombok.Data;

@Data
public class UserPasswordModifyDto {
    private String oldPassword;
    private String newPassword;
    private String newPasswordCheck;
}
