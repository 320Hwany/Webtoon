
document.getElementById("authorSignup").addEventListener('click', function (){
    const nickname = document.getElementById("authorNickname").value;
    const email = document.getElementById("authorEmail").value;
    const password = document.getElementById("authorPassword").value;

    fetch("http://localhost:8080/author/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "nickname": nickname,
            "email": email,
            "password": password
        }),
    }).then(res => {
        if(res.status == "200"){
            window.location.href = "http://localhost:8080/author/home.html"
        } else if(res.status == "400"){
            alert("회원정보를 다시 입력해주세요")
        }
    })
})
