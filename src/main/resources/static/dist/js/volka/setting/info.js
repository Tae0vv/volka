$('#infoChangeForm').submit(function(e) {
    let agreeTerms = $('#agreeTerms').prop('checked');

    if (!agreeTerms) {
        e.preventDefault(); // 폼 제출을 막음
        Swal.fire({
            icon: 'error',
            title: '약관 동의 오류',
            text: '회원정보 수정을 위해서는 약관에 동의해주세요.',
            confirmButtonText: '확인'
        });
    }
});