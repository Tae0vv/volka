package com.project.volka.repository;

import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByChatRoomNoOrderByChatNoDesc(ChatRoom chatRoom);
}
