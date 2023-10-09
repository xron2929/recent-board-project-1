
let allCommentId = 1;
let isAddCommentClick = false;

function x() {
    let section = document.getElementById("section");
    let likeCount = document.getElementById("likeCount");
    let disLikeCount = document.getElementById("disLikeCount");
    let up = document.getElementById("up");
    let down = document.getElementById("down");
    let likeCountNumber;
    let disLikeCountNumber;
    let x= "<p>h1</p>";
    let borderNumber = document.location.href.split("boards/")[1];
    let content;
    let form = document.createElement('div');
    form.setAttribute('method', 'post'); //POST 메서드 적용
    let url = "/boards/"+Number(borderNumber)+"/data";
    // form.setAttribute('action', url);	// 데이터를 전송할 url
    // json 리다이렉트 vs form 전송 받기 근데 이경우는 form 이동으로ㅓ 해버리면 로직이 되게 복잡해짐
    let form2 = document.createElement('form');

    form2.setAttribute('method', 'get'); //POST 메서드 적용
    let url2 = +"/404page";
    form2.setAttribute('action', url2);	// 데이터를 전송할 url
    let requestData = {
        method: "GET",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
    }
    fetch(url, requestData)
        .then(function (response) {
            //  console.log(response.json());
            if(response.status===500) {
                throw new Error("데이터 없음");
            }
            // alert("저장되었습니다");
            return response.json();
        })
        .then(function (datas) {
            likeCountNumber = datas['likeCount'];
            disLikeCountNumber = datas['disLikeCount'];
            boardWriterId = datas["boardWriterId"];
            console.log(datas["contents"]);
            content = datas["contents"];
        }).then(function() {
        form.innerHTML = content;
        likeCount.textContent = likeCountNumber;
        disLikeCount.textContent = disLikeCountNumber;
        section.append(form);
        up.addEventListener("click",()=>updateLikeCount(likeCount,borderNumber,boardWriterId));
        down.addEventListener("click",()=>updateDisLikeCount(disLikeCount,borderNumber,boardWriterId));
        return;

    }).catch(function (e) {
        console.log(e);
        document.body.appendChild(form2);
        console.log(form2);
        form2.submit();
    });

    // let commentSubmitButtons = document.getElementsByClassName("parentCommentSubmitArea");

    let isAddCommentClick = false;
    let editButton = document.getElementById("edit");
    let deleteButton = document.getElementById("delete");
    editButton.addEventListener("click",()=>editBoard());
    deleteButton.addEventListener("click",()=>removeBoard());
    console.log(isAddCommentClick);
    addComment(isAddCommentClick);
}


function updateLikeCount(likeCount,boardId,userId) {
    let url = "/board/like/user";
    let data ={
        method: "PUT",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "joinStatusId": boardId
        })
    }
    fetch(url,data).then(function(response) {
        return response.text();
    }).then(function(response) {
        likeCount.textContent = response;
    })
}

function updateDisLikeCount(disLikeCount,boardId,userId) {
    let url = "/board/dislike/user";
    let data ={
        method: "PUT",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "joinStatusId": boardId
        })
    }
    fetch(url,data).then(function(response) {
        return response.text();
    }).then(function(response) {
        disLikeCount.textContent = response;
    })
}


function editBoard() {
    let param = document.location.href.split("boards/")[1];
    window.location.href="/boards/edit/"+param;
}

function removeBoard() {
    let param = document.location.href.split("boards/")[1];
    let url = "/board/admin/"+ param;
    // 데이터를 전송할 url
    let requestData = {
        method: "DELETE",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }}
    fetch(url, requestData)
        .then(function (response) {
            window.location.href="/?pageQuantity=1&boardQuantity=20";
        });
}

function addComment(isAddCommentClick) {
    if(isAddCommentClick===true) return;
    isAddCommentClick = false;
    // fetch 전송 하고
    // 뒤에 데이터 받게 하기
    let commentGroup;
    let boardId = document.location.href.split("boards/")[1];
    let userName;

    let readComment;
    let comment;
    let name;
    let textarea;
    let commentId = 1;

    let url = "/comment/"+boardId+"?startId="+allCommentId;
    let data= {
        method: 'get' // 통신할 방식
    }
    fetch(url,data).then(function (response) {
        return response.json();
    }).then(function (x) {
        console.log(x);
        x.some(obj => {
            commentGroup = document.getElementById("commentGroup");
            readComment = document.createElement("div");
            comment = document.createElement("div");
            name = document.createElement("div");
            textarea = document.createElement("div");
            textarea.className = "parentWriteComment";
            readComment.id = "comment" + commentId;
            comment.className = "parentReadCommentArea";
            name.className = "parentReadCommentName";
            name.textContent = "이름";
            Object.entries(obj)
                .forEach(([key, value]) => {
                    /*
                    private String userName;
        private String content;
        forEach 이전에 append 문법 넣어도 실질적으로 똑같은 거라 추가안되고 변경만 된다는 점 주의
                     */
                    if(commentId==11) {
                        return;
                    }
                    if (key === "userName") {
                        name.textContent = value;
                    } else if (key === "content") {
                        textarea.textContent = value;
                    }
                })
            if(commentId===11) {
                return;
            }
            readComment.append(comment);

            comment.append(name);
            commentGroup.append(readComment);
            commentGroup.append(textarea);
            commentId++;
            allCommentId++;
            console.log(commentId);
        })
        console.log(commentId);
        setTimeout(addCommentButton(commentId),100);
    });


}
function addCommentButton(commentId) {
    console.log("??");

    if(commentId === 11) {
        console.log("??");
        let isAddReadComment = document.getElementById("addReadComment")
        if(isAddReadComment!==null) {
            commentGroup.removeChild(isAddReadComment);
        }
        // let addReadComment = document.createElement("div");
        //addReadComment.id="addReadComment";
        //addReadComment.textContent="더보기";
        // addReadComment.addEventListener("click",addComment);
        let addReadComment = document.createElement("div");
        addReadComment.innerHTML = "<div id='addReadComment' >더보기</div>"
        addReadComment.addEventListener("click",addComment);
        commentGroup.after(addReadComment);
        // console.log(addReadComment);

    }
}
function submitComment(commentSubmitButton,isAddCommentClick) {
    // console.log(commentSubmitButton.id);

    if(isAddCommentClick === true) return;
    isAddCommentClick = true;

    let boardId = document.location.href.split("boards/")[1];
    let writeCommentId = commentSubmitButton.id.split("parentWriteCommentSubmit")[1];
    let userId = document.getElementById("commentWriteName" + writeCommentId);
    let content = document.getElementById("parentWriteComment1");
    content.addEventListener("keydown",resize);
    content.addEventListener("keyup",resize);
    // console.log(commentId);
    // /user/comment/1 유저는 따로 만들어야되긴함
    let url = "/user/comment/"+boardId;
    let data = {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            "userId":userId.textContent,
            "content":content.value
        })
    }
    fetch(url,data).then(function x(response){
        return sendMessage(boardId,content.value,userId.textContent);
    }).then(function() {
        location.href= location.href;
    });
    console.log(isAddCommentClick);
    return isAddCommentClick;

}

let stompClient = null;
let commentSubmitButtons = null;
let sessionId;
let boardWriterId;
let username;
setUrl().then(function(data) {
    sessionId = data;
    console.log("sessionId = "+sessionId);
}).then(function() {
    connect();
    x();
    commentSubmitButtons = document.getElementById("parentWriteCommentSubmit1");
    username = document.getElementById("commentWriteName1");
    let data = {
        "METHOD" : "GET",
        headers: {
            "Content-Type": "application/json"
        }
    }
    return fetch("/user/account",data);
}).then(function(response) {
    return response.json();
}).then(function(response) {
    console.log(response);
    username.textContent = response.username;
    isAddCommentClick = commentSubmitButtons.addEventListener("click",()=>submitComment(commentSubmitButtons,isAddCommentClick));
})


function connect() {

    const socket = new SockJS('/my-websocket-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/"+sessionId+"/queue/messages2", function(message) {
            alert("새로운 글이 작성되었습니다");
        });
    });
}
function setUrl() {
    boardId = location.href.split("boards/")[1];

    let url = "/user-noneuser/account";
    console.log(url);
    let accountData = {
        "method" : "GET",
        headers : {
            "Content-Type": "application/json"
        }
    }

    return fetch(url,accountData).then(function findUsername(response) {
        return response.json();
    }).then(function(response) {
        return response.userId;
    })
}
function sendMessage(boardId,summaryCommentContent,commentWriter) {
    let data = {
        message:"update"
    }
    stompClient.send("/app/send-message-to-user", {"sessionId":sessionId},JSON.stringify(data));

    return setAlarmData(boardId,summaryCommentContent,commentWriter)
        .then(function (response) {
            console.log(response);
        })

}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function isUser(userId) {
    if(boardWriterId.includes("_")) {
        return true;
    }
    return isAdmin(userId);
}
function setUserAlarmData(boardId,summaryCommentContent,commentWriter) {
    let alarmData = {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "boardId": boardId,
            "summaryCommentContent": summaryCommentContent,
            "commentWriter" : commentWriter,
            "isVisited" : false
        })
    }
    return fetch("/alarm/user",alarmData).then(function(response) {
        return response.text();
    })
}
function setNoneUserAlarmData(boardId,summaryCommentContent,commentWriter) {
    let alarmData = {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "boardId": boardId,
            "summaryCommentContent": summaryCommentContent,
            "userName" : commentWriter,
            "uuid":sessionId,
            "isVisited" : false
        })
    }
    return fetch("/alarm/none-user",alarmData).then(function(response) {
        return response.text();
    })
}
function setAlarmData(boardId,summaryCommentContent,commentWriter) {
    if (isUser(commentWriter)) {
        return setUserAlarmData(boardId,summaryCommentContent,commentWriter);
    }
    else {
        return setNoneUserAlarmData(boardId,summaryCommentContent,commentWriter);
    }

}
function isAdmin(userId) {
    let data= {
        method: "GET"
    };
    return fetch("/check/admin/"+userId,data).then(function (res) {
        console.log(res);
        return res.text();
    }).then(function(data){
        console.log(data);
        if(data==true) {
            return true;
        }
        return false;
    })
}
let content = document.getElementById("parentWriteComment1");
content.addEventListener("keydown",resize);
content.addEventListener("keyup",resize);

function resize() {
    let content = document.getElementById("parentWriteComment1");
    console.log("??error");
    content.style.height = "1px";
    content.style.height = (12+content.scrollHeight)+"px";
}
