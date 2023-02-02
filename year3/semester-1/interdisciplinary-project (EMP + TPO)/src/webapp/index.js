import { httpRequest, extractJSON } from "./utils.js";


window.login = function login(){

    // show spinner
    document.getElementById("mySpinner").style.display = "inline-block";


    console.log("Fetching login info...");
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let url = "http://164.90.163.179:8080/SFL-WEB-API/getLoginInfo?username=" + username + "&password=" + password;
    let data = httpRequest(url, "GET");
    let json = extractJSON(data);
    console.log(json);

    if (json.login == "true") {
        console.log("Login credentials are correct.");
        startSession();
    } else {
        document.getElementById("loginWarning").style.display = "block";
        document.getElementById("mySpinner").style.display = "none";

        console.log("Login credentials are incorrect.");
    }

}

function startSession(){

    console.log("Clearing any old sessions...");
    sessionStorage.clear();

    console.log("Starting session...");
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let url = "http://164.90.163.179:8080/SFL-WEB-API/getUserFullName?username=" + username + "&password=" + password;
    let data = httpRequest(url, "GET");
    let json = extractJSON(data);
    console.log(json);

    let name = json.name;
    let surname = json.surname;

    sessionStorage.setItem("name", name);
    sessionStorage.setItem("surname", surname);
    sessionStorage.setItem("loggedIn", true);
    sessionStorage.setItem("username", username);
    sessionStorage.setItem("password", password);

    console.log("Session started.");

    window.location.href = "sentParcels.html";
}

