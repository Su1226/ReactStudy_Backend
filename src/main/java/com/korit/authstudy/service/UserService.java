package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.UserRegisterDto;
import com.korit.authstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    // final이 붙은 객체를 초기화를 한 번 해준다.
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User register(UserRegisterDto dto) {
        User insertedUser = userRepository.save(dto.toEntity(passwordEncoder));
        // 비밀번호를 암호화 하기 위해서

        return insertedUser;
    }

}
