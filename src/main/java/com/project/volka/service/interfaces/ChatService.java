package com.project.volka.service.interfaces;

import com.project.volka.dto.ChatDTO;
import com.project.volka.dto.ChatRoomDTO;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;

import java.util.HashMap;
import java.util.List;

public interface ChatService {

    List<ChatDTO> getChats(Long chatRoomNo);
    List<ChatDTO> getUnreadChats(UserInfo userInfo);
    void sendChat(UserInfo userInfo, HashMap<String, String> chatDTO);
    void readChats(UserInfo userInfo, List<ChatDTO> chats);
}
