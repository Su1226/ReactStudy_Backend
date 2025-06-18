package com.korit.authstudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.korit.authstudy.domain.entity.Member;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@ToString
public class MemberRegisterDto {

    private String username;
    private String password;
    private String fullName;
    private String email;

    public Member toEntitiy(BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .memberName(username)
                .password(passwordEncoder.encode(password))
                .name(fullName)
                .email(email)
                .build();
    }

    /*public Member toEntity() {
        Member member = new Member();
        member.setMemberName(username);
        member.setPassword(password);
        member.setName(fullName);
        member.setEmail(email);

        return member;
    }*/
}
