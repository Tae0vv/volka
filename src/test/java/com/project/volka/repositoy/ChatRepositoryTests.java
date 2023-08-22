package com.project.volka.repositoy;

import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Log4j2
public class ChatRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ModelMapper modelMapper;




    @Test
    public void sendChat(){
        ChatRoom chatRoom = chatRoomRepository.findById(1L).orElseThrow();
        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();
        UserInfo userInfo2 = userRepository.findById("volka").orElseThrow();


        Chat chat = Chat.builder()
                .chatRoomNo(chatRoom)
                .chatUserNo(userInfo2)
                .chatContent("안녕 22")
                .chatRead(1)
                .build();

        chatRepository.save(chat);
    }

    @Test
    public void getChatInfo(){
        ChatRoom chatRoom = chatRoomRepository.findById(1L).orElseThrow();
        List<Chat> chats = chatRepository.findByChatRoomNoOrderByChatNoDesc(chatRoom);
        log.info(chats);
    }



}
