package com.korit.authstudy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginStatusDto {
    private String status;
    private boolean isLogin;
    private Integer userId;
}
