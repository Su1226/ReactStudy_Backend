package com.korit.authstudy.controller;

import com.korit.authstudy.dto.*;
import com.korit.authstudy.exception.MyAccountException;
import com.korit.authstudy.security.model.PrincipalUser;
import com.korit.authstudy.security.service.JwtService;
import com.korit.authstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
        log.info("DTO : {}", dto);
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        JwtDto jwtDto = userService.login(dto);

        // 예외 발생 시(아이디나 패스워드 둘 중 하나 이상 틀렸을 경우), ControllerAdvice에서 예외를 처리한다.
        // 하나씩 if문을 거는 것보다 예외를 터트려서 응답을 처리하도록 한다.

        System.out.println("로그인 컨트롤러 호출");

        return ResponseEntity.ok(jwtDto);
    }
    // 로그인에서 POST, '생성'한다는 점에 집중해야 한다.
    // 즉, 로그인에 대해 성공했는지, 실패했는지에 대해서에 대한 정보를 생성한다.
    // 로그인의 최종 목적은 '토큰'을 저장하는 것이다.
    // 클라이언트 측에서 세션 인증 방식을 할지, 토큰 인증 방식(JWT)를 할지 정해지게 된다.

    @GetMapping("/login/status")
    public ResponseEntity<?> getLoginStatus(@RequestHeader("Authorization") String authorization) {
        System.out.println(authorization);
        return ResponseEntity.ok(jwtService.validLoginAccessToken(authorization));
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipalUser() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    } // 비회원 계정으로 들어오는 경우.

    @PutMapping("/{userId}")
    public ResponseEntity<?> modifyFullNameOrEmail(@PathVariable Integer userId, @RequestBody UserModifyDto dto) {
        System.out.println(userId);
        System.out.println(dto);
        userService.modifyFullNameOrEmail(userId, dto);
        return ResponseEntity.ok("변경 성공");
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> modifyPassword(@PathVariable Integer userId, @RequestBody UserPasswordModifyDto dto, @AuthenticationPrincipal PrincipalUser principalUser) {
        System.out.println(userId);
        System.out.println(dto);
        
        if (!userId.equals(principalUser.getUserId())) {
            throw new MyAccountException("본인의 계정만 변경할 수 있습니다.");
        } // 해당 if문에 걸릴 시, ControllerAdivce 쪽에서 처리한다. 

        userService.modifyPassword(dto, principalUser);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }


}

// Security 때문에 더 이상 함부러 데이터 주입이 불가능해졌다.
// 때문에 config 패키지의 SecurityConfig에서 데이터를 받아온다.