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
public class Friend extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_NO")
    private Long friendNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIN_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo mainUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo targetUser;

    @Column(name = "FRIEND_RELATION" ,nullable = false)
    private int friendRelation;// 0대기 1수락 2거절 3숨김 4차단

    public void accept() {
        this.friendRelation = 1;
    }
    public void reject() {
        this.friendRelation = 2;
    }
    public void hide() {
        this.friendRelation = 3;
    }
    public void block() {
        this.friendRelation = 4;
    }
}
