package com.project.volka.controller;

import com.project.volka.dto.UserInfoDTO;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public String joinPost(UserInfoDTO userInfoDTO, RedirectAttributes redirectAttributes){

        log.info("join post");
        log.info(userInfoDTO);

        try {
            userService.signup(userInfoDTO);
        }catch (UserService.MidExistException e){

            redirectAttributes.addFlashAttribute("error","userId");
            return "redirect:/user/signup";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/bej/main";
    }
}
