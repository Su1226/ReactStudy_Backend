package com.korit.authstudy.controller;

import com.korit.authstudy.exception.BearerValidException;
import com.korit.authstudy.exception.MyAccountException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 권한이 없을 때, 즉 인증이 되지 않았을 때
// 예외를 처리하는 기능을 하는 클래스
// 해당 클래스에서 로그인이나 패스워드가 틀렸을 때, 처리한다.

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> unauthorized(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    } // 인증이 안되었을 때 (401 Error)

    @ExceptionHandler(BearerValidException.class)
    public ResponseEntity<?> isNotBearer(BearerValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    } // JWT Token이 잘못되었을 때 (400 Error)

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> jwtError(JwtException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    } // 못 쓰는 토큰의 경우. (기한이 끝나거나 위조된 경우)

    @ExceptionHandler(MyAccountException.class)
    public ResponseEntity<?> isNotMyAccount(MyAccountException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    } // 자신의 계정이 아닌 다른 계정의 정보를 바꾸려고 할 때. -> 권한 없음.
}
