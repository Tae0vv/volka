package com.project.volka.service.interfaces;


import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;

import java.util.HashMap;
import java.util.List;

public interface FriendService {

    void requestFriendship(UserInfo userInfo, HashMap<String,String> friendMap) throws Exception;
    void acceptFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    void rejectFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    void hideFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    void blockFriendship(UserInfo userInfo, HashMap<String,String> friendMap);
    List<String> getFriendsNickName(UserInfo userInfo, int relation);

}
