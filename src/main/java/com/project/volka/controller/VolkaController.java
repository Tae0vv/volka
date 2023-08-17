package com.project.volka.controller;

import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.PromiseService;
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

import java.util.List;

@Controller
@Log4j2
@RequestMapping("volka")
@RequiredArgsConstructor
public class VolkaController {

    private final UserService userService;
    private final FriendService friendService;
    private final PromiseService promiseService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public String home( @AuthenticationPrincipal User user,Model model){
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        List<String> friendRequests = friendService.getFriendsNickName(userInfo, 0);
        List<PromiseReqDTO> promiseRequests = promiseService.getPromiseReqDTOList(userInfo, 0);

        log.info("volka requests ");
        log.info(promiseRequests);
        model.addAttribute("user", userInfo);
        model.addAttribute("friends", friends);
        model.addAttribute("friendRequests", friendRequests);
        model.addAttribute("promiseRequests", promiseRequests);

        return "/volka/home";
    }

}
