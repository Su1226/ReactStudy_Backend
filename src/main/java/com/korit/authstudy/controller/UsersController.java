package com.korit.authstudy.controller;

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
}

// Security 때문에 더 이상 함부러 데이터 주입이 불가능해졌다.
// 때문에 config 패키지의 SecurityConfig에서 데이터를 받아온다.