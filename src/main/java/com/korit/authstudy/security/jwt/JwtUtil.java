package com.korit.authstudy.security.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key KEY;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String id) {
        JwtBuilder jwtBuilder = Jwts.builder(); // 토큰 생성에 필요한 정보를 입력
        jwtBuilder.subject("AccessToken");
        jwtBuilder.id("101");
        Date expirDate = new Date(new Date().getTime() + (1000l * 60l * 60l * 24l * 30l));    // 매개변수로 특정 날짜를 넣어줄 수 있다.
        jwtBuilder.expiration(expirDate);       // 토큰의 만료 기간은 30일
        jwtBuilder.signWith(KEY);

        String token = jwtBuilder.compact();    // 입력된 정보로 문자열 JWT 토큰 생성

        System.out.println(token);
        return token;
    }


    // secret key 생성 : https://jwtsecrets.com/
}
