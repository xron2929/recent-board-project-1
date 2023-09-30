let url;
let id;
let title;
let password;
let content;
let username;
let boardsId;
let domainUri;
const cookies = document.cookie.split(';'); // 모든 쿠키 가져오기
for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();
    if (cookie.startsWith('domainUrl=')) {
        domainUri = cookie.substring('domainURI='.length, cookie.length);
        break;
    }
}
let sessionId;
let stompClient;
setUrl().then(function(data) {
    sessionId = data;
}).then(function() {
    connect();
});

function connect() {
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
    let url = domainUri+"user-noneuser/account";
    let accountData = {
        "method" : "GET"
    }

    return fetch(url,accountData).then(function findUsername(response) {
        return response.text();
    });
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}
function JwtExpiredProcess() {
    location.href = domainUri+"jwt/expiration";
}
replace();
function replace() {
    let writeBoard = document.getElementById("inline");
    CKEDITOR.replace('contents',{
        filebrowserUploadUrl: '/image/upload/'+window.location.href.split("/edit/")[1],
        font_names : "맑은 고딕/Malgun Gothic;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;MS Mincho/MS Mincho;Trebuchet MS/Trebuchet MS;Verdana/Verdana",
        font_defaultLabel : "맑은 고딕/Malgun Gothic",
        fontSize_defaultLabel : "12",
        skin : "office2013",
        language : "ko"
    });

    boardsId = location.href.split("/boards/edit/")[1];
    url = "/boards/"+boardsId+"/data";
    let params = {
        method: "GET",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },

    }
    fetch(url,params)
        .then(function x(response) {
            if(response.status==500) {
                location.href = domainUri+"404page";;
            }
              waef = response.json();
            title=document.getElementById("title");
            content=document.getElementById("content");
            username=document.getElementById("username");
            password = document.getElementById("password");
              return waef;
        // then 절로 중복 방지
    }).then(function getBackData(data) {
        console.log(data);
        title.value = data.title;
        CKEDITOR.instances.contents.setData(data.contents);
        console.log(data.contents);
        alert(username.value);
        username.value = data.boardWriterName;
        return content;
    }).then(function isErrorRetry(content) {
        console.log(CKEDITOR.instances.contents.getData(content));
        if(CKEDITOR.instances.contents.getData() === null) {
            // 데이터 등록이 바로 안되서 리다이렉트 함
            location.href = location.href;
        }

        let btnSave = document.getElementById("writeButton");

        btnSave.addEventListener("click",() => {
            saveGallery();
        });

    }).catch(function (e) {
        // console.log(e);
        location.href = domainUri+"404page";
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
    title = document.getElementById("title");
    content = document.getElementById("content");
    username = document.getElementById("username");
    password = document.getElementById("password");
    url = "/board/none-user/edit/"+boardsId;
    let isSecret = false;
    setTimeout(() => {
        console.log(password.textContent);
        makeDTO("PUT",boardsId,title.value,CKEDITOR.instances.contents.getData(),
            username.value,password.value,isSecret,url)
            .then(function (response) {
                    if(response.status===400) {
                        throw new Error("비밀번호가 다릅니다");
                    }

                alert("저장되었습니다");
                location.href=domainUri+"?pageQuantity=1&boardQuantity=20";
                })
            .catch(function(error) {
                alert("비밀번호가 다릅니다");
            })
        ,20000});
    function makeDTO(method,id,title,content,username,password,isSecret,url) {
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
                'password' : password,
                'isSecret' : isSecret

            })
        };
        return fetch(url, requestData);
    }

}