import Utility from './utility.js';
let utility = new Utility();

let nickName = user.userNickName;
let unreadChatMap = new Map();

$(document).ready(function () {
    console.log(user);
    console.log(plans);
    console.log(friends);
    console.log(friendRequests);
    console.log(promiseRequests);
    console.log(chatRooms);
    console.log(unreadChats);
    console.log(unReadChatRooms);
    console.log(unreadChatMap);
    chatAlarmRender();
    initEvent();
    onOffconnect();
    friendRequestConnect();
    promiseRequestConnect();
    roomConnect();
});
function initUnreadChatMap(){
    unreadChatMap.clear();

    for (let unreadChat of unreadChats) {
        if (!unreadChatMap.has(unreadChat.chatRoomNo)) {
            unreadChatMap.set(unreadChat.chatRoomNo, 1);
        } else {
            unreadChatMap.set(unreadChat.chatRoomNo, unreadChatMap.get(unreadChat.chatRoomNo) + 1);
        }
    }
}


let stompOnOffClient = null;
let friendsStatus = null;
let stompFriendRequestClient = null;
let stompPromiseRequestClient = null;
let stompRoomClient = null;
function onOffconnect() {
    let socket = new SockJS('/status');
    stompOnOffClient = Stomp.over(socket);
    stompOnOffClient.connect({}, function(frame) {
        // 연결 성공시 실행할 메서드
        stompOnOffClient.send("/app/status", {}, JSON.stringify({ nickName : nickName, status : 'on' }));

        stompOnOffClient.subscribe('/queue/onoff/' + nickName, function(msg) {
            friendsStatus = JSON.parse(msg.body);
            loginUserStatus(friendsStatus);
        });
        stompOnOffClient.subscribe('/topic/onoff', function(msg) {
            console.log('wait... friend');
            updateUserStatus(JSON.parse(msg.body));
        });
    },function (error){
        console.log(error);
    });
}

function friendRequestConnect() {
    let socket = new SockJS('/friend');
    stompFriendRequestClient = Stomp.over(socket);
    stompFriendRequestClient.connect({}, function(frame) {
        stompFriendRequestClient.subscribe('/queue/friend/' + nickName, function(msg) {
            friendRequests = JSON.parse(msg.body);
            renderFriendRequests();
        });
        stompFriendRequestClient.subscribe('/queue/accept/' + nickName, function(msg) {
            let responseData = JSON.parse(msg.body);
            friends = responseData.friends;
            friendRequests = responseData.friendRequests;
            renderFriends();
            renderFriendRequests();
        });

    },function (error){
        console.log(error);
    });
}

function promiseRequestConnect() {
    let socket = new SockJS('/promise');
    stompPromiseRequestClient = Stomp.over(socket);
    stompPromiseRequestClient.connect({}, function(frame) {
        stompPromiseRequestClient.subscribe('/queue/promise/' + nickName, function(msg) {
            promiseRequests = JSON.parse(msg.body);
            for(let promiseReq of promiseRequests){

                let startDateArr = promiseReq.planStartDate;
                let endDateArr = promiseReq.planEndDate;
                let startDate = new Date(startDateArr[0], startDateArr[1] - 1, startDateArr[2], startDateArr[3], startDateArr[4]);
                let endDate = new Date(endDateArr[0], endDateArr[1] - 1, endDateArr[2], endDateArr[3], endDateArr[4]);
                promiseReq.planStartDate = startDate.toISOString().slice(0, -5);
                promiseReq.planEndDate = endDate.toISOString().slice(0, -5);
            }
            renderPromiseRequests();
        });
        stompFriendRequestClient.subscribe('/queue/agree/' + nickName, function(msg) {
            console.log('promise agree websocket');
            let responseData = JSON.parse(msg.body);
            console.log(responseData);
            plans = responseData.plans;
            friendRequests = responseData.friendRequests;

            Swal.fire({
                title: '알림',
                text: '약속이 수락됐습니다. Bej의 Main으로 가주세요',
                icon: 'success',
                showConfirmButton: false,
                timer: 1000
            });
        });
    },function (error){
        console.log(error);
    });
}

function roomConnect() {
    let socket = new SockJS('/room');
    stompRoomClient = Stomp.over(socket);
    stompRoomClient.connect({}, function(frame) {

        stompRoomClient.subscribe('/queue/room/' + nickName, function(msg) {

            unReadChatRooms = JSON.parse(msg.body).unReadChatRooms;
            chatRooms = JSON.parse(msg.body).chatRooms;
            unreadChats = JSON.parse(msg.body).unreadChats;
            chatAlarmRender();
        });
    },function (error){
        console.log(error);
    });
}

function loginUserStatus(message) {
    $(".user-li").each(function() {
       let friendName = $(this).find(".user-i").text();
       let status = friendsStatus[friendName];

       let badgeClass = status === 'on' ? 'badge bg-light ml-auto ' : 'badge bg-gray ml-auto ';
       let badgeText = status === 'on' ? 'on' : 'off';

        $(this).append('<span class="' + badgeClass + '">' + badgeText +'</span>');
    });
}

function updateUserStatus(message) {
   let messageNickName = message.nickName;
   let messageStatus = message.status;

    $(".user-li").each(function() {
       let friendName = $(this).find(".user-i").text();

        if (friendName === messageNickName) {
           let currentStatus = friendsStatus[friendName];

            if (currentStatus !== messageStatus) {
                friendsStatus[friendName] = messageStatus;

               let badgeClass = messageStatus === 'on' ? 'badge bg-light ml-auto ' : 'badge bg-gray ml-auto';
               let badgeText = messageStatus === 'on' ? 'on' : 'off';

                $(this).find(".badge").removeClass('badge bg-light ml-auto on badge bg-gray ml-auto off').addClass(badgeClass).text(badgeText);
            }
        }
    });

    sortUserList();
    initEvent();
}


function initEvent() {

    $(".plus-icon").off('click').click(function () {
        $(".search-box").toggle();
    });

    $('#scheduleButton').off('click').click(function () {
        let friendNickName = $('.user-name').text().split('님')[0].trim();
        console.log('일정보기');
        openPopup('/bej/schedule?friend=' + friendNickName,800,800);

        $('#friendModal').modal('hide');
    });

    $('#promise-chooser li').off('click').click(function() {
        $('#promise-chooser li a').removeClass('selected');
        $(this).find("a").addClass('selected');
        let promiseChooserColor = $(this).find("a").css("color");
        $('#selectedColorReq').val(promiseChooserColor);
        console.log(promiseChooserColor);
    });

    $('#appointmentButton').off('click').click(function () {
        $('#friendModal').modal('hide');
        console.log('약속잡기');
        $('#addTitleReq').val('');
        $('#selectedColorReq').val('');
        $('#addStartDateReq').val('');
        $('#addEndDateReq').val('');
        $('#addTextAreaReq').val('');
        $('#promise-chooser a.selected').removeClass('selected');
        $('#promiseModal').modal('show');
    });

    $('#chatButton').off('click').click(function () {
        $('#friendModal').modal('hide');
        let clickedChatRoomNo = 0;
        let clickedName = $('.user-name').text().slice(0, -1).trim();

        for (let i = 0; i < chatRooms.length; i++) {
            let participantNickNames = Object.values(chatRooms[i].participants);

            if (participantNickNames.length == 2 &&
                participantNickNames.includes(clickedName) &&
                participantNickNames.includes(nickName)) {

                clickedChatRoomNo = chatRooms[i].chatRoomNo;
                break;
            }
        }
        if (clickedChatRoomNo > 0) {
            utility.ajax('/volka/chat/alarm',{roomNo : clickedChatRoomNo},'post')
                .then((responseData) => {
                    chatRooms = responseData.chatRooms
                    unreadChats = responseData.unreadChats
                    unReadChatRooms = responseData.unReadChatRooms
                    chatAlarmRender();
                    openPopup('/volka/chat?room='+clickedChatRoomNo, 600, 800);
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            utility.ajax('/volka/chat/create',{member: clickedName},'post')
                .then((responseData) => {
                    chatRooms = responseData.chatRooms
                    unreadChats = responseData.unreadChats
                    unReadChatRooms = responseData.unReadChatRooms
                    chatAlarmRender();
                    openPopup('/volka/chat?room='+responseData.chatRoomNo, 600, 800);
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    });

    $('#blockButton').off('click').click(function () {
        let userNickName = $('.user-name').text().split('님')[0].trim();
        utility.ajax('/friend/block', {nickName: userNickName}, 'post')
            .then((responseData) => {
                friends = responseData.friends;
                renderFriends();
            })
            .catch((error) => {
                console.log(error);
            });
        $('#friendModal').modal('hide');
    });

    /*$('#hideButton').off('click').click(function () {
        let userNickName = $('.user-name').text().split('님')[0].trim();
        utility.ajax('/friend/hide', {nickName: userNickName}, 'post')
            .then((responseData) => {
                friends = responseData.friends;

                renderFriends();
            })
            .catch((error) => {
                console.log(error);
            });
        $('#friendModal').modal('hide');
    });*/

    $('#promise-req').off('click').click(function () {
        console.log('promise ajax post');
        let userNickName = $('.user-name').text().split('님')[0].trim();
        let title = $('#addTitleReq').val();
        let selectedColor = $('#selectedColorReq').val();
        let startDate = $('#addStartDateReq').val();
        let endDate = $('#addEndDateReq').val();
        let content = $('#addTextAreaReq').val();

        let data = {
            title: title,
            color: selectedColor,
            planStartDate: startDate,
            planEndDate: endDate,
            content: content,
            friendName: userNickName
        };
        $('#promiseModal').modal('hide');
        utility.ajax('/promise/request', data, 'POST')
            .then((responseData) => {
                console.log(responseData);

                Swal.fire({
                    title: '알림',
                    text: responseData.message,
                    icon: 'success',
                    showConfirmButton: false,
                    timer: 800
                });
            })
            .catch((error) => {
                console.error('실패:', error);
            });
    });

    $('.user-li').off('click').on('click', function (event) {
        let modal = $('#friendModal .modal-dialog');
        modal.css('position', 'fixed');
        modal.css('left', event.pageX + 'px');
        modal.css('top', event.pageY + 'px');
        let clickedUserName = $(this).find('.user-i').text() + '님';
        $('.user-name').text(clickedUserName);
        $('#friendModal').modal("show");
    });

    $('#addFriendBtn').off('click').on('click', function () {
        console.log('친추');
        let existFriend = false;

        for(let i = 0; i < friends.length; i++){
            if(friends[i] == $('#addFriend').val()){
                existFriend = true;
                break;
            }
        }

        if(existFriend){
            Swal.fire({
                title: '실패',
                text: '이미 존재하는 친구입니다.',
                icon: 'error',
                showConfirmButton: false,
                timer: 800
            });
        }else if($('#addFriend').val() == nickName){
            Swal.fire({
                title: '실패',
                text: '자신에게는 친구 요청을 보낼수 없습니다.',
                icon: 'error',
                showConfirmButton: false,
                timer: 800
            });
        }else{
            utility.ajax('/friend/request', {nickName: $('#addFriend').val()}, 'post')
                .then((responseData) => {
                    if (responseData.status === 'success') {
                        Swal.fire({
                            title: '알림',
                            text: responseData.message,
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 800
                        });
                    } else if (responseData.status === 'fail') {
                        Swal.fire({
                            icon: 'error',
                            title: '실패',
                            text: responseData.message,
                        });
                    }

                    $('#addFriend').val('');
                })
                .catch((error) => {
                    console.log(error);
                });
        }

    });

    $(document).off('click', '.friend-req-accept').on('click', '.friend-req-accept', function () {

        let friendRequestElement = $(this).closest('.friend-requests');
        let selectFriendNo = friendRequestElement.find('.friend-no').val();
        let friendReqDTO = null;

        console.log(selectFriendNo);
        for(let friend of friendRequests){
            if(friend.friendNo == selectFriendNo){
                friendReqDTO = friend;
                break;
            }
        }

        console.log('수락');
        console.log(friendReqDTO);

        utility.ajax('/friend/accept', friendReqDTO, 'post')
            .then((responseData) => {
                friends = responseData.friends;
                friendRequests = responseData.friendRequests;

                renderFriends();
                renderFriendRequests();
            })
            .catch((error) => {
                console.log(error);
            });
    });

    $(document).off('click', '.promise-requests').on('click', '.promise-requests', function () {
        $('#addTitleRes').val('');
        $('#addStartDateRes').val('');
        $('#addEndDateRes').val('');
        $('#addTextAreaRes').val('');
        $('#modal-promise-no').val('');

        let selectPromiseNo = $(this).find('.promiseNo').val();
        let selectPromise = null;

        for (let promise of promiseRequests) {
            if (promise.promiseNo == selectPromiseNo) {
                selectPromise = promise;
                break;
            }
        }

        $('#modal-promise-no').val(selectPromiseNo);
        $('#addTitleRes').val(selectPromise.planTitle);
        $('#addStartDateRes').val(selectPromise.planStartDate);
        $('#addEndDateRes').val(selectPromise.planEndDate);
        $('#addTextAreaRes').val(selectPromise.planContent);

       $('#promiseResModal').show();
    });

    $(document).off('click', '#res-modal-close-btn').on('click', '#res-modal-close-btn', function () {
        $('#promiseResModal').hide();
    });

    $(document).off('click', '#promise-res-agree-btn').on('click', '#promise-res-agree-btn', function () {

        let promiseReq = null;
        for (let promise of promiseRequests) {

            if (promise.promiseNo == $('#modal-promise-no').val()) {
                promiseReq = promise;
                break;
            }
        }

        utility.ajax('/promise/accept', promiseReq, 'post')
            .then((responseData) => {
                plans = responseData.plans;
                promiseRequests = responseData.promiseRequests;
                renderPromiseRequests();
                let currentURL = window.location.href;

                if (currentURL.includes("bej/main")) {
                    history.go(0);
                }
            })
            .catch((error) => {
                console.log(error);
            });

        $('#promiseResModal').hide();
    });

    $(document).off('click', '#promise-res-reject-btn').on('click', '#promise-res-reject-btn', function () {

        let promiseReq = null;
        for (let promise of promiseRequests) {

            if (promise.promiseNo == $('#modal-promise-no').val()) {
                promiseReq = promise;
                break;
            }
        }
        utility.ajax('/promise/reject', promiseReq, 'post')
            .then((responseData) => {
                promiseRequests = responseData.promiseRequests;
                renderPromiseRequests();
            })
            .catch((error) => {
                console.log(error);
            });
        $('#promiseResModal').hide();
    });

    $(document).off('click', '.unread-chatrooms').on('click', '.unread-chatrooms', function () {
        console.log('들어옴?')
        let value = $(this).find('.chatroom-no').val();
        console.log(value);
        utility.ajax('/volka/chat/alarm',{roomNo : value},'post')
            .then((responseData) => {
                console.log('ajax');
                chatRooms = responseData.chatRooms
                unreadChats = responseData.unreadChats
                unReadChatRooms = responseData.unReadChatRooms
                chatAlarmRender();
                openPopup('/volka/chat?room='+value, 600, 800);
            })
            .catch((error) => {
                console.error(error);
            });
    });

    $("#profile-image").off('click').click(function(){
        $("#profile-modal").modal('show');
    });

    $("#change-profile").off('click').click(function(){
        $("#fileInput").click();
    });

    $("#fileInput").off('change').on('change', function(e){
        let file = e.target.files[0];
        let formData = new FormData();
        formData.append('file', file);
        let fileType = file["type"];
        let validImageTypes = ["image/jpg", "image/jpeg", "image/png"];


        if ($.inArray(fileType, validImageTypes) < 0) {
            Swal.fire({
                title: '알림',
                text: 'jpg 또는 png 형식의 이미지만 업로드 가능합니다.',
                icon: 'error',
                showConfirmButton: false,
                timer: 1000
            });
            return;
        }
        $.ajax({
            url: 'profile/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false
        })
            .done(function(responseData) {
                $("#profile-modal").modal('hide');
                window.location.reload();
            })
            .fail(function(error) {
                console.error(error);
            });
    });
}


    function renderFriends() {
        let friendsList = $('#friends-list');
        friendsList.empty();
        friends.forEach(function (friend) {
            let listItem = $('<li>').addClass('list-group-item d-flex justify-content-between align-items-center user-li').css('background-color', 'white');
            let image = $('<img>').attr('src', '/profile/image/' + friend).addClass('img-circle elevation-2 mr-3').css({'width': '33px', 'height': '33px'});
            let nameSpan = $('<span>').addClass('user-i').text(friend);
            listItem.append(image, nameSpan);
            friendsList.append(listItem);
        });

        stompOnOffClient.send("/app/status", {}, JSON.stringify({nickName: nickName, status: 'on'}));
    }

    function renderFriendRequests() {
        $('.friend-requests').remove();

        friendRequests.forEach(function (friend) {
            let friendReqDiv = $('<div class="friend-requests"></div>');
            let dropdownDivider = $('<div class="dropdown-divider"></div>');
            let reqName = $('<a href="#" class="dropdown-item req-name"></a>');

            reqName.html('<i class="fas fa-users mr-2"></i>' + friend.friendNickName + '님의 친구신청');
            reqName.append('<div class="float-right"><div class="btn-group"><button class="btn btn-outline-success btn-xs ml-2 mr-1 friend-req-accept">수락</button><button class="btn btn-outline-danger btn-xs friend-req-reject">거절</button></div></div>');
            let inputTag = $('<input type="hidden" class ="friend-no"/>').attr('value', friend.friendNo);
            reqName.prepend(inputTag);

            friendReqDiv.append(dropdownDivider);
            friendReqDiv.append(reqName);

            $('#friend-div').append(friendReqDiv);
        });

        initEvent();
        $('.req-num').text(friendRequests.length + '개의 알림');
        $('.req-count').text(friendRequests.length);
    }

    function renderPromiseRequests() {

        $('.promise-requests').remove();

        promiseRequests.forEach(function (promise) {
            let friendReqDiv = $('<div class="promise-requests"></div>');
            let dropdownDivider = $('<div class="dropdown-divider"></div>');
            let reqName = $('<a href="#" class="dropdown-item promise-name"></a>');

            reqName.html('<i class="fas fa-users mr-2"></i>' + promise.targetUser + '님의 약속요청');

            let promiseNoInput = $('<input class="promiseNo" type="hidden">');
            promiseNoInput.val(promise.promiseNo);
            reqName.append(promiseNoInput);

            reqName.append('<div class="float-right"><div class="btn-group"><button class="btn btn-outline-success btn-xs promise-request-btn">보기</button></div></div>');

            friendReqDiv.append(dropdownDivider);
            friendReqDiv.append(reqName);

            $('#promise-div').append(friendReqDiv);
        });

        $('.promise-num').text(promiseRequests.length + '개의 알림');
        $('.promise-count').text(promiseRequests.length);
        initEvent();
    }

    function sortUserList() {
        let renderFriendList = $("#friends-list");
        let onlineUsers = renderFriendList.find("li:has(.bg-light)").sort();
        let offlineUsers = renderFriendList.find("li:not(:has(.bg-light))").sort();

        renderFriendList.empty().append(onlineUsers).append(offlineUsers);
        initEvent();
    }

    function renderUnreadChatRooms() {
        $('.unread-chatrooms').remove();
        unReadChatRooms.forEach(function (unReadChatRoom) {
            let chatRoomDiv = $('<div class="unread-chatrooms"></div>');
            let dropdownDivider = $('<div class="dropdown-divider"></div>');
            let chatName = $('<a href="#" class="dropdown-item chat-name"></a>');
            let inputTag = $('<input type="hidden" class ="chatroom-no"/>').attr('value', unReadChatRoom.chatRoomNo);

            let messageCountSpan = $('<span class="float-right text-muted text-sm"></span>');
            messageCountSpan.text(`${unreadChatMap.get(unReadChatRoom.chatRoomNo)}개`);
            let ids = unReadChatRoom.chatRoomParticipants.split('|');
            let roomName = '';
            for(let i = 0; i < ids.length-1; i++){
                if(ids[i] != user.userId){
                    roomName += unReadChatRoom.participants[ids[i]];
                }
            }
            chatName.html('<i class="far fa-comment"></i> ' + roomName + ' 님과의 채팅방');
            chatName.append(messageCountSpan);
            chatName.prepend(inputTag);
            chatRoomDiv.append(dropdownDivider);
            chatRoomDiv.append(chatName);

            $('#msg-div').append(chatRoomDiv);
        });

        $('.msg-count').text(unreadChats.length);  // navbar 상단 알림 수 업데이트
        $('.msg-header').text(`${unreadChats.length}개의 알림`);  // dropdown 헤더 알림 수 업데이트
    }

    function chatAlarmRender(){
        initUnreadChatMap();
        renderUnreadChatRooms();
        initEvent();
    }
    function openPopup(url,width,height) {
        let _width = width +'';
        let _height = height + '';
        let _left = Math.ceil(( window.screen.width - _width )/2);
        let _top = Math.ceil(( window.screen.height - _height )/2);
        window.open(url, 'schedule', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );

    }

