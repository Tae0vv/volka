package com.project.volka.dto;

import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
public class ChatRoomDTO {
    private Long chatRoomNo;
    private String chatRoomParticipants;
    private Map<String,String> participants;
}
