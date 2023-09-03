let id;
let title;
let password;
let content;
let username;
let finalId;
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
    location.href = domainUri+"jwt/" +
        "expiration";
}
let requestData = {
    method: "GET",

    headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
    }
    // enctype : "multipart/form-data",
};
fetch(domainUri+"board/finalId", requestData)
    .then(function (response) {
        let data = response.json();
        return data;
    })
    .then(function(data) {
        console.log(data);
        finalId = data;

    })
    .then(function loadCkeditor() {
            CKEDITOR.replace('contents', {
                filebrowserUploadUrl: '/image/upload/' + finalId,
                font_names: "맑은 고딕/Malgun Gothic;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;MS Mincho/MS Mincho;Trebuchet MS/Trebuchet MS;Verdana/Verdana",
                font_defaultLabel: "맑은 고딕/Malgun Gothic",
                fontSize_defaultLabel: "12",
                skin: "office2013",
                language: "ko"
            });
        });




    let btnSave = document.getElementById("writeButton");
    let url = domainUri + "user/account";
    let accountData = {
        "method": "GET"
    }
    fetch(url, accountData).then(function findUsername(response) {
        return response.text();
    }).then(function(data){
        username = document.getElementById("username");
        return data;
    }).then(function setUsername(data) {
        console.log("account = "+data);
        username.value = data;
    }).then(function setButtonSaveEvent() {
        btnSave.addEventListener("click",() => {
            saveGallery();
        });
    });





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
    // password = document.getElementById("password");


    setTimeout(() => {
        // console.log(password.textContent);


            let boardReadData = {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                // enctype : "multipart/form-data",
                body: JSON.stringify({
                    'id': finalId,
                    'title' : title.value,
                    'content' : CKEDITOR.instances.contents.getData(),
                    'username' : username.value

                })

            };
            let saveUrl =  domainUri + "board/user";
            fetch(saveUrl, boardReadData)
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
                , 20000
        });


}

function eraseTitle() {
    let title = document.getElementById("title");
    if(title.value==="제목") {
        title.value="";
    }
ㅈ
}