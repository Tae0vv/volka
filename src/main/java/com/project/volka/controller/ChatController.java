package com.project.volka.controller;

import com.project.volka.dto.*;
import com.project.volka.entity.Chat;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        chatService.readChats(userInfo,chats);

        model.addAttribute("user", userInfo);
        model.addAttribute("chats", chats);
        model.addAttribute("chatRoom", chatRoom);
        return "/volka/chat";
    }

    @PostMapping("/chat/alarm")
    @ResponseBody
    public  ResponseEntity<?> chatAlarm(@AuthenticationPrincipal User user,@RequestBody HashMap<String, String> chatMap){
        log.info("들어옴?");

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<ChatDTO> chats = chatService.getChats(Long.valueOf(chatMap.get("roomNo")));
        chatService.readChats(userInfo,chats);

        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<ChatDTO> unreadChats = chatService.getUnreadChats(userInfo);
        List<ChatRoomDTO> unReadChatRooms = chatRoomService.getUnReadChatRooms(unreadChats);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("chatRooms", chatRooms);
        responseData.put("unreadChats", unreadChats);
        responseData.put("unReadChatRooms", unReadChatRooms);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<?> planPost(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, String> chatMap) {
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        chatService.sendChat(userInfo,chatMap);
        List<ChatDTO> chats = chatService.getChats(Long.valueOf(chatMap.get("roomNo")));

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("chats", chats);

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }
}
