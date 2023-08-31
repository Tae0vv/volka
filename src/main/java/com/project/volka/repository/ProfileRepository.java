package com.project.volka.repository;

import com.project.volka.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    List<Profile> findByUserId(String userId);

    @Query(value = "SELECT * FROM profile p WHERE p.USER_ID = :userId AND p.PROFILE_NO = (SELECT MAX(PROFILE_NO) FROM Profile WHERE USER_ID = :userId)", nativeQuery = true)
    Profile getProfile(@Param("userId") String userId);

}
