package com.korit.authstudy.security.jwt;

import io.jsonwebtoken.*;
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

//    public String generateAccessToken(String id) {
//        JwtBuilder jwtBuilder = Jwts.builder(); // 토큰 생성에 필요한 정보를 입력
//        jwtBuilder.subject("AccessToken");
//        jwtBuilder.id(id);
//        Date expirDate = new Date(new Date().getTime() + (1000l * 60l * 60l * 24l * 30l));    // 매개변수로 특정 날짜를 넣어줄 수 있다.
//        jwtBuilder.expiration(expirDate);       // 토큰의 만료 기간은 30일
//        // Claims를 설정하는 부분.
//
//        jwtBuilder.signWith(KEY);               // KEY값을 넣는다.
//
//        String token = jwtBuilder.compact();    // 입력된 정보로 문자열 JWT 토큰 생성
//
//        System.out.println(token);
//        return token;
//    } // JWT Token 생성

    public String generateAccessToken(String id) {
        return Jwts.builder()
                .subject("AccessToken")
                .id(id)
                .expiration(new Date(new Date().getTime() + (1000l * 60l * 60l * 24l * 30l)))
                .signWith(KEY)
                .compact();
    }

    public boolean isBearer(String token) {
        // JWT Token인가?

        if (token == null) {
            return false;
        } // token이 비었는가?

        if (!token.startsWith("Bearer ")) {
            return false;
        } // Bearer로 시작하는가?

        return true;
        // token이 존재하고 JWT Token이 맞다.
    }

    public String removeBearer(String bearerToken) {
        return bearerToken.replaceFirst("Bearer ", "");
    }

    public Claims getClaims(String token) {
        // Jwts.parser()는 만들어진 Token을 확인하는 메소드.
        JwtParserBuilder jwtParserBuilder = Jwts.parser();
        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();

        return jwtParser.parseClaimsJws(token).getPayload();
    } // JWT Token을 풀어준다.


    // secret key 생성 : https://jwtsecrets.com/
}
