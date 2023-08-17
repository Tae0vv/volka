import Utility from './utility.js';
let utility = new Utility();

let nickName = user.userNickName;

$(document).ready(function () {
    console.log("프로미스");
    console.log(promiseRequests);
    initEvent();
    onOffconnect();
    friendRequestConnect();
    promiseRequestConnect();
});

let stompOnOffClient = null;
let friendsStatus = null;

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

let stompFriendRequestClient = null;
function friendRequestConnect() {
    let socket = new SockJS('/friend');
    stompFriendRequestClient = Stomp.over(socket);
    stompFriendRequestClient.connect({}, function(frame) {
        stompFriendRequestClient.subscribe('/queue/friend/' + nickName, function(msg) {
            friendRequests = JSON.parse(msg.body);
            renderFriendRequests();
        });
        stompFriendRequestClient.subscribe('/queue/accept/' + nickName, function(msg) {
            friends = JSON.parse(msg.body);
            console.log('수락완료');
            console.log(friends);
            renderFriends();
        });

    },function (error){
        console.log(error);
    });
}

let stompPromiseRequestClient = null;
function promiseRequestConnect() {
    let socket = new SockJS('/promise');
    stompPromiseRequestClient = Stomp.over(socket);
    stompPromiseRequestClient.connect({}, function(frame) {
        stompPromiseRequestClient.subscribe('/queue/promise/' + nickName, function(msg) {
            promiseRequests = JSON.parse(msg.body);
            renderPromiseRequests();
        });
        stompFriendRequestClient.subscribe('/queue/agree/' + nickName, function(msg) {
            //data가 생겨서 calendar render가 되어야함
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
        window.open('/bej/schedule?friend=' + friendNickName, '_blank');
        $('#friendModal').modal('hide');
    });

    $('.fc-color-picker a').off('click').click(function () {
        $('.fc-color-picker a.selected').removeClass('selected');
        $(this).addClass('selected');
        let selectedColor = $(this).css('color');
        $('#selectedColorReq').val(selectedColor);
    });

    $('#appointmentButton').off('click').click(function () {
        $('#friendModal').modal('hide');
        console.log('약속잡기');
        $('#addTitleReq').val('');
        $('#selectedColorReq').val('');
        $('#addStartDateReq').val('');
        $('#addEndDateReq').val('');
        $('#addTextAreaReq').val('');
        $('.fc-color-picker a.selected').removeClass('selected');
        $('#promiseModal').modal('show');
    });

    $('#chatButton').off('click').click(function () {
        console.log('채팅');
        $('#friendModal').modal('hide');
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

    $('#hideButton').off('click').click(function () {
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
    });

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
        utility.ajax('/friend/request', {nickName: $('#addFriend').val()}, 'post')
            .then((responseData) => {
                if (responseData.status === 'success') {
                    // 친구 요청 성공시 처리
                    Swal.fire({
                        title: '알림',
                        text: responseData.message,
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 800
                    });
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

    $(document).off('click', '.friend-req-accept').on('click', '.friend-req-accept', function () {

        let friendRequestElement = $(this).closest('.friend-requests');
        let elementText = friendRequestElement.find('.req-name').text();
        let friendNickName = elementText.split('님의 친구')[0].trim();

        utility.ajax('/friend/accept', {nickName: friendNickName}, 'post')
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

    $(document).off('click', '#promise-request-alarm').on('click', '#promise-request-alarm', function () {
        console.log('일정누름');

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
        let promiseReq = "";
        for (let promise of promiseRequests) {

            if (promise.promiseNo == $('#modal-promise-no').val()) {
                promiseReq = promise;
                break;
            }
        }

        utility.ajax('/promise/accept', promiseReq, 'post')
            .then((responseData) => {
                //받을 값 plans, promiseRequest
                plans = responseData.plans;
                friendRequests = responseData.friendRequests;

                renderFriendRequests();
                // 달력 랜더 새로하기
            })
            .catch((error) => {
                console.log(error);
            });

        $('#promiseResModal').hide();

    });

    $(document).off('click', '#promise-res-reject-btn').on('click', '#promise-res-reject-btn', function () {
        $('#promiseResModal').hide();
    });


}
    function renderFriends() {
        let friendsList = $('#friends-list');
        friendsList.empty();

        friends.forEach(function (friend) {
            let listItem = $('<li>').addClass('list-group-item d-flex justify-content-between align-items-center user-li').css('background-color', 'white');
            let icon = $('<i>').addClass('fas fa-user mr-3').css('color', 'silver');
            let nameSpan = $('<span>').addClass('user-i').text(friend);
            listItem.append(icon, nameSpan);
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

            reqName.html('<i class="fas fa-users mr-2"></i>' + friend + '님의 친구 신청');
            reqName.append('<div class="float-right"><div class="btn-group"><button class="btn btn-outline-success btn-xs mr-2 friend-req-accept">수락</button><button class="btn btn-outline-danger btn-xs friend-req-reject">거절</button></div></div>');

            friendReqDiv.append(dropdownDivider);
            friendReqDiv.append(reqName);

            $('.dropdown-menu.dropdown-menu-lg.dropdown-menu-right').append(friendReqDiv);
        });

        initEvent();
        $('.req-num').text(friendRequests.length + '개의 알림');
        $('.req-count').text(friendRequests.length);
    }

    function renderPromiseRequests() {
        $('.promise-requests').remove();
        console.log('프로미스 랜더');
        console.log(promiseRequests);
        promiseRequests.forEach(function (promise) {
            let friendReqDiv = $('<div class="promise-requests"></div>');
            let dropdownDivider = $('<div class="dropdown-divider"></div>');
            let reqName = $('<a href="#" class="dropdown-item promise-name"></a>');

            reqName.html('<i class="fas fa-users mr-2"></i>' + promise.targetUser + '님의 약속 요청');

            let promiseNoInput = $('<input class="promiseNo" type="hidden">');
            promiseNoInput.val(promise.promiseNo);
            reqName.append(promiseNoInput);

            reqName.append('<div class="float-right"><div class="btn-group"><button class="btn btn-outline-success btn-xs promise-request-btn">보기</button></div></div>');

            friendReqDiv.append(dropdownDivider);
            friendReqDiv.append(reqName);

            $('.dropdown-menu.dropdown-menu-lg.dropdown-menu-right').append(friendReqDiv);
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
