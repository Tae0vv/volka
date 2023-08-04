package com.project.volka.controller;

import com.project.volka.dto.PasswordDTO;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("setting")
public class SettingController {

    private final SettingService settingService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/info")
    public void changeInfoGet(@AuthenticationPrincipal User user){
        log.info(user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/password")
    public String changePasswordGet(@AuthenticationPrincipal User user){
        log.info(user);
        return "/setting/changepassword";
    }

    @PostMapping("/password")
    public String changePasswordPost(@AuthenticationPrincipal User user, PasswordDTO passwordDTO, RedirectAttributes redirectAttributes) throws Exception {

        try {
            settingService.changePw(passwordDTO,user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","password");
            return "redirect:/setting/password";
        }

        redirectAttributes.addFlashAttribute("result", "change");
        return "redirect:/user/login";
    }



}
