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


function JwtExpiredProcess() {
    location.href = domainUri+"jwt/expiration";
}
replace();
function replace() {


    boardsId = location.href.split("/boards/edit/")[1];
    url = "/boards/"+boardsId+"/data";
    let params = {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },

    }
    fetch(url,params)
        .then(function x(response) {
            if(response.status==500) {
                location.href = domainUri+"404page";;
            }
            waef = response.json();
            return waef;
        })
        // then 절로 중복 방지
        .then(function y(data) {
            title=document.getElementById("title");
            content=document.getElementById("content");
            username=document.getElementById("username");
            password = document.getElementById("password");
            return data;
        }).then(function z(data) {
        console.log(data);
        title.value = data.title;
        CKEDITOR.replace('contents',{
            filebrowserUploadUrl: '/image/upload/'+window.location.href.split("/edit/")[1],
            font_names : "맑은 고딕/Malgun Gothic;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;MS Mincho/MS Mincho;Trebuchet MS/Trebuchet MS;Verdana/Verdana",
            font_defaultLabel : "맑은 고딕/Malgun Gothic",
            fontSize_defaultLabel : "12",
            skin : "office2013",
            language : "ko"
        });
        CKEDITOR.instances.contents.setData(data.contents);
        username.value = data.boardWriterId;
        return content;
    }).then(function k(content) {
        console.log( CKEDITOR.instances.contents.getData());
        if(CKEDITOR.instances.contents.getData() === null) {
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
let boardReadData;

function saveGallery() {
    if (confirm("저장하시겠습니까?") == false) {
        return;
    }
    title = document.getElementById("title");
    content = document.getElementById("content");
    username = document.getElementById("username");
    password = document.getElementById("password");

    setTimeout(() => {
        let name;
        let url = domainUri + "user/account";
        let accountData = {
            "method": "GET"
        }

        fetch(url, accountData).then(function findUsername(response) {
            return response.text();
        }).then(function setUsername(data) {
            name = data;
        }).then(function () {
            boardReadData = {
                method: "PUT",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                // enctype : "multipart/form-data",
                body: JSON.stringify({
                    'boardId': boardsId,
                    'title': title.value,
                    'content': CKEDITOR.instances.contents.getData(),
                    'isSecret' : false
                    // 'username': username.value
                    // ,'userId': name,
                })
            };
            console.log(boardReadData);
        }).then(function x() {
            return fetch("/board/user/edit/"+boardsId, boardReadData)
                .then(function (response) {
                    if (response.status === 400) {
                        throw new Error("비밀번호가 다릅니다");
                    }

                    alert("저장되었습니다");
                    location.href = domainUri + "?pageQuantity=1&boardQuantity=20";
                })
                .catch(function (error) {
                    alert("비밀번호가 다릅니다");
                })
        }), 20000
    });
}
