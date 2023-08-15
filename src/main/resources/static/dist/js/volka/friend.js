import Utility from './utility.js';
const utility = new Utility();

let nickName = user.userNickName;

$(document).ready(function () {

    $(".plus-icon").click(function() {
        $(".search-box").toggle();
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



    $('#addFriendBtn').on('click', function () {
        utility.ajax('/friend/request', {nickName : $('#addFriend').val()}, 'post')
            .then((responseData) => {
                if (responseData.status === 'success') {
                    // 친구 요청 성공시 처리
                    Swal.fire({
                        icon: 'success',
                        title: '성공',
                        text: responseData.message,
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

    // 친구 리스트의 아이템 클릭 시 메뉴 토글

    $('#scheduleButton').click(function() {
        console.log('일정보기');
        $('#friendModal').modal('hide');
    });

    $('#appointmentButton').click(function() {
        console.log('약속잡기');
        $('#friendModal').modal('hide');
    });

    $('#chatButton').click(function() {
        console.log('채팅');
        $('#friendModal').modal('hide');
    });

    $('#blockButton').click(function() {
        console.log('차단');
        $('#friendModal').modal('hide');
    });

    $('#hideButton').click(function() {
        console.log('숨김');
        $('#friendModal').modal('hide');
    });

    onOffconnect();

});

let stompOnOffClient = null;
let friendsStatus = null;

function onOffconnect() {
    let socket = new SockJS('/status');
    stompOnOffClient = Stomp.over(socket);
    stompOnOffClient.connect({}, function(frame) {
        // 연결 성공시 실행할 메서드
        console.log('Connected: ' + frame);
        stompOnOffClient.send("/app/status", {}, JSON.stringify({ nickName : nickName, status : 'on' }));
        stompOnOffClient.subscribe('/queue/onoff/' + nickName, function(onoff) {
            // 개인 토픽에 대한 처리
            console.log("로그인시");
            friendsStatus = JSON.parse(onoff.body);
            console.log(friendsStatus);
            loginUserStatus(JSON.parse(onoff.body));
        });
        stompOnOffClient.subscribe('/topic/onoff', function(onoff) {
            console.log("다른사람이 로그인하거나 로그아웃 했을때");
            updateUserStatus(JSON.parse(onoff.body));

        });
    },function (error){
        console.log(error);
    });
}

function loginUserStatus(message) {
    $(".user-li").each(function() {
       let friendName = $(this).find(".user-i").text();
       let status = friendsStatus[friendName]; // 친구의 온/오프 상태 가져오기

        // 상태에 따라 badge 클래스와 내용 설정
       let badgeClass = status === 'on' ? 'badge bg-light ml-auto ' : 'badge bg-gray ml-auto ';
       let badgeText = status === 'on' ? 'on' : 'off';

        // li 항목에 span 추가
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

                // 해당 li 내의 span 엘리먼트 업데이트
                $(this).find(".badge").removeClass('badge bg-light ml-auto on badge bg-gray ml-auto off').addClass(badgeClass).text(badgeText);
            }
        }
    });
}


