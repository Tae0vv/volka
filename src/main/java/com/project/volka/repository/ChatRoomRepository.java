package com.project.volka.repository;

import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
