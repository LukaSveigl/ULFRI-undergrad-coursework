import { httpRequest, extractJSON } from "./utils.js";

// http://localhost:17076/api/getParcelHistory?username=aarmatt59&password=password&parcelID=SVNXcZjO

// fetch parcel history
let username = "aarmatt59";
let password = "password";
let parcelID = new URL(window.location.href).searchParams.get("trackingNumber");
console.log("Tracking number: " + parcelID);
updateParcelIDLabel();

let url = "http://164.90.163.179:8080/SFL-WEB-API/getParcelHistory?parcelID=" + parcelID;
let data = httpRequest(url, "GET");
let json = extractJSON(data);
console.log(json);


populateTable();


function updateParcelIDLabel(){
    let parcelIDLabel = document.getElementById("trackingNumber");
    parcelIDLabel.innerHTML = parcelID;
}

function populateTable(){
    let tableBody = document.getElementById("tableBody");

    for (let i = 0; i < Object.keys(json).length; i++) { 
        let tr = document.createElement("tr");
        let tdDate = document.createElement("td");
        tdDate.innerHTML = json[i].dateCompleted;
        tr.appendChild(tdDate);
        let tdStatus = document.createElement("td");
        tdStatus.innerHTML = json[i].status;
        tr.appendChild(tdStatus);
        tableBody.appendChild(tr);
    }
}

