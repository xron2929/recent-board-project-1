// alert("?")
let domainUri;
const cookies = document.cookie.split(';'); // 모든 쿠키 가져오기
for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();
    if (cookie.startsWith('domainUrl=')) {
        domainUri = cookie.substring('domainURI='.length, cookie.length);
        console.log(domainUri);
        break;
    }
}
let jwtTimeData = {
    method:'GET'
}
fetch(domainUri+"jwt/time",jwtTimeData)
    .then((response) => {
        return response.text();
    }).then((data)=> {
    let isJwtExpired = Number(data);

    if(isJwtExpired === 0) {
        return JwtExpiredProcess();
    }
    if(isJwtExpired === -1) {
        return;
    }

    return setTimeout(JwtExpiredProcess,isJwtExpired)
})
function JwtExpiredProcess() {
    location.href = domainUri + "jwt/expiration";
}
let textRole = document.getElementById("noticeRole");
let params = {
    method: "get",
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
    }
}
const url = domainUri+"role";
fetch(url, params)
    .then(function (response) {
        if(response.status===500) {
            throw new Error("데이터 없음");
        }
        if(response.status===404) {
            throw new Error("클라이언트 데이터에 문제가 있을 수도 있습니다");
        }
        if(response.status===401) {
            throw new Error("클라이언트 데이터에 자격이 없습니다");
        }
        return response.json();

    })
    .then(function (data) {
        console.log(data.role);
        document.getElementById("noticeRole").textContent ='반갑습니다 당신의 권한은'+data.role+'입니다.';
        return;
    })
    .catch(function (e) {

        console.log(e);
    });