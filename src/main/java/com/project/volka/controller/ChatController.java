package com.project.volka.controller;

import com.project.volka.dto.*;
import com.project.volka.entity.Chat;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("volka")
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;
    private final PlanService planService;
    private final FriendService friendService;
    private final PromiseService promiseService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/chat")
    public String chatRoomIn(@AuthenticationPrincipal User user, @RequestParam("room") Long chatRoomNo, Model model){

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<ChatDTO> chats = chatService.getChats(chatRoomNo);
        ChatRoomDTO chatRoom = chatRoomService.getChatRoom(chatRoomNo);

        model.addAttribute("user", userInfo);
        model.addAttribute("chats", chats);
        model.addAttribute("chatRoom", chatRoom);
        return "/volka/chat";
    }
}
