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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MAIN_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo mainUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TARGET_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo targetUser;

    @Column(name = "FRIEND_RELATION" ,nullable = false)
    private int friendRelation;//-1최조에 걸었을때 건사람 0대기 1수락 2거절 3숨김 4차단

    @Column(name = "PAIR_NO")
    private Long pairNo;

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
    public void regPair(Long pairNo){
        this.pairNo = pairNo;
    }
}
