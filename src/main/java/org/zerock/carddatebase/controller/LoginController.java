package org.zerock.carddatebase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.carddatebase.domain.AccountCredentials;
import org.zerock.carddatebase.service.JwtService;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        // 인증에 알맞은 객체에 username/password 전달
        UsernamePasswordAuthenticationToken creds =
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword());

        // UserDetailsServiceImpl에 구현한 loadUserByUsername이 호출되어
        // 사용자의 인증을 DB와 확인하여 내부적으로 처리함
        Authentication auth = authenticationManager.authenticate(creds);

        // 토큰 발급
        String jwts = jwtService.getToken(auth.getName());

        // 클라이언트에 AUTHORIZATION 헤더에 토큰이 전달됨
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bitcamp" + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authrorization")
                .build();
    }
}
