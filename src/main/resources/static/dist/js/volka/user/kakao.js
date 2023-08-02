$('#kakaoForm').submit(function(e) {
    let password1 = $('input[name="userPw"]').val();
    let password2 = $('input[id="userPwConfirm"]').val();
    let userName = $('input[name="userName"]').val();
    let userNickName = $('input[name="userNickName"]').val();
    let userPhone = $('input[name="userPhone"]').val();
    let agreeTerms = $('#agreeTerms').prop('checked');

    if (password1 === '' || password2 === '' || userName === '' || userNickName === '' || userPhone === '') {
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
    } else if (!agreeTerms) {
        e.preventDefault(); // 폼 제출을 막음
        Swal.fire({
            icon: 'error',
            title: '약관 동의 오류',
            text: '회원가입을 위해서는 약관에 동의해주세요.',
            confirmButtonText: '확인'
        });
    }
});