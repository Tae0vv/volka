package com.project.volka.dto;

import com.project.volka.entity.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class ChatDTO {

    private Long chatNo;
    private Long chatRoomNo;
    private String chatUserId;
    private String chatContent;
    private int chatRead;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public void entityToDTO(Chat chat){
        this.chatNo = chat.getChatNo();
        this.chatRoomNo = chat.getChatRoomNo().getChatRoomNo();
        this.chatUserId = chat.getChatUserId().getUserId();
        this.chatContent = chat.getChatContent();
        this.chatRead = chat.getChatRead();
        this.regDate = chat.getRegDate();
        this.modDate = chat.getModDate();
    }

}


