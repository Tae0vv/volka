package com.project.volka.dto;

import com.project.volka.entity.*;
import com.project.volka.repository.ChatRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.UserService;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ChatDTO {

    private Long chatNo;
    private Long chatRoomNo;
    private String chatUserId;
    private String chatContent;
    private int chatRead;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}


