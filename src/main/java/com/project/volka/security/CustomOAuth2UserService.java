package com.project.volka.security;

import com.project.volka.entity.UserInfo;
import com.project.volka.entity.UserRole;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest....");
        log.info(userRequest);

        log.info("oauth2 user................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("name" + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName){
            case "kakao":
                email = getKaKaoEmail(paramMap);
                break;
        }

        log.info("============================");
        log.info(email);
        log.info("============================");

        return generateDTO(email, paramMap);
    }

    private UserSecurityDTO generateDTO(String email, Map<String,Object>params){

        Optional<UserInfo> result = userRepository.findByUserEmail(email);
        if(result.isEmpty()){
            UserInfo userInfo = UserInfo.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode("undefined"))
                    .userPhone("undefined")
                    .userNickName("undefined")
                    .userName("undefined")
                    .userEmail(email)
                    .userSocial(true)
                    .build();
            userInfo.addRole(UserRole.USER);
            userRepository.save(userInfo);
            UserSecurityDTO userSecurityDTO =
                    new UserSecurityDTO
                            (email,"undefined","undefined",
                            email,"undefined","undefined",
                            0,false,true,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            userSecurityDTO.setProps(params);

            return userSecurityDTO;
        }else{
            UserInfo userInfo = result.get();

            UserSecurityDTO userSecurityDTO =
                    new UserSecurityDTO(
                            userInfo.getUserId(),
                            userInfo.getUserPw(),
                            userInfo.getUserName(),
                            userInfo.getUserEmail(),
                            userInfo.getUserNickName(),
                            userInfo.getUserPhone(),
                            userInfo.getUserStatus(),
                            userInfo.isUserOn(),
                            userInfo.isUserSocial(),
                            userInfo.getRoleSet()
                                    .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                                    .collect(Collectors.toList())
                    );

            return userSecurityDTO;
        }
    }

    private String getKaKaoEmail(Map<String,Object> paramMap){
        log.info("KAKAO-----------------------------");
        Object value = paramMap.get("kakao_account");
        log.info(value);
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String)accountMap.get("email");
        log.info("email....." + email);
        return email;
    }

}
