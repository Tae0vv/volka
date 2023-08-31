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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_NO")
    private Long profileNo;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PATH")
    private String filePath;


    public void changeFilePath(String filePath){
        this.filePath = filePath;
    }
}
