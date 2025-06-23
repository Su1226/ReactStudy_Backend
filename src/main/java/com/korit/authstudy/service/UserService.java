package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.*;
import com.korit.authstudy.mapper.UsersMapper;
import com.korit.authstudy.repository.UserRepository;
import com.korit.authstudy.security.jwt.JwtUtil;
import com.korit.authstudy.security.model.PrincipalUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    // final이 붙은 객체를 초기화를 한 번 해준다.
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final JwtUtil jwtUtil;

    public User register(UserRegisterDto dto) {
        User insertedUser = userRepository.save(dto.toEntity(passwordEncoder));
        // 비밀번호를 암호화 하기 위해서

        return insertedUser;
    }

    public JwtDto login(LoginDto dto) {
        List<User> foundUsers = userRepository.findByUsername(dto.getUsername());

        if (foundUsers.isEmpty()) {
            throw new UsernameNotFoundException("사용자 정보를 확인하세요.");
        }

        User user = foundUsers.get(0);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            // (원본 값, 암호화 된 값)
            throw new BadCredentialsException("사용자 정보를 확인하세요.");
        }

        System.out.println("로그인 성공하여 토큰을 생성합니다.");
        String token = jwtUtil.generateAccessToken(user.getId().toString());
        // return이 성공적으로 되었을 때만, 토큰이 생성되고 반환된다.
        return JwtDto.builder().accessToken(token).build();
    }

    // 토큰 발행을 위해서는 MVN에서 JJWT 라이브러리를 가져와야 한다.
    // 1. JJWT :: API, 3. JJWT :: Impl. 4. JJWT :: Extensions :: Jackson을 가져와야 한다.

    @Transactional(rollbackOn = Exception.class)    // 중간에 에러가 발생하면 모든 것을 중단한다.
    public void modifyFullNameOrEmail(Integer userId, UserModifyDto dto) {
       User user = dto.toEntity(userId);
       userRepository.updateFullNameOrEmailById(user); // JPA는 자동으로 AUTO COMMIT이 되지 않기 때문에, 직접 해줘야 한다.
//       int updateCount = usersMapper.updateFullNameOrEmailById(user); // MyBATIS는 자동으로 AUTO COMMIT이 된다.
//       System.out.println(updateCount);
    }

    @Transactional(rollbackOn = Exception.class)
    public void modifyPassword(UserPasswordModifyDto dto, PrincipalUser principalUser) {
        // 1. 현재 로그인 되어 있는 비밀번호와 요청 때 받은 현재 비밀번호가 일치하는지 확인.
        if(!passwordEncoder.matches(dto.getOldPassword(), principalUser.getPassword())) {
            throw new BadCredentialsException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 새 비밀번호와 새 비밀번호가 확인이 일치하는지
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(dto.getNewPassword());
        usersMapper.updatePassword(principalUser.getUserId(), encodePassword);

    }
}
