package org.zerock.carddatebase.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.zerock.carddatebase.component.AuthEntryPoint;
import org.zerock.carddatebase.filter.AuthenticationFilter;

/*
sping security 설정을 하게 되면
직접 서버의 주소로 접근하려고 하면 무조건, login화면으로 이동한다
user 사용자

CONSOLE창에 아래와 같이 출력된다.
Using generated security password: 543ec2aa-5507-4af2-b75b-c35c29b72a7f
*/

/* 스프링 시큐리티
1) 권한 부여(ROLL 부여)
   해당 서비스 접근 가능 여부
인증(Authentication) : 당신이 누구인지 증명해라, id/password
인가(Authorization) : 권한이 있으면 허용해줄게, ROLE(역할)
2) PASSWORD 암호화
3) 웹 보안에 취약, 보안 관련 기능 강화
...
*/


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint authEntryPoint;

    // 사용자인증을 위한 userDetailsService 설정/패스워드 암호화 알고리즘 설정
    // 암호를 DB에 저장하기 전에 BCrypt 암호화 처리
    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    // 인증 검사하는 객체를 Bean으로 생성
    @Bean
    public AuthenticationManager getauthenticationManager() throws Exception {
        return authenticationManager();
    }

    // 보안설정/주소 권한 허용 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf보안은 세션을 활용하는데 Rest서버는 세션을 사용하지 않으므로 disable
        http.csrf().disable()
                .sessionManagement()
                // Rest 서버는 세션 상태를 유지하지 않으므로 STATELESS
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // /login엔드포인트에 대한 POST요청은 접근을 허용함.
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 다른 요청은 인증 과정을 거쳐야 접근할 수 있다.
                .anyRequest().authenticated().and()
                // 잘못된 인증정보 요청 시, 오류 응답 처리를 authEntryPoint가 한다.
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint).and()
                // /login을 제외한 나머지 모든 요청은 필터를 통과해야 정상 응답을 받을 수 있다.
                .addFilterBefore(authenticationFilter,
                        UsernamePasswordAuthenticationToken.class);
    }

    @Autowired
    public void setAuthenticationFilter(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }
}
