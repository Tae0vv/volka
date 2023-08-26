package com.project.volka.service.interfaces;

import com.project.volka.dto.ChatDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;

import java.util.List;

public interface ChatService {

    List<ChatDTO> getChats(Long chatRoomNo);
    List<ChatDTO> getUnreadChats(UserInfo userInfo);
    void sendChat(ChatDTO chatDTO);

}
