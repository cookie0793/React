package org.zerock.carddatebase.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.carddatebase.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 로그인을 제외한 다른 모든 요청들이 token을 가지고 있는지 검사
// token이 있는지 > token이 정상인지 > 요청 전달
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰이 있나 없나 확인
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 토큰이 있다면
        if(jws!=null){
            // 토큰을 비밀키로 해제해서 user정보를 추출
            String user = jwtService.getAuthUser(request);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, null, java.util.Collections.emptyList());

            // 시큐리티 인증저장소에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터 or 서블릿에 전송
        filterChain.doFilter(request, response);
    }
}
