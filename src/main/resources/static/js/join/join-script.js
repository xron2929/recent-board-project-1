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

connect();
function connect() {
    let stompClient = null;
    let sessionId = setUrl();
    const socket = new SockJS('/my-websocket-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/'+sessionId+'/queue/messages', function(message) {
            alert("새로운 글이 작성되었습니다");
        });
    });
}
function setUrl() {
    let url = domainUri+"user/account";
    let accountData = {
        "method" : "GET"
    }
    let name;
    fetch(url,accountData).then(function findUsername(response) {
        return response.text();
    });
}
function JwtExpiredProcess() {
    location.href = domainUri+"jwt/expiration";
}
let count;
let isClick = false;
window.onload = function() {
    document.getElementById('joinButton').onclick = function sendPost() {
        setTimeout(() => {
            if (isClick === true) return;
            isClick = true;
            let url = domainUri + "join";
            let userId = document.getElementById('userId').value;
            let nickname = document.getElementById("nickname").value;
            let password = document.getElementById('password').value;
            let checkPassword = document.getElementById('checkPassword').value;
            let email = document.getElementById('userEmail').value;
            let emailCode = document.getElementById('checkEmail').value;
            let age = document.getElementById('age').value;
            if(isNaN(age) || age==="") {
                isClick = false;
                alert("입력 값은 숫자이어야 합니다");
                return;
            }
            let phoneNumber = document.getElementById('phoneNumber').value;
            let trans = document.getElementById("trans").value;
            if (password !== checkPassword) {
                isClick = false;
                alert("비밀번호가 다릅니다");
                return;
            }
            let data = {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    'userId': userId,
                    'nickname': nickname,
                    'password': password,
                    'email': email,
                    'trans': trans,
                    'emailCode': emailCode,
                    'age': age,
                    'phoneNumber': phoneNumber
                })
            }
            fetch(url, data).then(response => {
                if (response.ok) {
                    location.href = "/login";
                }
                return response.json();

            }).then(errorMsg => {
                isClick = false;

                alert(errorMsg.error);
            })
        }, 100)
    }
    document.getElementById('checkUserEmailButton').onclick = function eamilTransform() {
        let email = document.getElementById("userEmail").value;
        let params = {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            },
                // enctype : "multipart/form-data",
            body: JSON.stringify({
                email
            })
        }
        let url = domainUri + "email";
        fetch(url, params)
            .then(function (response) {
                    //  console.log(response.json());
                if (response.status === 500) {
                    throw new Error("데이터 없음");
                }
                return response.json();
            })
            .then(function (data) {
                let email = data;
                console.log(email);
                return;

            }).catch(function (e) {
                console.log(e);
            });
        document.getElementById("timer").textContent = "00:00";
        count = 181;
        time = setInterval("myTimer()", 1000);
    }

};


    function myTimer() {

        count = count - 1; // 타이머 선택 숫자에서 -1씩 감산함(갱신되기 때문)
        let minute = Math.floor(count / 60);
        if (minute < 10) {
            minute = "0" + minute;
        }
        let second = count % 60;
        if (second < 10) {
            second = "0" + second;
        }
        // console.log("m="+minute);
        // console.log("s="+second);
        console.log("count=" + count);
        document.getElementById("timer").innerHTML
            = minute + ":" + second;
        if (count == 0) {
            clearInterval(time);	// 시간 초기화
            alert("시간이 완료되었습니다.");
        }
    }