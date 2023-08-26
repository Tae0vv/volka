package com.project.volka.repository;

import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByChatRoomNo(ChatRoom chatRoom);
    @Query("SELECT c FROM Chat c WHERE c.chatRead = 0 AND c.chatUserId != :user")
    List<Chat> findUnreadChats( @Param("user") UserInfo user);
}
