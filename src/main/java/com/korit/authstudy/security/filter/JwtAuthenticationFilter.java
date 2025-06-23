package com.korit.authstudy.security.filter;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.repository.UserRepository;
import com.korit.authstudy.security.jwt.JwtUtil;
import com.korit.authstudy.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.models.PathItem;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // doFilter가 실행되기 전에 JWT Token을 검사해줘야 한다.
        // 로그인이 되어야 token이 만들어지기 때문에, 로그인은 거부해서는 안된다. -> securityConfig에서 설정

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // servlet은 어떤 응답이든 처리할 수 있도록 해주는 최상위 타입의 인터페이스
        // HttpServletRequest는 그 중 HTTP 전용 구현체로, HttpServletRequest를 사용하기 위해 다운 캐스팅.

        // 해당 메소드가 아니면, 그냥 다음 필터로 넘긴다.
        List<String> methods = List.of("POST", "GET", "PUT", "PATCH", "DELETE");
        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorization = request.getHeader("Authorization");

        System.out.println("Barer Token : " + authorization);
        if (jwtUtil.isBearer(authorization)) {
            String acessToken = jwtUtil.removeBearer(authorization);
            // Filter에서는 ControllerAdvice를 들어갈 필요가 없다.
            // 아직 Controller로 들어가기 전이기 때문이다.
            try {
                Claims claims = jwtUtil.getClaims(acessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                Optional<User> foundUser = userRepository.findById(userId);
                foundUser.ifPresentOrElse(
                        (user) -> {
                            // UserEntity를 Security에서 인증 객체로 사용할 PrincipalUser로 변환
                            // 즉, User를 찾아서 만들어주는 Token이다.
                            PrincipalUser principalUser = PrincipalUser.builder()
                                    .userId(user.getId())
                                    .username(user.getUsername())
                                    .password(user.getPassword())
                                    .fullName(user.getFullName())
                                    .email(user.getEmail())
                                    .build();

                            // 변환된 PrincipalUser을 Authentication의 UsernamePasswordAuthenticationToken을 업 캐스팅 해서 값을 넣는다.
                            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());

                            // 싱글톤 형식으로 .getContext()를 가져오며, setAuthentication에 넣어주고 나면
                            // UsernamePasswordAuthenticationToken로 가서 get으로 가져와 유효한지 확인한다.
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            // 만약 없다면, 아무것도 안하고 돌려보낸다.
                            // 있다면, 이미 로그인이 된 상태.
                            System.out.println("인증 성공");
                            System.out.println(authentication.getName());
                        }, () -> {
                            // 예외 처리
                            // UserEntity를 찾지 못한 경우 실행되는 부분
                            // DB에서 사용자를 찾지 못한 경우 실행되는 부분
                            throw new AuthenticationServiceException("인증 실패");
                        });
            } catch (JwtException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
