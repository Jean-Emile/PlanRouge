function onload() {
	document.addEventListener("deviceready", onDeviceReady, false);
}
function onDeviceReady() {
	// Now safe to use the PhoneGap API

	$(document).on("pageinit", "#identitepage", function() {

		var myDate = new Date();
		var select = document.getElementById("selectYear");
		var select3 = document.getElementById("age");
		var select2 = document.getElementById("selectDay");

		for ( var i = 1900; i <= myDate.getFullYear(); i++) {
			select.options[select.length] = new Option(i, i);
		}

		for ( var j = 1; j <= 31; j++) {
			if (j < 10) {
				select2.options[select2.length] = new Option('0' + j, '0' + j);
			} else {
				select2.options[select2.length] = new Option(j, j);
			}
		}

		for ( var j = 0; j <= 120; j++) {
			if (j < 10) {
				select3.options[select3.length] = new Option(j + ' ans', '00' + j);
			} else if (j < 100) {
				select3.options[select3.length] = new Option(j + ' ans', '0' + j);
			} else {
				select3.options[select3.length] = new Option(j + ' ans', j);
			}
		}
		readIdentity();
	});

	$(document).on("pageinit", "#victimCategory", function() {
		read_victim_category();
	});

	$(document).on("pageinit", "#champLibre", function() {
		read_champlibre();
	});

	$(document).on("pageinit", "#bilanComplementaire", function() {
		getInfoBilanComplementaire();
	});

	$(document).on("pageinit", "#evacuation", function() {
		read_destination();
	});

	$(document).on("pageinit", "#gpsHours", function() {
		readDateHoursGps();
	});

	$(document).on("pageinit", "#lesion", function() {
		read_lesion();
	});

	$(document).on("pageinit", "#urgenceVitale", function() {
		getInfoVitalEmergency();
	});
	
	


}

// Appuie bouton Catégorie victime
function changementradio() {

	// write_victim_category();
	navigator.notification.confirm("Voulez vous envoyer la catégorie de victime sur la puce", // message
	onConfirmCategory, // callback to invoke with index of button pressed
	'Enregistrer Catégorie Victime', // title
	'OUI,NON' // buttonLabels

	);
}

function onConfirmCategory(button) {
	if (button == 1) { // Confirm
		write_victim_category();
	}

}

// RÉCUPÉRATION HEURE
function showTime() {
	var myDate = new Date();
	// var date = myDate.getTime(); TODO : Changer date avec long millisecond
	// depuis 1970
	var day = myDate.getDate();
	var month = myDate.getMonth() + 1;
	var years = myDate.getFullYear();
	var time = myDate.getTime();
	var hour = myDate.getHours();
	var minute = myDate.getMinutes();
	var second = myDate.getSeconds();

	if (hour < 10) {
		hour = "0" + hour;
	}
	if (minute < 10) {
		minute = "0" + minute;
	}
	if (second < 10) {
		second = "0" + second;
	}
	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}
	return day + month + years + hour + minute + second;
}

// RAIFFRAICHISSEMENT GPS ET HEURE

function onConfirmGPS(button) {
	if (button == 1) {
		writeDateHoursGps();

	}
}

function GPSUpdate() {
	navigator.notification.confirm("Voulez-vous mettre a jour les coordonnees GPS et l'heure ?", // message
	onConfirmGPS, // callback to invoke with index of button pressed
	'Enregistrer GPS et HEURE', // title
	'OUI,NON' // buttonLabels

	);

}

// LOADING
function loading() {
	$.mobile.loading('show', {
		text : 'Chargement des données sur la puce',
		textVisible : true,
		theme : 'a',
		textonly : false,

	});
}

function stoploading() {
	$.mobile.loading("hide");
}

function testabdo() {
	navigator.notification.confirm("Problème ?", // message
	onConfirmAtteinte, // callback to invoke with index of button
	// pressed
	'Atteintes lésionnelles', // title
	'1,2,3,4,5,6,7' // buttonLabels

	);
}

function onConfirmAtteinte(button) {
	var atteinte = $('input[type=radio][name=atteintesLesionnelles]:checked').attr('value');
	alert('bouton appuyé ' + button + '   ' + atteinte);
}
