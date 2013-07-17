//
//function onload() {
//	document.addEventListener("deviceready", function onDeviceReady() {
//		var ws = new WebSocket('ws://192.168.1.140:8080/comptageVictime');
//		showMessage('Connecting...');
//		ws.onopen = function() {
//			showMessage('Connected !');
//		};
//		ws.onclose = function() {
//			showMessage('Lost connection');
//		};
//		ws.onmessage = function(msg) {
//			showMessage(msg.data);
//		};
//
//
//	}, true);
//}
//
//function showMessage(text) {
//	alert(text);
//}

function sendJsonToServer(id, birthday, age, name, sexe, surname){

	var json = JSON.stringify({id: id, sexe: sexe, age:age, nom : surname, prenom : name});
	var json1 = JSON.stringify({latitude: id, longitude: sexe, precision:age});
	var json2 = JSON.stringify({identity : json, gps :json1 })
	alert(json2);
}