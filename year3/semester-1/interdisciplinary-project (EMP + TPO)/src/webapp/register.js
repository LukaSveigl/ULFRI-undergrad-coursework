import { httpRequest, extractJSON } from "./utils.js";

window.register = function register(){

    // show spinner
    document.getElementById("mySpinner").style.display = "inline-block";

    //         http://localhost:17076/api/registerUser?username=kurac&name=Dick&surname=Dickins&companyName=MegaDickINC&phoneNumber=123456789&streetName=DickStreet&streetNumber=99&postCode=1230&cityName=Dom%C5%BEale&countryISO=SVN

    let username = document.getElementById("username").value;
    let name = document.getElementById("name").value;
    let surname = document.getElementById("surname").value;
    let companyName = document.getElementById("companyName").value;
    let phoneNumber = document.getElementById("phoneNumber").value;
    let streetName = document.getElementById("streetName").value;
    let streetNumber = document.getElementById("streetNumber").value;
    let postCode = document.getElementById("postCode").value;
    let cityName = document.getElementById("cityName").value;
    let countryISO = document.getElementById("countryISO").value;

    let url = "http://164.90.163.179:8080/SFL-WEB-API/registerUser?username=" + username + "&name=" + name + "&surname=" + surname + "&companyName=" + companyName + "&phoneNumber=" + phoneNumber + "&streetName=" + streetName + "&streetNumber=" + streetNumber + "&postCode=" + postCode + "&cityName=" + cityName + "&countryISO=" + countryISO;

    let data = httpRequest(url, "GET");
    let json = extractJSON(data);
    console.log(json);

    if (json.status == "Request processed.") {
        alert("User created successfully!\n Redirecting to login page...");
        window.location.href = "index.html";
    }else{
        alert("User creation failed!");
    }
}