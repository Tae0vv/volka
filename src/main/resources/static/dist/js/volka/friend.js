import Utility from './utility.js';
const utility = new Utility();

let nickName = user.userNickName;


$(document).ready(function () {
    initEvent();
    onOffconnect();
    friendRequestConnect();
});

let stompOnOffClient = null;
let friendsStatus = null;

function onOffconnect() {
    let socket = new SockJS('/status');
    stompOnOffClient = Stomp.over(socket);
    stompOnOffClient.connect({}, function(frame) {
        // 연결 성공시 실행할 메서드
        stompOnOffClient.send("/app/status", {}, JSON.stringify({ nickName : nickName, status : 'on' }));

        stompOnOffClient.subscribe('/queue/onoff/' + nickName, function(onoff) {
            friendsStatus = JSON.parse(onoff.body);
            loginUserStatus(friendsStatus);
        });
        stompOnOffClient.subscribe('/topic/onoff', function(onoff) {
            console.log('wait... friend');
            updateUserStatus(JSON.parse(onoff.body));
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


let stompFriendRequestClient = null;

function friendRequestConnect() {
    let socket = new SockJS('/req');
    stompFriendRequestClient = Stomp.over(socket);
    stompFriendRequestClient.connect({}, function(frame) {
        stompFriendRequestClient.subscribe('/queue/req/' + nickName, function(onoff) {
            friendRequests = JSON.parse(onoff.body);
            renderFriendRequests();
        });
        stompFriendRequestClient.subscribe('/queue/accept/' + nickName, function(onoff) {
            friends = JSON.parse(onoff.body);
            console.log('수락완료');
            console.log(friends);
            renderFriends();
        });

    },function (error){
        console.log(error);
    });
}

function initEvent(){

    $(".plus-icon").off('click').click(function() {
        $(".search-box").toggle();
    });

    $('#scheduleButton').off('click').click(function() {
        let friendNickName = $('.user-name').text().split('님')[0].trim();
        console.log('일정보기');
        window.open('/bej/schedule?friend=' + friendNickName, '_blank');
        $('#friendModal').modal('hide');
    });


    $('#appointmentButton').off('click').click(function() {
        console.log('약속잡기');
        $('#addTitleReq').val('');
        $('#selectedColorReq').val('');
        $('#addStartDateReq').val('');
        $('#addEndDateReq').val('');
        $('#addTextAreaReq').val('');
        $('.fc-color-picker a.selected').removeClass('selected');
        $('#promiseModal').modal('show');
        $('#friendModal').modal('hide');
    });


    $('#chatButton').off('click').click(function() {
        console.log('채팅');
        $('#friendModal').modal('hide');
    });

    $('#blockButton').off('click').click(function() {
        let userNickName = $('.user-name').text().split('님')[0].trim();
        utility.ajax('/friend/block', {nickName : userNickName}, 'post')
            .then((responseData) => {
                friends = responseData.friends;

                renderFriends();
            })
            .catch((error) => {
                console.log(error);
            });
        $('#friendModal').modal('hide');
    });

    $('#hideButton').off('click').click(function() {
        let userNickName = $('.user-name').text().split('님')[0].trim();
        utility.ajax('/friend/hide', {nickName : userNickName}, 'post')
            .then((responseData) => {
                friends = responseData.friends;

                renderFriends();
            })
            .catch((error) => {
                console.log(error);
            });
        $('#friendModal').modal('hide');
    });

    $('#promise-req').on('click', function() {

        let userNickName = $('.user-name').text().split('님')[0].trim();
        const title = $('#addTitleReq').val();
        const selectedColor = $('#selectedColorReq').val();
        const startDate = $('#addStartDateReq').val();
        const endDate = $('#addEndDateReq').val();
        const content = $('#addTextAreaReq').val();

        const data = {
            title: title,
            color: selectedColor,
            planStartDate: startDate,
            planEndDate: endDate,
            content: content,
            friendName: userNickName
        };

        utility.ajax('/promise/request', data, 'POST')
            .then((responseData) => {
                console.log(responseData);
                Swal.fire({
                    title: '알림',
                    text: responseData.message,
                    icon: 'success',
                    showConfirmButton: false,
                    timer: 800
                })
            })
            .catch((error) => {
                console.error('실패:', error);
            });

        $('#addModal').modal('hide');
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
        utility.ajax('/friend/request', {nickName : $('#addFriend').val()}, 'post')
            .then((responseData) => {
                if (responseData.status === 'success') {
                    // 친구 요청 성공시 처리
                    Swal.fire({
                        title: '알림',
                        text: responseData.message,
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 800
                    })
                } else if (responseData.status === 'fail') {
                    // 친구 요청 실패시 처리
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
    });

    $(document).off('click', '.btn-outline-success').on('click', '.btn-outline-success', function () {

        let friendRequestElement = $(this).closest('.friend-requests');
        let elementText = friendRequestElement.find('.req-name').text();
        let friendNickName = elementText.split('님의 친구')[0].trim();

        utility.ajax('/friend/accept', {nickName : friendNickName}, 'post')
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

    $(document).off('click', '.btn-outline-danger').on('click', '.btn-outline-danger', function () {

        let friendRequestElement = $(this).closest('.friend-requests');
        let elementText = friendRequestElement.find('.req-name').text();
        let friendNickName = elementText.split('님의 친구')[0].trim();

        utility.ajax('/friend/reject', {nickName : friendNickName}, 'post')
            .then((responseData) => {
                friendRequests = responseData.friendRequests;
                console.log('render전');
                console.log(friendRequests);
                renderFriendRequests();
            })
            .catch((error) => {
                console.log(error);
            });

    });

}


function renderFriends(){
    let friendsList = $('#friends-list');
    friendsList.empty();

    friends.forEach(function(friend) {
        let listItem = $('<li>').addClass('list-group-item d-flex justify-content-between align-items-center user-li').css('background-color', 'white');
        let icon = $('<i>').addClass('fas fa-user mr-3').css('color', 'silver');
        let nameSpan = $('<span>').addClass('user-i').text(friend);
        listItem.append(icon, nameSpan);
        friendsList.append(listItem);
    });
    stompOnOffClient.send("/app/status", {}, JSON.stringify({ nickName : nickName, status : 'on' }));
}

function renderFriendRequests() {
    $('.friend-requests').remove();

    friendRequests.forEach(function(friend) {
        let friendReqDiv = $('<div class="friend-requests"></div>');
        let dropdownDivider = $('<div class="dropdown-divider"></div>');
        let reqName = $('<a href="#" class="dropdown-item req-name"></a>');

        reqName.html('<i class="fas fa-users mr-2"></i>' + friend + '님의 친구 신청');
        reqName.append('<div class="float-right"><div class="btn-group"><button class="btn btn-outline-success btn-xs mr-2">수락</button><button class="btn btn-outline-danger btn-xs">거절</button></div></div>');

        friendReqDiv.append(dropdownDivider);
        friendReqDiv.append(reqName);

        $('.dropdown-menu.dropdown-menu-lg.dropdown-menu-right').append(friendReqDiv);
    });

    initEvent();
    $('.req-num').text(friendRequests.length + '개의 알림');
    $('.req-count').text(friendRequests.length);
}

function sortUserList() {
    let $friendList = $("#friends-list");
    let $onlineUsers = $friendList.find("li:has(.bg-light)").sort();
    let $offlineUsers = $friendList.find("li:not(:has(.bg-light))").sort();

    $friendList.empty().append($onlineUsers).append($offlineUsers);
    initEvent();
}
