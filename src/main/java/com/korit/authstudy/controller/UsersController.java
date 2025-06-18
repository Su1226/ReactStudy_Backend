package com.korit.authstudy.controller;

import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.dto.UserRegisterDto;
import com.korit.authstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
        log.info("DTO : {}", dto);
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        return ResponseEntity.ok(userService.login(dto));
    }
    // 로그인에서 POST, '생성'한다는 점에 집중해야 한다.
    // 즉, 로그인에 대해 성공했는지, 실패했는지에 대해서에 대한 정보를 생성한다.
    // 로그인의 최종 목적은 '토큰'을 저장하는 것이다.
    // 클라이언트 측에서 세션 인증 방식을 할지, 토큰 인증 방식(JWT)를 할지 정해지게 된다.


}

// Security 때문에 더 이상 함부러 데이터 주입이 불가능해졌다.
// 때문에 config 패키지의 SecurityConfig에서 데이터를 받아온다.