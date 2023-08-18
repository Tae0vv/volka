package com.project.volka.service.interfaces;


import com.project.volka.dto.FriendReqDTO;
import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;

import java.util.HashMap;
import java.util.List;

public interface FriendService {

    void requestFriendship(UserInfo userInfo, HashMap<String,String> friendMap) throws Exception;
    void acceptFriendship(FriendReqDTO friendReqDTO);
    void rejectFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    void hideFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    void blockFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    List<String> getFriendsNickName(UserInfo userInfo, int relation);
    List<FriendReqDTO> getFriendRequests(UserInfo resUser);
}
