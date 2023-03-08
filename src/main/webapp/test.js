function updateTime(){
    let xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function (){
        if(xhr.readyState===4&&xhr.status===200){
            document.getElementById("time").innerText=xhr.responseText
        }
    }
    xhr.open('Get','time',true)
    xhr.send()
}