function httpRequest(theUrl, type)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( type, theUrl, false ); // false for synchronous request
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function extractJSON(jsonString) {
  let jsonObject = JSON.parse(jsonString);
  let extractedVariables = {};

  for (let key in jsonObject) {
    extractedVariables[key] = jsonObject[key];
  }

  return extractedVariables;
}



export { httpRequest, extractJSON };













