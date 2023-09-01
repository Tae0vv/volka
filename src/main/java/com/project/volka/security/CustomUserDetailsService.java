package com.project.volka.security;

import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

        Optional<UserInfo> result = userRepository.getWithRoles(username);

        if(result.isEmpty()){ //그냥 로그인이 없을때
            Optional<UserInfo> socialResult = userRepository.getWithRolesSocial(username);

            if(socialResult.isEmpty()){// 카카오 로그인에 대한 예외
                throw new UsernameNotFoundException("username not found");
            }
            return getUserDetails(socialResult);

        }else{ //그냥 로그인이 있을때
            return getUserDetails(result);
        }
    }

    private UserDetails getUserDetails(Optional<UserInfo> result) {
        UserInfo user = result.get();
        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(
                user.getUserId(),
                user.getUserPw(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserNickName(),
                user.getUserPhone(),
                user.getUserKeyword(),
                user.getUserStatus(),
                user.isUserOn(),
                false,
                user.getRoleSet()
                        .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                        .collect(Collectors.toList())

        );
        return userSecurityDTO;
    }
}
