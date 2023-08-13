import Utility from './utility.js';
const utility = new Utility();

$(document).ready(function () {
    $('.plus-icon').on('click', function () {
        $(this).toggleClass('active');
        $('.search-box').slideToggle();
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
    $('ul.list-group').on('click', 'li.list-group-item', function() {
        $(this).find('.friend-menu').toggle();
    });

    $('#scheduleButton').click(function() {
        // '일정보기' 버튼이 클릭되었을 때 수행할 동작
        // 예를 들어, 일정 정보를 표시하는 로직을 추가
    });

    $('#appointmentButton').click(function() {
        // '약속잡기' 버튼이 클릭되었을 때 수행할 동작
        // 예를 들어, 약속 잡는 기능을 구현하는 로직을 추가
    });

    $('#chatButton').click(function() {
        // '채팅' 버튼이 클릭되었을 때 수행할 동작
        // 예를 들어, 채팅창을 열고 관련 로직을 추가
    });

    $('#blockButton').click(function() {
        // '차단' 버튼이 클릭되었을 때 수행할 동작
        // 예를 들어, 사용자를 차단하는 로직을 추가
    });

    $('#hideButton').click(function() {
        // '숨김' 버튼이 클릭되었을 때 수행할 동작
        // 예를 들어, 사용자를 목록에서 숨기는 로직을 추가
    });

});
