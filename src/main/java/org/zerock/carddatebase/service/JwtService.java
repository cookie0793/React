package org.zerock.carddatebase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    // 토큰의 유효기간
    static final long EXPIRATION_TIME = 60 * 60 * 24 * 1; // 1일 = 86400000;
    static final String PREFIX = "Bitcamp";     // 토큰을 빨리 찾기 위한 함께 붙여주는 문자열
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 비밀키로 서명된 JWT 토큰 발급
    public String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();

        return token;
    }

    // 클라이언트가 보내온 요청 헤더에서, 토큰을 확인하고 사용자 이름으로 전환함
    public String getAuthUser(HttpServletRequest request){

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰이 헤더에 존재한다면
        if(token != null){
            String user = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();

                    // token을 비밀키로 풀었을 때 user가 잘 추출되면
                    if(user != null)
                        return user;
        }

        return null;
    }
}
