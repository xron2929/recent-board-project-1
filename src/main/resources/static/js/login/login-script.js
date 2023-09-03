
function erasePassword() {
    let search=document.getElementById('password');
    if(search.class!=="") {
        search.value="";
        search.type="password";
    }
    search.class="";

    return;
}
function eraseID() {
    let search=document.getElementById('id');
    if(search.class!=="") {
        search.value="";
    }
    search.class="";
    return;
}



/*
document.getElementById('sdfadsfdsa11').jscolor.show();  얘가 undefined 아니게 되는지 나중에 확인 ㄱ ㄱ
create Element 로 감춤 ㄱㄱ
*/