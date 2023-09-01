package com.project.volka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.volka.dto.*;
import com.project.volka.entity.Chat;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpMessagingTemplate simpMessagingTemplateChatAlarm;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("chat")
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

    @PostMapping("chat/alarm")
    @ResponseBody
    public  ResponseEntity<?> chatAlarm(@AuthenticationPrincipal User user,@RequestBody HashMap<String, String> chatMap){
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
    @PostMapping("read")
    @ResponseBody
    public  ResponseEntity<?> read(@AuthenticationPrincipal User user,@RequestBody HashMap<String, String> chatMap){

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
        simpMessagingTemplateChatAlarm.convertAndSend("/queue/room/" +  userInfo.getUserNickName(),responseData);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("chat")
    @ResponseBody
    public ResponseEntity<?> chat(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, String> chatMap) {
        log.info(chatMap);
        String participants = chatMap.get("participants");
        String[] participantsArray = participants.split("\\|");
        log.info(participantsArray[0]);
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        chatService.sendChat(userInfo,chatMap);
        List<ChatDTO> chats = chatService.getChats(Long.valueOf(chatMap.get("roomNo")));

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("chats", chats);

        for(int i = 0; i < participantsArray.length; i++){
            try{
                UserInfo chatParticipants = userService.getUserInfo(participantsArray[i]);

                List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(chatParticipants.getUserId());
                List<ChatDTO> unreadChats = chatService.getUnreadChats(chatParticipants);
                List<ChatRoomDTO> unReadChatRooms = chatRoomService.getUnReadChatRooms(unreadChats);

                HashMap<String, Object> alarmData = new HashMap<>();
                alarmData.put("chatRooms", chatRooms);
                alarmData.put("unreadChats", unreadChats);
                alarmData.put("unReadChatRooms", unReadChatRooms);
                simpMessagingTemplateChatAlarm.convertAndSend("/queue/room/" +  participantsArray[i],alarmData);

                ObjectMapper serverToClient = new ObjectMapper();
                serverToClient.registerModule(new JavaTimeModule());
                String chatsJson = serverToClient.writeValueAsString(chats);
                simpMessagingTemplate.convertAndSend("/queue/chat/" +  participantsArray[i],chatsJson);
            }catch (Exception e){
                log.error(e);
            }
        }
        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }
    @PostMapping("chat/create")
    @ResponseBody
    public ResponseEntity<?> chatCtreate(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, String> chatMap) {
        log.info("create");
        String participants = chatMap.get("member");
        String[] participantsArray = participants.split("\\|");
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        Long chatRoomNo = chatRoomService.makeChatRoom(userInfo,participantsArray);

        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<ChatDTO> unreadChats = chatService.getUnreadChats(userInfo);
        List<ChatRoomDTO> unReadChatRooms = chatRoomService.getUnReadChatRooms(unreadChats);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("chatRooms", chatRooms);
        responseData.put("unreadChats", unreadChats);
        responseData.put("unReadChatRooms", unReadChatRooms);
        responseData.put("chatRoomNo", chatRoomNo);

        for(int i = 0; i < participantsArray.length; i++){
            try{
                UserInfo chatParticipants = userService.getUserInfo(participantsArray[i]);

                List<ChatRoomDTO> socketChatRooms = chatRoomService.getChatRooms(chatParticipants.getUserId());
                List<ChatDTO> socketUnreadChats = chatService.getUnreadChats(chatParticipants);
                List<ChatRoomDTO> socketUnReadChatRooms = chatRoomService.getUnReadChatRooms(socketUnreadChats);

                HashMap<String, Object> alarmData = new HashMap<>();
                alarmData.put("chatRooms", socketChatRooms);
                alarmData.put("unreadChats", socketUnreadChats);
                alarmData.put("unReadChatRooms", socketUnReadChatRooms);
                simpMessagingTemplateChatAlarm.convertAndSend("/queue/room/" +  participantsArray[i],alarmData);
            }catch (Exception e){
                log.error(e);
            }
        }
        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }


}
