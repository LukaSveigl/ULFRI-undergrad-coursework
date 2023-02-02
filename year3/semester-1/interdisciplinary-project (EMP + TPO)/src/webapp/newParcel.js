import { httpRequest, extractJSON } from "./utils.js";

// redirect back to login page if not logged in
if (sessionStorage.getItem("loggedIn") != "true") {
    window.location.href = "index.html";
}
// set user's full name
document.getElementById("fullName").innerHTML = sessionStorage.getItem("name") + " " + sessionStorage.getItem("surname");

let username = sessionStorage.getItem("username");
let password = sessionStorage.getItem("password");

window.createParcel = function createParcel(){
    let parcelID = "SVN";
    let statusID = "1";
    let sender = username;
    let recipient = document.getElementById("recipient").value;
    let weight = document.getElementById("weight").value;
    let width = document.getElementById("width").value;
    let height = document.getElementById("height").value;
    let length = document.getElementById("length").value;

    let url = "http://164.90.163.179:8080/SFL-WEB-API/createParcel?username=" + username + "&password=" + password + "&parcelID=" + parcelID + "&statusID=" + statusID + "&sender=" + sender + "&recipient=" + recipient + "&weight=" + weight + "&length=" + length + "&width=" + width + "&height=" + height ;
    let data = httpRequest(url, "GET");
    let json = extractJSON(data);
    console.log(json);

    if (json.status == "Request processed.") {
        alert("Parcel created successfully!");
        window.location.href = "sentParcels.html";
    }else{
        alert("Parcel creation failed!");
    }
}

// sign out function
window.signOut = function signOut(){
    sessionStorage.clear();
    window.location.href = "index.html";
}