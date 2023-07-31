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
public class Chat extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_NO")
    private Long chatNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHAT_ROOM_NO" ,nullable = false, referencedColumnName = "CHAT_ROOM_NO")
    private ChatRoom chatRoomNo;

    @JoinColumn(name = "CHAT_USER_ID",nullable = false, referencedColumnName = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo chatUserNo;

    @Column(name = "CHAT_STRING",length = 5000, nullable = false)
    private String chatContent;

}
