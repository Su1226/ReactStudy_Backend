package com.korit.authstudy.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtDto {
    private String accessToken;
}
