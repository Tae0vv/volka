package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ROOM_NO")
    private Long chatRoomNo;

    @Column(name = "CHAT_ROOM_PARTICIPANTS")
    private String chatRoomParticipants;
}
