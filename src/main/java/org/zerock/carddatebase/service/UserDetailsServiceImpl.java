package org.zerock.carddatebase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.carddatebase.domain.User;
import org.zerock.carddatebase.repository.UserRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // AuthenticationManager의 인증과정에서 호출되어 진다.
    // 이곳에서는 테이블의 사용자가 있는지를 확인하고,
    // 사용자id, password, role을 넣어줘야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername-" + username);
        Optional<User> user = userRepository.findByUsername(username);

        UserBuilder builder = null;
        // isPresent -> User테이블에 username이 존재한다.
        if(user.isPresent()) {
            // username, password, role을 builder에 전달
            User currentUser = user.get();
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(currentUser.getPassword());
            builder.roles(currentUser.getRole());
        }else {
            throw new UsernameNotFoundException("User not found : " + username);
        }
        return builder.build();
    }
}
