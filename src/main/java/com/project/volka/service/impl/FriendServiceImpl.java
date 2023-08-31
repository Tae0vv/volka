package com.project.volka.service.impl;

import com.project.volka.dto.FriendReqDTO;
import com.project.volka.entity.Friend;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.FriendRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public void requestFriendship(UserInfo userInfo, HashMap<String,String> friendMap) throws Exception{

        if(friendMap.containsKey("nickName")){
            String nickName = friendMap.get("nickName");
            UserInfo resUser = userRepository.findByUserNickName(nickName);

            Friend req = Friend.builder()
                    .mainUser(userInfo)
                    .targetUser(resUser)
                    .friendRelation(-1)
                    .build();
            friendRepository.save(req);

            Friend res = Friend.builder()
                    .mainUser(resUser)
                    .targetUser(userInfo)
                    .friendRelation(0)
                    .pairNo(req.getFriendNo())
                    .build();
            friendRepository.save(res);

            req.regPair(res.getFriendNo());
            friendRepository.save(req);
        }
    }

    @Override
    public void acceptFriendship(FriendReqDTO friendReqDTO) {
        Friend res = friendRepository.findById(friendReqDTO.getFriendNo()).orElseThrow(); //수락한사람
        Friend req = friendRepository.findById(res.getPairNo()).orElseThrow(); //친구요청한사람
        res.accept();
        req.accept();
        friendRepository.save(res);
        friendRepository.save(req);
        friendRepository.deduplication(req.getMainUser(), res.getMainUser());
    }


    @Override
    public void rejectFriendship(UserInfo userInfo, HashMap<String, String> friendMap) {
        if(friendMap.containsKey("nickName")){
            String nickName = friendMap.get("nickName");
            UserInfo reqUser = userRepository.findByUserNickName(nickName);

            Friend resFriend = friendRepository.findByMainUserAndTargetUser(userInfo, reqUser);
            Friend reqFriend = friendRepository.findByMainUserAndTargetUser(reqUser, userInfo);

            resFriend.reject();
            reqFriend.reject();

            friendRepository.save(resFriend);
            friendRepository.save(reqFriend);
        }
    }

    @Override
    public void hideFriendship(UserInfo userInfo, HashMap<String, String> friendMap) {
        if(friendMap.containsKey("nickName")){
            String nickName = friendMap.get("nickName");
            UserInfo targetUser = userRepository.findByUserNickName(nickName);

            Friend friend = friendRepository.findByMainUserAndTargetUser(userInfo, targetUser);

            friend.hide();

            friendRepository.save(friend);
        }
    }

    @Override
    public void blockFriendship(UserInfo userInfo, HashMap<String, String> friendMap) {
        if(friendMap.containsKey("nickName")){
            if(friendMap.containsKey("nickName")){
                String nickName = friendMap.get("nickName");
                UserInfo targetUser = userRepository.findByUserNickName(nickName);

                Friend main = friendRepository.findByMainUserAndTargetUser(userInfo, targetUser);
                Friend target = friendRepository.findByMainUserAndTargetUser(targetUser, userInfo);
                friendRepository.delete(main);
                friendRepository.delete(target);
            }
        }
    }

    @Override
    public List<String> getFriendsNickName(UserInfo userInfo, int relation) {

        List<Friend> friends =  friendRepository.findByMainUserAndFriendRelation(userInfo,relation);
        List<String> nickNameList = new ArrayList<>();

        for(Friend friend : friends){
            nickNameList.add(friend.getTargetUser().getUserNickName());
        }

        return nickNameList;
    }

    @Override
    public List<FriendReqDTO> getFriendRequests(UserInfo resUser) {

        List<Friend> friends =  friendRepository.findByMainUserAndFriendRelation(resUser,0);
        List<FriendReqDTO> friendReqDTOList = new ArrayList<>();

        for(Friend friend : friends){
            FriendReqDTO friendReqDTO = new FriendReqDTO();
            friendReqDTO.setFriendNo(friend.getFriendNo());
            friendReqDTO.setFriendNickName(friend.getTargetUser().getUserNickName());
            friendReqDTOList.add(friendReqDTO);
        }
        return friendReqDTOList;
    }


}
