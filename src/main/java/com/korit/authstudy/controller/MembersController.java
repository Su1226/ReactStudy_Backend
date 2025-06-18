package com.korit.authstudy.controller;


import com.korit.authstudy.dto.MemberRegisterDto;
import com.korit.authstudy.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MembersController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody MemberRegisterDto dto) {
        log.info("DTO : {}", dto);
        return ResponseEntity.ok(memberService.register(dto));
    }
}
