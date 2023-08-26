package com.project.volka.service.impl;


import com.project.volka.dto.ChatDTO;
import com.project.volka.dto.ChatRoomDTO;
import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.ChatRepository;
import com.project.volka.repository.ChatRoomRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.SettingService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<ChatRoomDTO> getChatRooms(String userId) {
        UserInfo userInfo = userRepository.findById(userId).orElseThrow();
        List<ChatRoom> allChatRooms = chatRoomRepository.findAll();
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();

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

        for(ChatRoom chatRoom : chatRooms){
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomNo(chatRoom.getChatRoomNo());
            chatRoomDTO.setChatRoomParticipants(chatRoom.getChatRoomParticipants());
            HashMap<String, String> map = new HashMap<>();

            String[] participantIds = chatRoomDTO.getChatRoomParticipants().split("\\|");
            for (String participantId : participantIds) {
                map.put(userRepository.findById(participantId).orElseThrow().getUserId(),userRepository.findById(participantId).orElseThrow().getUserNickName());
            }
            chatRoomDTO.setParticipants(map);
            chatRoomDTOList.add(chatRoomDTO);
        }

        return chatRoomDTOList;
    }

    @Override
    public ChatRoomDTO getChatRoom(Long roomNo) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomNo).orElseThrow();
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        HashMap<String, String> map = new HashMap<>();
        chatRoomDTO.setChatRoomNo(chatRoom.getChatRoomNo());
        chatRoomDTO.setChatRoomParticipants(chatRoom.getChatRoomParticipants());

        String[] participantIds = chatRoomDTO.getChatRoomParticipants().split("\\|");
        for (String participantId : participantIds) {
            map.put(userRepository.findById(participantId).orElseThrow().getUserId(),userRepository.findById(participantId).orElseThrow().getUserNickName());
        }
        chatRoomDTO.setParticipants(map);
        return chatRoomDTO;
    }

    @Override
    public List<ChatRoomDTO> getUnReadChatRooms(List<ChatDTO> unreadChatDTOList) {
        Set<ChatRoom> unReadChatRoomsSet = new HashSet<>();
        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();
        List<Chat> unreadChats = new ArrayList<>();

        for(ChatDTO chatDTO : unreadChatDTOList) {
            Chat chat = Chat.builder()
                    .chatNo(chatDTO.getChatNo())
                    .chatRoomNo(chatRoomRepository.findById(chatDTO.getChatRoomNo()).orElseThrow())
                    .chatUserId(userRepository.findById(chatDTO.getChatUserId()).orElseThrow())
                    .chatContent(chatDTO.getChatContent())
                    .chatRead(chatDTO.getChatRead())
                    .build();

            unreadChats.add(chat);
        }

        for (Chat chat : unreadChats) {
            ChatRoom chatRoom = chat.getChatRoomNo();
            unReadChatRoomsSet.add(chatRoom);
        }

        List<ChatRoom> chatRooms = new ArrayList<ChatRoom>(unReadChatRoomsSet);
        for(ChatRoom chatRoom : chatRooms){
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomNo(chatRoom.getChatRoomNo());
            chatRoomDTO.setChatRoomParticipants(chatRoom.getChatRoomParticipants());
            HashMap<String, String> map = new HashMap<>();

            String[] participantIds = chatRoomDTO.getChatRoomParticipants().split("\\|"); // 파이프를 기준으로 문자열 분할
            for (String participantId : participantIds) {
                map.put(userRepository.findById(participantId).orElseThrow().getUserId(),userRepository.findById(participantId).orElseThrow().getUserNickName());
            }
            chatRoomDTO.setParticipants(map);
            chatRoomDTOList.add(chatRoomDTO);
        }

        return chatRoomDTOList;

    }
}

