$('#signupForm').submit(function(e) {

    let password1 = $('input[name="userPw"]').val();
    let password2 = $('input[name="userPwConfirm"]').val();
    let userEmail = $('input[name="userEmail"]').val();
    let userId = $('input[name="userId"]').val();
    let userName = $('input[name="userName"]').val();
    let userNickName = $('input[name="userNickName"]').val();
    let userPhone = $('input[name="userPhone"]').val();

    if (password1 === '' || password2 === '' || userEmail === '' || userId === '' || userName === '' || userNickName === '' || userPhone === '') {
        e.preventDefault(); // 폼 제출을 막음
        Swal.fire({
            icon: 'error',
            title: '입력 오류',
            text: '모든 항목을 입력해주세요.',
            confirmButtonText: '확인'
        });
    } else if (password1 !== password2) {
        e.preventDefault(); // 폼 제출을 막음
        Swal.fire({
            icon: 'error',
            title: '비밀번호 오류',
            text: '입력한 비밀번호가 일치하지 않습니다.',
            confirmButtonText: '확인'
        });
    }
});
