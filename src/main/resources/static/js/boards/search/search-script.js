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
function JwtExpiredProcess() {
    location.href = domainUri + "jwt/expiration";
}
x();
function x() {
    let params = document.location.href.split("?")[1];
    let item2 = document.getElementById("item2")
    console.log(item2.style.fontSize);
    console.log(item2);
    let pageQuantity = params.split("&")[0].split("=")[1];
    let boardQuantity = params.split("&")[1].split("&")[0].split("=")[1];
    let keyword = params.split("&")[2].split("=")[1];
    let tbody = document.getElementById("tbody");
    let elementsByClassNameElementTr;
    for (let boardCount = 0; boardCount < boardQuantity; boardCount++) {
        let tr = document.createElement("tr");
        tr.innerHTML =
            " <tr> " +
            "<td class='th-id'>NULL</td> " +
            "<td class='th-title'>12</td> " +
            "<td class='th-author'>12</td> " +
            "<td class='th-date'>작성날22짜</td> " +
            "</tr> ";
        tr.setAttribute("class","null");
        tbody.appendChild(tr);
    }
    // 1. 현재 페이지 로딩
    // 2. 게시판에 데이터 파싱할 조건
    // 3. 게시판 페이지 안보이게 처리
    let url = domainUri+"board/data?pageQuantity=" +pageQuantity
        +"&boardQuantity=" +boardQuantity
        +"&keyword="+keyword;
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
    fetch(url,data).then(function (response) {
        let x = response.json();
        return x;
    }).then(function (x) {
        x.forEach(obj => {
            Object.entries(obj).forEach(([key, value]) => {
                console.log("key"+key);
                let elementsByClassNameElement;
                if (key === 'id')
                {
                    console.log("boardSize = "+boardSize);
                    console.log(key+" "+value);
                    let trElement = document.getElementsByClassName("null")[0];
                    trElement.classList = "";
                    elementsByClassNameElement = document.getElementsByClassName("th-"+key)[currentSize];
                    console.log(elementsByClassNameElement);
                    elementsByClassNameElement.textContent = idData;
                    idNumber = value;
                    idData++;
                }
                if(key === 'title') {
                    console.log("boardSize = "+boardSize);
                    console.log(key+" "+value);
                    let elementsByClassNameElement = document.getElementsByClassName("th-"+key)[currentSize];
                    console.log(elementsByClassNameElement);
                    elementsByClassNameElement.textContent = value;
                }
                if(key === 'userName') {
                    console.log(boardSize);
                    let elementsByClassNameElement = document.getElementsByClassName("th-author")[currentSize];
                    elementsByClassNameElement.textContent = value;
                }
                if(key === "createdDate") {
                    console.log('success');
                    document.getElementsByClassName("th-date")[currentSize];
                    let elementsByClassNameElementTr = $("tr")[currentSize++];
                    let randeringBoard = idNumber;
                    elementsByClassNameElementTr.addEventListener("click",function load() {
                        window.location.href = domainUri+"boards/"+randeringBoard;
                    })
                }
                if(key === "lastModifiedDate") {
                    boardSize++;
                }

            });
            console.log('-------------------');
            console.log(boardSize);
        });
    }).then(function () {
        let nullElements = document.getElementsByClassName("null");
        if(nullElements[0] === undefined || nullElements[0] === null) {
            return;
        }
        if(nullElements[0] !== undefined && nullElements[0] !== null) {
            while (nullElements[0]) {
                nullElements[0].remove();
            }
        }
    });
    let item1 = document.getElementsByClassName("itema")[0];
    item1.addEventListener('click',function x() {
        window.location.href=domainUri+"board?pageQuantity=1&boardQuantity=20";
    })
    let home = document.getElementById("home");
    home.addEventListener("click",function home() {
        window.location.href = domainUri+"board?" +
            "pageQuantity="+pageQuantity+"&boardQuantity="+boardQuantity;
    })

    // imageFileUpload.addEventListener('change', uploadImageTest);

}
pageLoad();
function pageLoad() {
    let params = document.location.href.split("?")[1];
    let pageQuantity = params.split("&")[0].split("=")[1];
    let boardQuantity = params.split("&")[1].split("&")[0].split("=")[1];
    let keyword = params.split("&")[2].split("=")[1];
    let currentPageNumber;

    let startNumber;
    let isNextButton;
    let url = domainUri+"board/data/count?pageQuantity="+pageQuantity
        +"&boardQuantity="+boardQuantity+"&keyword="+keyword;
    let item = document.getElementsByClassName("itema");
    let data = {
        method:'GET'
    };
    fetch(url,data).then(
        function(response) {
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
            console.log("pageQuantity="+pageQuantity)
            console.log("startNumber="+startNumber)
            console.log("isNextButton="+isNextButton)
            for(let i = 0; i < pageQuantity; i++) {
                item[i+1].className = "itema";
                item[i+1].textContent = startNumber;
                console.log(item[i+1]);
                item[i+1].addEventListener("click",function addPageClick() {
                    window.location.href = domainUri+"board/searchScript?"
                        +"pageQuantity="+ item[i+1].textContent
                        +"&boardQuantity="+boardQuantity
                        +"&keyword="+keyword;
                })
                startNumber++;
            }
            if(isNextButton) {
                console.log("다음"+item[11]);
                item[11].className="itema";
                let nextPageNumber = parseInt(item[10].textContent)+1;
                item[11].addEventListener("click",function addNextPageButtonClick() {
                    window.location.href =  domainUri+"board/searchScript"
                        +"?pageQuantity=" + nextPageNumber
                        +"&boardQuantity=" + boardQuantity
                        +"&keyword=" + keyword;
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
function searchScript() {
    let search = document.getElementById('textarea');
    search.value = "";
    return;

}



