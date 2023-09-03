let tbody;

window.onload = function getAlarm() {
    let alarmPage = location.href.split("?page=")[1];
    let url = "/alarm/page/" + alarmPage;
    let data = {
        method: "GET",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }
    };
    fetch(url, data).then(function (response) {
        tbody = document.getElementById("tbody");
        return response.json();
    }).then(function (dtoList) {
        console.log(dtoList);
        // DTO 리스트를 반복하여 테이블에 추가합니다.
        dtoList.forEach(dto => {
            // 새로운 행을 생성합니다.
            setDto(dto);
        });
    });
    getUserId(alarmPage);
};
function setDto(dto) {
    const tr = document.createElement('tr');

    // 새로운 열을 생성하고 값을 할당합니다.
    const td1 = document.createElement('td');
    td1.className = "th-id";
    td1.textContent = dto.id;

    const td2 = document.createElement('td');
    td2.className = "th-title";
    td2.textContent = dto.title;

    const td3 = document.createElement('td');
    td3.className = "th-author";
    td3.textContent = dto.commentWriter;

    const td4 = document.createElement('td');
    td4.textContent = dto.summaryCommentContent;
    td4.className = "th-content";
    const td5 = dto.boardId;
    const isVisited = dto.isVisited;

    // 새로운 열을 행에 추가합니다.
    tr.appendChild(td1);
    tr.appendChild(td2);
    tr.appendChild(td3);
    tr.appendChild(td4);

    // 행을 테이블에 추가합니다.
    tbody.appendChild(tr);
    isVisitedChangeColor(isVisited);
    tr.addEventListener("click", function () {
        addTbodyClick(dto.boardId, td1.textContent);
    });
}
function isVisitedChangeColor(isVisited) {
    if (isVisited == "true") {
        tbody.style.color = "grey";
    }
}
function addTbodyClick(boardId, alarmId) {
    submitVisited(alarmId).then(function (response) {
        location.href = "/boards/" + boardId;
    });
}
function submitVisited(alarmId) {
    let url = "/alarm/visited/" + alarmId;
    let data = {
        method: "PUT"
    };
    return fetch(url, data).then(function (response) {
        return response.text();
    });
}
function setData(currentPageNumber, userId) {
    let pages;
    let finalPageButton;
    let nextPageButton;
    let firstPageButton;
    let previosPageButton;
    let url = "/current/page?currentPageNumber=" + currentPageNumber + "&userId=" + userId;
    let data = {
        method: "GET",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }
    };
    return fetch(url, data).then(function (resposnse) {
        pages = document.getElementsByClassName("itema");
        nextPageButton = document.getElementById("item12");
        finalPageButton = document.getElementById("item13");
        firstPageButton = document.getElementById("item0");
        previosPageButton = document.getElementById("item1");
        return resposnse.json();
    }).then(function (response) {
        let page1 = document.getElementById("item2");
        for (let page = response.tenFirstPageNum; page <= response.tenFinalPageNum; page++) {
            let currentPage = pages[page % 10 + 1];
            currentPage.textContent = page;
            currentPage.className = "itema";
            currentPage.addEventListener("click", function () {
                location.href = "/alarm?page=" + page;
            });
        }

        if (response.isNextPage === true) {
            nextPageButton.addEventListener("click", function () {
                location.href = "/alarm?page=" + (response.tenFinalPageNum + 1);
            });
        }
        if (response.isNextPage === false) {
            nextPageButton.addEventListener("click", function () {
                location.href = "/alarm?page=" + response.allFinalPageNum;
            });
        }
        firstPageButton.addEventListener("click", function () {
            location.href = "/alarm?page=" + 1;
        });
        if (response.tenFirstPageNum === 1) {
            previosPageButton.addEventListener("click", function () {
                location.href = "/alarm?page=" + 1;
            });
        }
        if (response.tenFirstPageNum !== 1) {
            previosPageButton.addEventListener("click", function () {
                location.href = "/alarm?page=" + (response.tenFirstPageNum - 1);
            });
        }
        finalPageButton.addEventListener("click", function () {
            location.href = "/alarm?page=" + response.allFinalPageNum;
        });
    });
}
function getUserId(currentPageNum) {

    let data = {
        method: "GET"

    };
    fetch("/user-noneuser/account", data).then(function (reseponse) {
        return reseponse.text();
    }).then(function (response) {
        return setData(currentPageNum, response);
    });
}
