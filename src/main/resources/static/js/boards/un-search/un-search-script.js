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
let sessionId;
let stompClient;
let stompClient2;
let pageQuantity;
let boardQuantity;
setUrl().then(function(data) {
    sessionId = data;
}).then(function() {
    connect();
});

// setUri + connet 부터 일단 제작
function connect() {
    const socket = new SockJS('http://localhost:8080/my-websocket-endpoint');
    stompClient = Stomp.over(socket);
    console.log(socket);
    console.log(stompClient);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/'+sessionId+'/queue/messages', function(message) {
            alert("새로운 글이 작성되었습니다");
        });
    });
    // 이거 연결하는 동시에 queue/messages2도 같이 등록해서 알림 서비스 n개 만들어봐야될듯
    // ㅇㅇ..
    const socket2 = new SockJS('http://localhost:8080/my-websocket-endpoint2');
    stompClient2 = Stomp.over(socket2);
    stompClient2.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient2.subscribe('/user/'+sessionId+'/queue/messages2', function(message) {
            alert("새로운 글이 작성되었습니다2");
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    if(stompClient2!==null) {
        stompClient2.disconnect();
    }
    console.log("Disconnected");
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
// setTimeout(bringData,1000);
bringData();
    function bringData() {

        let item2 = document.getElementById("item2")
        console.log(item2.style.fontSize);
        console.log(item2);

        let isPageExistence = document.location.href.includes("pageQuantity");
        let isBoardExistence = document.location.href.includes("boardQuantity");
        if(isPageExistence === false) {
            pageQuantity = 1;
        }
        if(isPageExistence === false) {
            boardQuantity = 20;
        }

        if(isPageExistence === true) {
            let params = document.location.href.split("?")[1];
            pageQuantity = params.split("&")[0].split("=")[1];
        }
        if(isBoardExistence === true) {
            let params = document.location.href.split("?")[1];
            boardQuantity = params.split("&")[1].split("=")[1];
        }


        let tbody = document.getElementById("tbody");
        for (let boardCount = 0; boardCount < boardQuantity; boardCount++) {
            let tr = document.createElement("tr");
            tr.innerHTML =
                " <tr> " +
                "<td class='th-id'>NULL</td> " +
                "<td class='th-title'>12</td> " +
                "<td class='th-author'>12</td> " +
                "<td class='th-date'>작성날22짜</td> " +
                "</tr> ";
            tr.setAttribute("class", "null");
            tbody.appendChild(tr);
        }
        // 1. 현재 페이지 로딩
        // 2. 게시판에 데이터 파싱할 조건
        // 3. 게시판 페이지 안보이게 처리
        // pageQuantity,@RequestBody Long boardQuantity
        let data = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }
        let idNumber;
        let idData = (pageQuantity - 1) * boardQuantity + 1;
        let currentSize = 1;
        let boardSize = 1;
        fetch(domainUri+"boards?pageQuantity="+pageQuantity+"&boardQuantity="+boardQuantity, data).then(function (response) {
            let x = response.json();
            return x;
        }).then(function (x) {
            x.forEach(obj => {
                Object.entries(obj).forEach(([key, value]) => {
                    let elementsByClassNameElement;
                    if (key === 'boardId') {
                        console.log("boardSize = " + boardSize);
                        console.log(key + " " + value);
                        let trElement = document.getElementsByClassName("null")[0];
                        trElement.classList = "";
                        elementsByClassNameElement = document.getElementsByClassName("th-id" )[currentSize];
                        console.log(elementsByClassNameElement);
                        elementsByClassNameElement.textContent = idData;
                        idNumber = value;
                        idData++;
                    }
                    if (key === 'title') {
                        console.log("boardSize = " + boardSize);
                        console.log(key + " " + value);
                        let elementsByClassNameElement = document.getElementsByClassName("th-" + key)[currentSize];
                        console.log(elementsByClassNameElement);
                        elementsByClassNameElement.textContent = value;
                    }
                    if (key === 'userName') {
                        console.log(boardSize);
                        let elementsByClassNameElement = document.getElementsByClassName("th-author")[currentSize];
                        elementsByClassNameElement.textContent = value;
                    }
                    if (key === "lastModifiedDate") {
                        console.log('success');
                        document.getElementsByClassName("th-date")[currentSize];

                        addRandingBoard(idNumber,currentSize);
                        currentSize++;
                    }
                    if (key === "lastModifiedDate") {
                        boardSize++;
                    }

                });
                console.log('-------------------');
                console.log(boardSize);
            });
        }).then(removePage);
        addClickEvent();
        LoadPage();
        // imageFileUpload.addEventListener('change', uploadImageTest);

}
function addRandingBoard(idNumber,currentSize) {
    let elementsByClassNameElementTr = document.getElementsByTagName("tr")[currentSize];
    setTimeout(() => clickAddLink(elementsByClassNameElementTr,idNumber),1000);

}
function clickAddLink(elementsByClassNameElementTr,renderingBoard) {
        console.log("elementsByClassNameElementTr = "+elementsByClassNameElementTr);
    elementsByClassNameElementTr.addEventListener("click", function load() {
        window.location.href = domainUri + "boards/" + renderingBoard;
    })
}

    function removePage() {
        let nullElements = document.getElementsByClassName("null");
        if (nullElements[0] === undefined || nullElements[0] === null) {
            return;
        }
        if (nullElements[0] !== undefined && nullElements[0] !== null) {
            while (nullElements[0]) {
                nullElements[0].remove();
            }
        }
    }

    function addClickEvent() {
        let item1 = document.getElementsByClassName("itema")[0];
        item1.addEventListener('click', function x() {
            window.location.href = domainUri + "?pageQuantity=1&boardQuantity=20";
        })
        let home = document.getElementById("home");
        home.addEventListener("click", function home() {
            window.location.href = location.href
        })
    }


    function LoadPage() {
        let currentPageNumber;
        let startNumber;
        let isNextButton;
        let url = domainUri + "page?currentBoardPage=" + pageQuantity + "&boardQuantity=" + boardQuantity;
        let item = document.getElementsByClassName("itema");
        let data = {
            method: 'GET'
        };
        fetch(url, data).then(
            function (response) {
                if(response.status !== 200) {
                    throw new Error("에러");
                }
                return response.json();
            })
            .then(function jsonApi(data) {
                /*
                startNumber;
            this.pageQuantity = pageQuantity;
            this.isNextButton = isNextButton;
                 */
                pageQuantity = data.pageQuantity;
                startNumber = data.startNumber;
                isNextButton = data.isNextButton;
                console.log("pageQuantity=" + pageQuantity)
                console.log("startNumber=" + startNumber)
                console.log("isNextButton=" + isNextButton)
                for (let i = 0; i < pageQuantity; i++) {
                    item[i + 1].className = "itema";
                    item[i + 1].textContent = startNumber;
                    console.log(item[i + 1]);
                    item[i + 1].addEventListener("click", function addPageClick() {
                        window.location.href = domainUri + "?pageQuantity=" +
                            item[i + 1].textContent +
                            "&boardQuantity=" + boardQuantity;
                    })
                    startNumber++;
                }
                if (isNextButton) {
                    console.log("다음" + item[11]);
                    item[11].className = "itema";
                    let nextPageNumber = parseInt(item[10].textContent) + 1;
                    item[11].addEventListener("click", function addNextPageButtonClick() {
                        window.location.href = domainUri + "?pageQuantity=" +
                            +nextPageNumber
                            + "&boardQuantity="
                            + boardQuantity;
                    })
                }
            })
    }


    function eraseAdvertisement() {

        console.log("ready");
        let c = document.getElementById('advertisementCss');
        console.log(c);
        c.href = "/css/boards/none-advertise/none-advertise-boards.css";
        console.log("succ");

        // relative 때문에 한 줄 띄어지는 문제로 absolute로 직접 좌표를 입력해서 설정함


        return;
    }

function search() {
    let search = document.getElementById('textarea');
    search.value = "";
    return;

}

alertMobileSize();
function alertMobileSize() {
    let width = window.innerWidth; // 현재 윈도우의 가로 크기
    let height = window.innerHeight; // 현재 윈도우의 세로 크기

    // iOS에서 Safari의 경우 가로 모드에서 높이가 실제 높이보다 더 길게 나타날 수 있습니다.
    if (navigator.userAgent.match(/(iPod|iPhone|iPad)/) && navigator.userAgent.match(/AppleWebKit/)) {
        if (width > height) {
            // 가로 모드에서는 높이가 실제 높이보다 작아지기 때문에 다시 계산합니다.
            height = width / (window.screen.width / window.screen.height);
        }
    }
    alert(width);
    alert(height);
}





