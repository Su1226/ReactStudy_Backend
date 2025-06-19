package com.korit.authstudy.Filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StudyFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("전처리");                        // Controller에 들어가기 전에 실행. (전처리)
        filterChain.doFilter(servletRequest, servletResponse);   // 다음 Filter를 실행하면서 Controller를 실행.
        // 해당 아래 줄 부터는 Controller 호출 이후이다. (후처리)
        System.out.println("후처리");
    }
}
    

