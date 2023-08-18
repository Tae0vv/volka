package com.project.volka.repository;

import com.project.volka.entity.Friend;
import com.project.volka.entity.Plan;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface PromiseRepository extends JpaRepository<Promise,Long> {

    List<Promise> findByMainUserAndPromiseStatus(UserInfo main, int promiseStatus);

}

