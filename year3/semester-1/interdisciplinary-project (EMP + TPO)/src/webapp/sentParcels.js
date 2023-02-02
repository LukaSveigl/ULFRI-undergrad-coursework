import { httpRequest, extractJSON } from "./utils.js";

function populateTable(json){
    let tableBody = document.getElementById("tableBody");
    let tr;
  
    for (let i = 0; i < Object.keys(json).length; i++) {
      
      tr = document.createElement("tr");
  
      let tdParcelID = document.createElement("td");
      tdParcelID.innerHTML = json[i].parcelID;
      tr.appendChild(tdParcelID);
      
      let tdWeight = document.createElement("td");
      tdWeight.innerHTML = json[i].weight;
      tr.appendChild(tdWeight);
      
      let tdDimensions = document.createElement("td");
      tdDimensions.innerHTML = json[i].width + " x " + json[i].height + " x " + json[i].depth;
      tr.appendChild(tdDimensions);
      
      let buttonStatus = document.createElement("button");
      buttonStatus.type = "button";
      buttonStatus.className = "btn btn-primary btn-sm statusBadge";
      buttonStatus.innerHTML = json[i].statusID;
      let tdStatus = document.createElement("td");
      tdStatus.appendChild(buttonStatus);
      tr.appendChild(tdStatus);
      
      let actionLink = document.createElement("a");
      actionLink.href = "trackParcel.html?trackingNumber=" + json[i].parcelID;
      actionLink.className = "btn btn-outline-primary actionButton btn-sm";
      actionLink.innerHTML = "Track";
      let tdAction = document.createElement("td");
      tdAction.appendChild(actionLink);
      tr.appendChild(tdAction);
  
      tableBody.appendChild(tr);
    }
  
}

// redirect back to login page if not logged in
if (sessionStorage.getItem("loggedIn") != "true") {
    window.location.href = "index.html";
}

// set user's full name
document.getElementById("fullName").innerHTML = sessionStorage.getItem("name") + " " + sessionStorage.getItem("surname");

// fetch sent parcels
let url = "http://164.90.163.179:8080/SFL-WEB-API/getSentParcels?username=" + sessionStorage.getItem("username") + "&password=" + sessionStorage.getItem("password");
let data = httpRequest(url, "GET");
let json = extractJSON(data);
console.log(json);

// add them to the table
populateTable(json);

// sign out function
window.signOut = function signOut(){
    sessionStorage.clear();
    window.location.href = "index.html";
}