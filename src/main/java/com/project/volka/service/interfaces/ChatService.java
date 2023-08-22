package com.project.volka.service.interfaces;

import com.project.volka.dto.ChatDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;

import java.util.List;

public interface ChatService {

    List<Chat> getChat(ChatRoom chatRoom);

    void sendChat(ChatDTO chatDTO);

}
