package com.project.volka.controller;

import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("volka")
@RequiredArgsConstructor
public class VolkaController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public String home( @AuthenticationPrincipal User user,Model model){
        log.info(user);
        UserInfo userinfo = userService.updateUserInfo((UserSecurityDTO) user);

        model.addAttribute("user", userinfo);
        return "/volka/home";
    }

}
