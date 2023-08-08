package com.project.volka.repository;

import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from UserInfo u where u.userId = :userId and u.userSocial = false")
    Optional<UserInfo> getWithRoles(@Param("userId") String userId);

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from UserInfo u where u.userId = :userId and u.userSocial = true")
    Optional<UserInfo> getWithRolesSocial(@Param("userId") String userId);

    @EntityGraph(attributePaths = "roleSet")
    Optional<UserInfo> findByUserEmail(String userEmail);

    UserInfo findByUserNickName(String nickName);

    @Modifying
    @Transactional
    @Query("update UserInfo u set u.userPw = :userPw where u.userId = :userId")
    void updatePassword(@Param("userId") String userId, @Param("userPw") String userPw);


}
