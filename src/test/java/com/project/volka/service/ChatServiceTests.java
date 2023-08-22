package com.project.volka.service;

import com.project.volka.dto.ChatDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.ChatService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ChatServiceTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatService chatService;

    @Test
    public void getChatRooms(){
        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();
        List<ChatRoom> rooms = chatRoomService.getChatRooms(userInfo.getUserId());
        log.info(rooms);
    }

    @Test
    public void getChats(){
        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();
        List<ChatRoom> rooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<Chat> chats = chatService.getChat(rooms.get(0));
        log.info(chats);
    }

    @Test
    public void testSendChat() {
        // Mock data
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setChatContent("Hello! serviceTest");
        chatDTO.setChatRoomNo(1L);
        chatDTO.setChatUserId("volka");
        chatDTO.setChatRead(1);

        chatService.sendChat(chatDTO);
    }

}
