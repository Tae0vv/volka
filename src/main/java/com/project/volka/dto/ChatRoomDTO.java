package com.project.volka.dto;

import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {

    private Long chatRoomNo;
    private String chatRoomParticipants;
}
