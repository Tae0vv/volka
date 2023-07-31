package com.project.volka.security.handler;

import com.project.volka.security.dto.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public  void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        log.info("-----------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess........");
        log.info(authentication.getPrincipal());

        UserSecurityDTO userSecurityDTO = (UserSecurityDTO) authentication.getPrincipal();
        String encodedPw = userSecurityDTO.getUserPw();

        //소셜로그인이고 회원의 pw가 undefined
        if (userSecurityDTO.isUserSocial()
                && (userSecurityDTO.getUserPw().equals("undefined")
                || passwordEncoder.matches("undefined", userSecurityDTO.getUserPw())
        )) {
            log.info("should change userInfo");
            log.info("redirect to member modify");

            response.sendRedirect("/user/kakao");
        }else{
            response.sendRedirect("/bej/main");
        }
    }
}
