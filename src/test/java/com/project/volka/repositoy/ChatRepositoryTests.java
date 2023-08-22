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
import java.util.ArrayList;
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
    public void createChatRoom(){
        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();

        List<ChatRoom> allChatRooms = chatRoomRepository.findAll();
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (ChatRoom chatRoom : allChatRooms) {
            String participants = chatRoom.getChatRoomParticipants();
            if (participants != null) {
                String[] participantIds = participants.split("\\|"); // 파이프를 기준으로 문자열 분할
                for (String participantId : participantIds) {
                    if (participantId.equals(userInfo.getUserId())) {
                        chatRooms.add(chatRoom);
                        break;
                    }
                }
            }
        }

        log.info(chatRooms);
    }

    @Test
    public void sendChat(){
        ChatRoom chatRoom = chatRoomRepository.findById(1L).orElseThrow();
        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();
        UserInfo userInfo2 = userRepository.findById("volka").orElseThrow();

        Chat chat = Chat.builder()
                .chatRoomNo(chatRoom)
                .chatUserId(userInfo2)
                .chatContent("안녕 난 volka야")
                .chatRead(1)
                .build();

        chatRepository.save(chat);
    }

    @Test
    public void getChatInfo(){
        ChatRoom chatRoom = chatRoomRepository.findById(1L).orElseThrow();
        List<Chat> chats = chatRepository.findByChatRoomNo(chatRoom);
        log.info(chats);
    }



}
