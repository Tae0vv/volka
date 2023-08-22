package com.project.volka.dto;

import lombok.*;

import javax.persistence.*;

@Data
public class ChatRoomDTO {
    private Long chatRoomNo;
    private String chatRoomParticipants;
}
