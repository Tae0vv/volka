
export default class Utility {
     ajax(url, data, type) {
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
}

