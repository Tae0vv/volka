package com.project.volka.repository;

import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long> {

    Friend findByMainUserAndTargetUser(UserInfo mainUser, UserInfo targetUser);
    List<Friend> findByMainUserAndFriendRelation(UserInfo main, int friendRelation);
}
