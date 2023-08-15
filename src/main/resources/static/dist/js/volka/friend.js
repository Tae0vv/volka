import Utility from './utility.js';
const utility = new Utility();

let nickName = user.userNickName;

$(document).ready(function () {

    $('.user-li').off('click').on('click', function (event) {
        var modal = $('#friendModal .modal-dialog');
        modal.css('position', 'fixed');
        modal.css('left', event.pageX + 'px');
        modal.css('top', event.pageY + 'px');
        var clickedUserName = $(this).find('.user-i').text() + '님';
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
            updateUserStatus(JSON.parse(onoff.body));
        });
        stompOnOffClient.subscribe('/topic/onoff', function(onoff) {
            // 퍼블릭 토픽에 대한 처리
            console.log("다른사람이 로그인 했을때");
            updateUserStatus(JSON.parse(onoff.body));
        });
    },function (error){
        console.log(error);
    });
}

function updateUserStatus(message) {
    //li를 for문 돌면서 친구가 있으면 on 없으면 off로 변경하는 메서드
    console.log("메시지받음?")
    console.log(message);
}

