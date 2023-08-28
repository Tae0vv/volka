package com.project.volka.controller;

import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.service.interfaces.UserService;
import com.project.volka.utility.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;


    @GetMapping("/login")
    public void loginGet(String error, String logout){
        log.info("login get..........");
        log.info("logout: " + logout);

        if(logout != null){
            log.info("user logout..........");
        }
    }

    @GetMapping("/signup")
    public void signupGet(){
        log.info("signup get.....");
    }

    @PostMapping("/signup")
    public String signupPost(UserInfoDTO userInfoDTO, RedirectAttributes redirectAttributes){

        log.info("signup post");
        log.info(userInfoDTO);

        try {
            userService.signup(userInfoDTO);
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("error","userId");
            return "redirect:/user/signup";
        }

        redirectAttributes.addFlashAttribute("result", "signup");
        return "redirect:/user/login";
    }

    @GetMapping("/forgotid")
    public void forgotIdGet(){
        log.info("forgotIdGet get");
    }

    @PostMapping("/forgotid")
    public String forgotIdPost(UserInfoDTO userInfoDTO){

        log.info("forgotIdPost");
        mailService.sendId(userInfoDTO);

        return "redirect:/user/login";
    }

    @GetMapping("/forgotpw")
    public void forgotpw(){
        log.info("forgotpw");
    }

    @PostMapping("/forgotpw")
    public String forgotPwPost(UserInfoDTO userInfoDTO){

        log.info("forgotPwPost");
        mailService.sendTempPw(userInfoDTO);

        return "redirect:/user/login";
    }
    @GetMapping("/kakao")
    public void kakaoGet(){
        log.info("kakao signup-----");
    }

    @PostMapping("/kakao")
    public String kakaoPost(UserInfoDTO userInfoDTO, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user){
        log.info("kakao signup------");

        userInfoDTO.setUserId(user.getUsername());
        userService.kakaoAddInfo(userInfoDTO);

        return "redirect:/user/login";
    }

}
