package com.project.volka.dto;

import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long chatNo;
    private ChatRoom chatRoomNo;
    private UserInfo chatUserNo;
    private String chatContent;
}
