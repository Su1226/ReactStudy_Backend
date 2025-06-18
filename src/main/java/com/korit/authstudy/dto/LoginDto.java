package com.korit.authstudy.dto;

import com.korit.authstudy.domain.entity.User;
import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;

}
