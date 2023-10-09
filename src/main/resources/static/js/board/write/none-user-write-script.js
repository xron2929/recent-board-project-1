//.ignoringAntMatchers 이걸로 시큐리티할 떄 일부 링크 제거 해서 동작할꺼임 나중에
// board 2끝나고 3에 등록하고 끝나고 보니 둘다 board 3,4인 경우
// 둘 다 board 3으로 등록되어도, 어차피 board 4지우면 이미지만 남고 board만 지워지는 거라
// 이미지가 이상하게 짤릴 염려는 없음

let jwtTimeData = {
    method:'GET'
}
fetch("/jwt/time",jwtTimeData)
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
    location.href =  "/jwt/expiration";
}

let finalId;
let stompClient;
let sessionId;
setUrl().then(function(data) {
    sessionId = data;
}).then(function() {
    connect();
});

function connect() {
    let stompClient = null;

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
    let url = "/user-noneuser/account";
    let accountData = {
        "method" : "GET",
        headers : {
            "Content-Type": "application/json"
        }

    }

    return fetch(url,accountData).then(function findUsername(response) {
        return response.json();
    }).then(function (response) {
        return response.userId;
    })
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}
replace2();
function replace2() {
    console.log("??");
    let requestData = {
        method: "GET",

        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }
        // enctype : "multipart/form-data",
    };
    fetch("/board/finalId", requestData)
        .then(function (response) {
            let data = response.json();
            return data;
        })
        .then(function(data) {
            console.log(data);
            finalId = data;
        })

        .then(
            function loadCkeditor() {
            CKEDITOR.replace('contents',{
                filebrowserUploadUrl: '/image/upload/'+finalId,
                font_names : "맑은 고딕/Malgun Gothic;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;MS Mincho/MS Mincho;Trebuchet MS/Trebuchet MS;Verdana/Verdana",
                font_defaultLabel : "맑은 고딕/Malgun Gothic",
                fontSize_defaultLabel : "12",
                skin : "office2013",
                language : "ko"
            });


            let btnSave = document.getElementById("writeButton");
            btnSave.addEventListener("click",() => {
                saveGallery();
            });

        })
        .then(function loadOn() {
            CKEDITOR.on('dialogDefinition', function(ev) {
                let dialogName = ev.data.name;
                let dialogDefinition = ev.data.definition;

                if (dialogName === 'image') {
                    let onOk = dialogDefinition.onOk;
                    dialogDefinition.onOk = function(e) {
                        console.log('이미지 속성 창에서 확인 버튼이 눌렸습니다.');
                        onOk.apply(this, arguments);
                    };
                }
            });
        })


};

let fileCount = 0;
let totalCount = 10;
let fileNum = 0;
let inputFileList = new Array();
let deleteFileList = new Array();

function saveGallery() {
    if (confirm("저장하시겠습니까?") == false) {
        return;
    }
    let requestUsername = document.getElementById("username").value;
    let requestPassword = document.getElementById("password").value;
    let requestTitle = document.getElementById("title").value;
    url = "/board/none-user";
    makeDTO("POST",finalId,requestTitle,CKEDITOR.instances.contents.getData(),
        requestUsername,requestPassword,url)
        .then(function (response) {
            window.location.href="/?pageQuantity=1&boardQuantity=20";
            console.log(response);
        })
}

function makeDTO(method,id,title,content,username,password,url) {
    let requestData = {
        method: method,
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        // enctype : "multipart/form-data",
        body: JSON.stringify({
            'id' : id,
            'title' : title,
            'content' : content,
            'username' : username,
            'password' : password
        })
    };
    return fetch(url, requestData);
}
function eraseTitle() {
    let title = document.getElementById("title");
    if(title.value==="제목") {
        title.value="";
    }

}
function eraseUsername() {
    let username = document.getElementById("username");
    if(username.value==="이름") {
        username.value="";
    }

}
function erasePassword() {
    let username = document.getElementById("username");
    if(username.value==="이름") {
        username.value="";
    }

}
