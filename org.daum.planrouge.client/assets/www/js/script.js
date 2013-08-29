$(document).one("mobileinit", function () {
 
            // Setting #container div as a jqm pageContainer
            $.mobile.pageContainer = $('#container');
           
        });



$(document).on("pageshow", "#identitePage", function() {

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
});

function onload() {
	document.addEventListener("deviceready", onDeviceReady, false);
	
//	document.addEventListener("resume", onDeviceResume, false);


}

function onDeviceResume(){
	Toast.shortshow("onDeviceResume");
	
		Toast.shortshow("victimCategory");
		read_victim_category();
		
		refreshIdAgent();
		    
	//	document.getElements('personneConnecte2').innerHTML = "Connecté en tant que : " +window.localStorage.getItem("matriculeAgent");
	
}	

$(document).ready(function() {
	refreshIdAgent();
	});
function refreshIdAgent(){

	 var elements = document.getElementsByClassName('idAgent');
	    for(var x=0; x < elements.length; x++)
	    {
	       elements[x].innerHTML = "Connecté en tant que : " +window.localStorage.getItem("matriculeAgent");
	    }
}

function onDeviceReady() {
	
	Toast.shortshow("Deviceready");	
	refreshIdAgent();
	$("#username").val(window.localStorage.getItem("matriculeAgent"));	
	
	$(document).on("pageshow", "#loginPage", function() {
		refreshIdAgent();
		$("#username").val(window.localStorage.getItem("matriculeAgent"));	
		
	});
	
	$(document).on("pageshow", "#victimCategory", function() {
		read_victim_category();
		refreshIdAgent();
	});
	
	$(document).on("pageshow", "#accueil", function() {
		refreshIdAgent();
	});
	
	$(document).on("pageshow", "#champLibre", function() {
		read_champlibre();	
		refreshIdAgent();
	});
	
	$(document).on("pageshow", "#identitePage", function() {
		readIdentity();
		refreshIdAgent();
	});

	$(document).on("pageshow", "#bilanComplementaire", function() {
		getInfoBilanComplementaire();
	});

	$(document).on("pageshow", "#evacuation", function() {
		read_destination();
	});

	$(document).on("pageshow", "#gpsHours", function() {
		readDateHoursGps();
		refreshIdAgent();
	});

	$(document).on("pageinit", "#lesion", function() {
		read_lesion();	
	});

	$(document).on("pageinit", "#urgenceVitale", function() {
		getInfoVitalEmergency();
	});
	
	$(document).on("pageinit", "#waitTag", function() {
	});
	
	$(document).on("pageinit", "#completed", function() {
	});
}

// Appuie bouton Catégorie victime
function changementradio() {
	var category = $('input[type=radio][name=categorie_victime]:checked').attr('value');
	for (i = 1; i <= 5; i++) {
		if (i != category) {

			$('#cat'+i).css({"opacity":"0.3"});
		} else {
			$('#cat'+i).css({"opacity":"1.0"});
			
		}
	}
	
	// write_victim_category();
	navigator.notification.confirm("Voulez vous envoyer la catégorie de victime sur la puce", // message
	onConfirmCategory, // callback to invoke with index of button pressed
	'Enregistrer Catégorie Victime', // title
	'OUI,NON ' // buttonLabels

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
	var date = myDate.getTime(); //TODO : Changer date avec long millisecond depuis 1970
	
	return date;
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

function registerAgent() {
	var matricule = $("#username").val();
	window.localStorage.setItem("matriculeAgent", matricule);
	$.mobile.loading( 'show', {
		text: 'Connexion en cours',
		textVisible: true,
		theme: 'a',
		html: "<div style='width: 170px; text-align: center;' class='ui-bar ui-overlay-a ui-corner-all'><img  src='file:///android_asset/www/images/ajax-loader.gif' /><h2>Connexion en cours </h2></div>"
	});	
	
//	var result = window.tagid.getAgent(matricule);
//	if (result == true){
		window.location = "file:///android_asset/www/waitTag/index.html";
//	}else {
//		window.location = "file:///android_asset/www/login/index.html";	
//	}
//	result = null;
	
}


