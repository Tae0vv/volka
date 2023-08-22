package com.project.volka.service.interfaces;

import com.project.volka.dto.PlanDTO;
import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

public interface ChatRoomService {

    List<ChatRoom> getChatRooms(String userId);

}
