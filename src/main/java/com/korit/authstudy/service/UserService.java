package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.JwtDto;
import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.dto.UserRegisterDto;
import com.korit.authstudy.repository.UserRepository;
import com.korit.authstudy.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    // final이 붙은 객체를 초기화를 한 번 해준다.
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User register(UserRegisterDto dto) {
        User insertedUser = userRepository.save(dto.toEntity(passwordEncoder));
        // 비밀번호를 암호화 하기 위해서

        return insertedUser;
    }

    public JwtDto login(LoginDto dto) {
        List<User> foundUsers = userRepository.findByUsername(dto.getUsername());

        if (foundUsers.isEmpty()) {
            System.out.println("아이디 없음");
            return null;
        }

        User user = foundUsers.get(0);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            // (원본 값, 암호화 된 값)
            System.out.println("비밀번호 틀림.");
            return null;
        }

        System.out.println("로그인 성공하여 토큰을 생성합니다.");
        String token = jwtUtil.generateAccessToken(user.getId().toString());

        return JwtDto.builder().accessToken(token).build();
    }

    // 토큰 발행을 위해서는 MVN에서 JJWT 라이브러리를 가져와야 한다.
    // 1. JJWT :: API, 3. JJWT :: Impl. 4. JJWT :: Extensions :: Jackson을 가져와야 한다.

}
