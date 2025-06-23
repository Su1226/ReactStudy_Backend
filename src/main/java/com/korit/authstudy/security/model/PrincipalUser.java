package com.korit.authstudy.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Data
public class PrincipalUser implements UserDetails {

    private Integer userId;
    private String username;
    @JsonIgnore
    private String password;
    // @JsonIgnore는 JSON에서 password를 표시하지 않는다.

    private String fullName;
    private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
