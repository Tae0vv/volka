
function ajax(url, data, type) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: url,
            data: JSON.stringify(data),
            type: type,
            dataType: "json",
            success: resolve,
            error: reject,
            contentType: "application/json" // 데이터를 JSON으로 보낼 때 Content-Type을 설정해줍니다.
        });
    });
}




// 위의 매서드 사용법
// ajax('서버주소/데이터요청경로', { key1: 'value1', key2: 'value2' }, 'GET')
//     .then((responseData) => {
//         console.log('성공:', responseData);
//     })
//     .catch((error) => {
//         console.error('실패:', error);
//     });