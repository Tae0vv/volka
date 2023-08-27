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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

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
        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<Chat> unreadChats = chatRepository.findUnreadChats(userInfo);
        List<ChatDTO> unreadChatDTOList = new ArrayList<>();
        for(Chat chat : unreadChats){
            unreadChatDTOList.add(chat.entityToDto());
        }
        Iterator<ChatDTO> iterator = unreadChatDTOList.iterator();
        while(iterator.hasNext()){
            ChatDTO chatDTO = iterator.next();
            boolean isParticipated = false;
            for(ChatRoomDTO chatRoom : chatRooms){
                if(chatRoom.getChatRoomNo().equals(chatDTO.getChatRoomNo())){
                    isParticipated = true;
                    break;
                }
            }
            if(!isParticipated){
                iterator.remove(); // 현재 사용자가 참여하지 않은 채팅방의 메시지 삭제
            }
        }
        return unreadChatDTOList;
    }

    @Override
    public void sendChat(UserInfo userInfo, HashMap<String, String> chatMap) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(chatMap.get("roomNo"))).orElseThrow();
        Chat chat = Chat.builder()
                .chatContent(chatMap.get("content"))
                .chatRoomNo(chatRoom)
                .chatUserId(userInfo)
                .chatRead(0)
                .build();
        chatRepository.save(chat);
    }

    @Override
    public void readChats(UserInfo userInfo, List<ChatDTO> chatDTOList) {

        for(ChatDTO chatDTO : chatDTOList){
            if((chatDTO.getChatRead() == 0) && (!chatDTO.getChatUserId().equals(userInfo.getUserId()))){
                Chat chat = chatRepository.findById(chatDTO.getChatNo()).orElseThrow();
                chat.read();
                chatRepository.save(chat);
            }
        }
    }

}

