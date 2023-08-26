package com.project.volka.controller;

import com.project.volka.dto.*;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.*;
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
    private final PlanService planService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public String home( @AuthenticationPrincipal User user,Model model){
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<PlanDTO> planList = planService.getPlanList(userInfo);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        List<FriendReqDTO> friendRequests = friendService.getFriendRequests(userInfo);
        List<PromiseReqDTO> promiseRequests = promiseService.getPromiseReqDTOList(userInfo, 0);
        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<ChatDTO> unreadChats = chatService.getUnreadChats(userInfo);
        List<ChatRoomDTO> unReadChatRooms = chatRoomService.getUnReadChatRooms(unreadChats);

        model.addAttribute("user", userInfo);
        model.addAttribute("planList", planList);
        model.addAttribute("friends", friends);
        model.addAttribute("friendRequests", friendRequests);
        model.addAttribute("promiseRequests", promiseRequests);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("unreadChats", unreadChats);
        model.addAttribute("unReadChatRooms", unReadChatRooms);

        return "/volka/home";
    }

}
