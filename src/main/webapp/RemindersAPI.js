const API = "http://localhost:3000/pawport";
function addReminder() {
    const title = document.getElementById("title").value;
    const minutes = document.getElementById("minutes").value;
    const email = document.getElementById("email").value;

    if (!title || !minutes || !email) {
        alert("Please fill all fields!");
        return;
    }

    fetch(API + "/reminder", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ title, minutes, email })
    })
    .then(res => res.text())
    .then(msg => {
        document.getElementById("status").innerText = msg;
    });
}