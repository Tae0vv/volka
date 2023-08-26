package com.project.volka.service.impl;


import com.project.volka.dto.ChatDTO;
import com.project.volka.dto.ChatRoomDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.ChatRepository;
import com.project.volka.repository.ChatRoomRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.ChatService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatDTO> getChats(Long chatRoomNo) {
        List<Chat> chats = chatRepository.findByChatRoomNo(chatRoomRepository.findById(chatRoomNo).orElseThrow());
        List<ChatDTO> ChatDTOList = new ArrayList<>();
        for(Chat chat : chats){
            ChatDTOList.add(chat.entityToDto());
        }
        return ChatDTOList;
    }

    @Override
    public List<ChatDTO> getUnreadChats(UserInfo userInfo) {
        List<Chat> unreadChats = chatRepository.findUnreadChats(userInfo);
        List<ChatDTO> unreadChatDTOList = new ArrayList<>();
        for(Chat chat : unreadChats){
            unreadChatDTOList.add(chat.entityToDto());
        }
        return unreadChatDTOList;
    }

    @Override
    public void sendChat(ChatDTO chatDTO) {
        Chat chat = Chat.builder()
                .chatContent(chatDTO.getChatContent())
                .chatRoomNo(chatRoomRepository.findById(chatDTO.getChatRoomNo()).orElseThrow())
                .chatUserId(userRepository.findById(chatDTO.getChatUserId()).orElseThrow())
                .chatRead(chatDTO.getChatRead())
                .build();
        chatRepository.save(chat);
    }

}

