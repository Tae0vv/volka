package com.project.volka.service.impl;


import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.ChatRepository;
import com.project.volka.repository.ChatRoomRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public List<ChatRoom> getChatRooms(String userId) {
        UserInfo userInfo = userRepository.findById(userId).orElseThrow();
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

        return chatRooms;
    }
}

