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
});