package com.project.volka.service;

import com.project.volka.dto.ChatDTO;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.ChatService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
public class ChatServiceTests {

    @Autowired
    private ChatService chatService;




    @Test
    public void testSendChat() {
        // Mock data
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setChatContent("Hello2 unread Test");
        chatDTO.setChatRoomNo(2L);
        chatDTO.setChatUserId("hello");
        chatDTO.setChatRead(0);

        chatService.sendChat(chatDTO);
    }



}
