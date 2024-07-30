package com.ablez.jookbiren.security.userdetails;

import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.repository.UserRepository;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEp01 user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    log.error("error: {}", "없는 회원입니다.");
                    throw new NoSuchElementException("없는 회원입니다.");
                });
        return CustomUserDetails.of(user);
    }
}
