package com.project.volka.service.interfaces;

import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

public interface PromiseService {
    void makePromise(UserInfo userInfo, HashMap<String, Object> promiseInfo);
    List<PromiseReqDTO> getPromiseReqDTOList(UserInfo userInfo, int promiseStatus);
    void promiseAccept(@RequestBody PromiseReqDTO promiseReqDTO);
    void promiseReject(@RequestBody PromiseReqDTO promiseReqDTO);
}
