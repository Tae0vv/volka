package com.project.volka.repository;

import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long> {

    Friend findByMainUserAndTargetUser(UserInfo mainUser, UserInfo targetUser);
    List<Friend> findByMainUserAndFriendRelation(UserInfo main, int friendRelation);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friend f " +
            "WHERE (f.mainUser = ?1 AND f.targetUser = ?2 AND f.friendRelation IN (0, -1)) " +
            "OR (f.mainUser = ?2 AND f.targetUser = ?1 AND f.friendRelation IN (0, -1))")
    void deduplication(UserInfo mainUser, UserInfo targetUser);
}
