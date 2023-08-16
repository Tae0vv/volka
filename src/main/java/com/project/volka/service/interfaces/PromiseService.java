package com.project.volka.service.interfaces;

import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;

import java.util.HashMap;

public interface PromiseService {
    Promise makePromise(UserInfo userInfo, HashMap<String, Object> promiseInfo);
}
