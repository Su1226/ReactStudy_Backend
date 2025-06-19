package com.korit.authstudy.security.filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JWT Access Token 검사");
        // doFilter가 실행되기 전에 JWT Token을 검사해줘야 한다.
        // 로그인이 되어야 token이 만들어지기 때문에, 로그인은 거부해서는 안된다.
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
