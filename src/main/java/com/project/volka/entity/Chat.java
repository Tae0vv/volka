package com.project.volka.entity;

import com.project.volka.dto.ChatDTO;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHAT_ROOM_NO" ,nullable = false, referencedColumnName = "CHAT_ROOM_NO")
    private ChatRoom chatRoomNo;

    @JoinColumn(name = "CHAT_USER_ID",nullable = false, referencedColumnName = "USER_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserInfo chatUserId;

    @Column(name = "CHAT_CONTENT",length = 5000, nullable = false)
    private String chatContent;

    @Column(name = "CHAT_READ")
    private int chatRead;

    public void read() {
        this.chatRead = chatRead +1;
    }

    public ChatDTO entityToDto(){
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setChatNo(this.chatNo);
        chatDTO.setChatRoomNo(this.getChatRoomNo().getChatRoomNo());
        chatDTO.setChatUserId(this.getChatUserId().getUserId());
        chatDTO.setChatContent(this.getChatContent());
        chatDTO.setChatRead(this.getChatRead());
        chatDTO.setRegDate(this.getRegDate());
        chatDTO.setModDate(this.getModDate());
        return chatDTO;
    }
}
