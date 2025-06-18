package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.Member;
import com.korit.authstudy.dto.MemberRegisterDto;
import com.korit.authstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Member register(MemberRegisterDto dto) {
//        Member member = new Member();
//        member.setMemberName(dto.getFullName());
//        member.setPassword(dto.getPassword());
//        member.setName(dto.getFullName());
//        member.setEmail(dto.getEmail());

        Member insertedMember = memberRepository.save(dto.toEntitiy(passwordEncoder));

        return insertedMember;
    }
}
