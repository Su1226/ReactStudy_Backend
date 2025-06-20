package com.korit.authstudy.config;

import com.korit.authstudy.Filter.StudyFilter;
import com.korit.authstudy.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final StudyFilter studyFilter;
    private final JwtAuthenticationFilter  jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // 암호화를 할 수는 있지만, 복호화는 불가능하다.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CorsOrigin 설정
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 요청을 보내는 쪽의 도메인(사이트 주소)에 대한 제약
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        // 요청을 보내는 쪽에서 Request, Response HEADER 정보에 대한 제약
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // 요청을 보내는 쪽의 메소드(GET, POST, PUT, DELETE, OPTION 등)에 대한 제약
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);

        // 요청 URL(/api/users)에 대한 CORS 설정 적용 위해 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
    // 매번 Controller를 만들 때마다 @CorsConfiguration을 달지 않기 위해서 만듦.
    // Cors 관련 설정 함수
    // 더 디테일한 설정을 하고자 할 때, 그 때 더 찾아보고 공부하는 것.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());               // 위에서 만든 cors origin 설정(bean)을 security에 적용.
        http.csrf(csrf -> csrf.disable());                  // 서버 사이드 랜더링이 아니니 REST API 방식에서는 비활성화.
        http.formLogin(formLogin -> formLogin.disable());   // 서버 사이드 랜더링 로그인 방식 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());   // HTTP 프로토콜 기본 로그인 방식 비활성화
        http.logout(logout -> logout.disable());            // 서버 사이드 랜더링 로그아웃 방식 비활성화

//        http.addFilterBefore(studyFilter, UsernamePasswordAuthenticationFilter.class);
        // 위의 security는 비활성화 하지만, 그것을 바활성화 하기 전에 해당 코드의 Filter는 실행한다.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 특정 요청 URL에 대한 권한 설정
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/users", "/api/users/login", "api/users/login/status", "/api/users/principal").permitAll(); // 로그인과 회원가입은 권한을 다 준다.
            auth.anyRequest().authenticated(); // 모든 요청을 인증 받아야 한다.
        });
        // 인증을 요청할 주소에서, 일단 모든 요청들에게 모든 권한을 준다.
        // 이렇게 설정을 하면 Security가 모든 권한을 풀어준다.
        // 이후에 설정을 통해 Security가 특정 권한만 가진 사람만 사용이 가능하도록 바꿔준다.
        // 현재는 테스트용으로 다음과 같이 사용한다.

        // HttpSecurity 객체에 설정한 모든 정보를 기반으로 build하여 SecurityFilterChain 객체 생성 후 Bean 등록.
        return http.build();
    }
}

// Bean로 만드는 방법은 2가지이다.
// 1. `@`을 달아서 IoC 컨테이너에 Bean로 등록한다. -> 예) Controller, Config 등등
// 2. `@Bean`을 다는 경우는, interface로 받아오는 경우이다. -> 인터페이스에는 애노테이션을 달 수 없다.

// 부품을 조립하는 개념이라고 생각하면 편하다.


// SSR은 클라이언트에서 응답으로 HTML을 전달한다.
// 이때, csrf 토큰을 form과 관련된 부분에 모두 토큰을 넣어
// 모든 토큰이 맞게 들어왔을 경우에만 서버에 데이터를 넣는다.
// 그러나 우리는 csrf 토큰을 사용하지 않고, jwf 토큰을 이용하는 것을 배울 예정이다.

// 참조 자료 : https://siyoon210.tistory.com/32