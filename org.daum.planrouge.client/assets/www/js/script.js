$(document).on("pageinit", "#identitepage", function() {
	// alert ("pageload");
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

// WEBSOCKET

function showMessage(text) {
	alert(text);
}


// Fin WEBSOCKET

// Appuie bouton Catégorie victime
function changementradio() {
	navigator.notification.confirm("Voulez vous envoyer la catégorie de victime sur la puce", // message
	onConfirmCategory, // callback to invoke with index of button pressed
	'Enregistrer Catégorie Victime', // title
	'OUI,NON' // buttonLabels

	);
}

function onConfirmCategory(button) {
	if (button == 1) {
		write_victim_category();
	}
}


// Chargement pages
function onload() {
	document.addEventListener("deviceready", function onDeviceReady() {
		var ws = new WebSocket('ws://192.168.1.101:8080/addVictim');
		showMessage('Connecting...');
		ws.onopen = function() {
			showMessage('Connected !');
		};
		ws.onclose = function() {
			showMessage('Lost connection');
		};
		ws.onmessage = function(msg) {
			showMessage(msg.data);
		};


	}, true);
} // Cette méthode est appelée lorsque PhoneGap est fini d’être chargé


// RÉCUPÉRATION HEURE
function showTime() {
	var myDate = new Date();
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

// URGENCE VITALE
function tableUrgence() {
	var data = "";
	for (i = 1; i < 29; i++) {
		var x = "urgence" + i;
		if (document.getElementById(x).checked == true) {
			data += "1";
		} else {
			data += "0";
		}

	}

	window.tagid.id(data, $("#perte_connaissance").val(), $("#freq_respiratoire").val(), $("#saturation").val(), $("#freq_cardiaque").val(), $(
			"#pression_arterielle").val());

}

function getInfoVitalEmergency() {

	var y = window.tagid.read_vital_emergency();
	if (y != false) {
		var x = parseInt(y.substring(0, 7), 16).toString(2);
		var pc = parseInt(y.substring(7, 9), 16);
		var fr = parseInt(y.substring(9, 11), 16);
		var sat = parseInt(y.substring(11, 13), 16);
		var fc = parseInt(y.substring(13, 15), 16);
		var pa = parseInt(y.substring(15, 17), 16);

		while (x.length < 28) {
			x = '0' + x;
		}

		for (i = 1; i <= 28; i++) {
			if (x.charAt(i - 1) == '0') {
				$('input[name=urgence_vitale_' + i + ']').attr('checked', false).checkboxradio("refresh");
			} else {
				$('input[name=urgence_vitale_' + i + ']').attr('checked', true).checkboxradio("refresh");
			}
		}

		$("#perte_connaissance").val(pc);
		$("#freq_respiratoire").val(fr);
		$("#saturation").val(sat);
		$("#freq_cardiaque").val(fc);
		$("#pression_arterielle").val(pa);
	}

}

// BILAN COMPLEMENTAIRE
function getInfoBilanComplementaire() {

	var y = window.tagid.read_bilan_complementaire();
	if (y != false) {
		var x = parseInt(y[0].substring(0, 7), 16).toString(2);
		var oxy = parseInt(y[0].substring(7, 9), 16);
		var chocs = parseInt(y[0].substring(9, 11), 16);

		while (x.length < 28) {
			x = '0' + x;
		}

		for (i = 1; i <= 4; i++) {
			if (x.charAt(i - 1) == '0') {
				$('#bilan_complementaire_' + i).attr("checked", false).checkboxradio("refresh");
			} else {
				$('#bilan_complementaire_' + i).attr("checked", true).checkboxradio("refresh");
			}
		}
		for (i = 5; i <= 27; i++) {
			if (x.charAt(i - 1) == '0') {
				$('input[name=bilan_complementaire_' + i + ']').attr('checked', false).checkboxradio("refresh");
			} else {
				$('input[name=bilan_complementaire_' + i + ']').attr('checked', true).checkboxradio("refresh");
			}
		}

		$("#oxygene").val(oxy);
		$("#chocs").val(chocs);
		$("#immobilisation").val(y[6]);
		$("#extension").val(y[5]);
		$("#maladies").val(y[2]);
		$("#hospitalisations").val(y[3]);
		$("#traitements").val(y[4]);
		$("#allergies").val(y[7]);
	}
}

function writeBilanComplementaire() {
	var data = "";

	data = $('input[type=radio][name=bilan_complementaire_1]:checked').attr('value');

	for (i = 5; i <= 27; i++) {
		var x = "bilan_complementaire_" + i;
		if (document.getElementsByName(x)[0].checked == true) {
			data += "1";
		} else {
			data += "0";
		}

	}

	window.tagid.write_bilan_complementaire(data, $("#oxygene").val(), $("#chocs").val(), $("#immobilisation").val(), $("#extension").val(), $(
			"#maladies").val(), $("#hospitalisations").val(), $("#traitements").val(), $("#allergies").val());
}

// GPS ET HEURE

function writeDateHoursGps() {
	alert('write write GPS and Hours');
	navigator.geolocation.getCurrentPosition(function(position) {
		var time = showTime();
		var lat = position.coords.latitude;
		var long = position.coords.longitude
		var latitude = Math.round((lat) * 10000000);
		var longitude = Math.round((long) * 10000000);
		var accuracy = Math.round(position.coords.accuracy);
		var signe;
		if (latitude < 0 && longitude < 0) {
			signe = 3;
			latitude = latitude * (-1);
			longitude = longitude * (-1);
		} else if (latitude < 0) {
			signe = 1;
			latitude = latitude * (-1);
		} else if (longitude < 0) {
			signe = 2;
			longitude = longitude * (-1);
		} else {
			signe = 0;
		}

		var id = window.tagid.write_date_hours_gps(time, latitude, longitude, signe, accuracy);
		if (id != false) {
			ws.send('{"id":"' + id + '",""GPS_Hours":{"latitude":"' + lat + '","longitude":"' + long + '","precision":"' + accuracy + '","heure":"'
					+ time + '"}');
		}
	}, function() {
		alert('code: ' + error.code + '\n' + 'message: ' + error.message + '\n');
	});

}

function readDateHoursGps() {

	var y = window.tagid.read_date_hours_gps();
	if (y != false) {
		var date1 = y[0];
		date1 = date1.substring(0, 2) + "/" + date1.substring(2, 4) + "/" + date1.substring(4, 8) + "   " + date1.substring(8, 10) + ":"
				+ date1.substring(10, 12) + ":" + date1.substring(12, 14);

		var latitude1 = y[1];
		var longitude1 = y[2];
		var accuracy1 = y[3];

		var date2 = y[4];
		date2 = date2.substring(0, 2) + "/" + date2.substring(2, 4) + "/" + date2.substring(4, 8) + "   " + date2.substring(8, 10) + ":"
				+ date2.substring(10, 12) + ":" + date2.substring(12, 14);
		var latitude2 = y[5];
		var longitude2 = y[6];
		var accuracy2 = y[7];

		var date3 = y[8];
		date3 = date3.substring(0, 2) + "/" + date3.substring(2, 4) + "/" + date3.substring(4, 8) + "   " + date3.substring(8, 10) + ":"
				+ date3.substring(10, 12) + ":" + date3.substring(12, 14);
		var latitude3 = y[9];
		var longitude3 = y[10];
		var accuracy3 = y[11];

		var date4 = y[12];
		date4 = date4.substring(0, 2) + "/" + date4.substring(2, 4) + "/" + date4.substring(4, 8) + "   " + date4.substring(8, 10) + ":"
				+ date4.substring(10, 12) + ":" + date4.substring(12, 14);
		var latitude4 = y[13];
		var longitude4 = y[14];
		var accuracy4 = y[15];

		$("#date1").text(date1);
		$("#latitude1").text(latitude1);
		$("#longitude1").text(longitude1);
		$("#accuracy1").text(accuracy1 + ' m');

		$("#date2").text(date2);
		$("#latitude2").text(latitude2);
		$("#longitude2").text(longitude2);
		$("#accuracy2").text(accuracy2 + ' m');

		$("#date3").text(date3);
		$("#latitude3").text(latitude3);
		$("#longitude3").text(longitude3);
		$("#accuracy3").text(accuracy3 + ' m');

		$("#date4").text(date4);
		$("#latitude4").text(latitude4);
		$("#longitude4").text(longitude4);
		$("#accuracy4").text(accuracy4 + ' m');

	}
}

function GoogleMap1() {
	window.open('https://maps.google.fr/maps?q=' + document.getElementById('latitude1').innerHTML + ','
			+ document.getElementById('longitude1').innerHTML + '&num=1&t=h&vpsrc=0&hl=fr&ie=UTF8&z=20&iwloc=A', '_blank', 'location=yes');
}
function GoogleMap2() {
	window.open('https://maps.google.fr/maps?q=' + document.getElementById('latitude2').innerHTML + ','
			+ document.getElementById('longitude2').innerHTML + '&num=1&t=h&vpsrc=0&hl=fr&ie=UTF8&z=20&iwloc=A', '_blank', 'location=yes');
}
function GoogleMap3() {
	window.open('https://maps.google.fr/maps?q=' + document.getElementById('latitude3').innerHTML + ','
			+ document.getElementById('longitude3').innerHTML + '&num=1&t=h&vpsrc=0&hl=fr&ie=UTF8&z=20&iwloc=A', '_blank', 'location=yes');
}
function GoogleMap4() {
	window.open('https://maps.google.fr/maps?q=' + document.getElementById('latitude4').innerHTML + ','
			+ document.getElementById('longitude4').innerHTML + '&num=1&t=h&vpsrc=0&hl=fr&ie=UTF8&z=20&iwloc=A', '_blank', 'location=yes');
}

// DESTINATION

function write_destination() {
	var evacuation = $('input[type=radio][name=evacuation]:checked').attr('value');

	window.tagid.write_destination(evacuation, $("#destination").val(), $("#ville_destination").val(), $("#code_postal").val());

}

function read_destination() {

	var y = window.tagid.read_destination();
	if (y != false) {
		var evacuation = y[0];
		var destination = y[1];
		var ville = y[2];
		var code_postal = y[3];

		for (i = 1; i <= 6; i++) {
			$('#evacuation_' + i).attr("checked", false).checkboxradio("refresh");
		}
		for (i = 1; i <= 6; i++) {
			if (evacuation == i) {
				$('#evacuation_' + i).attr("checked", true).checkboxradio("refresh");
			}
		}

		$("#destination").val(destination);
		$("#ville_destination").val(ville);
		$("#code_postal").val(code_postal);
	}
}

// IDENTITÉ

function writeIdentity() {
	var sexe = $('input[type=radio][name=identity_sexe]:checked').attr('value');
	var age_unit = $('input[type=radio][name=identity_age]:checked').attr('value');
	var day = document.getElementById("selectDay");
	var year = document.getElementById("selectYear");
	var month = document.getElementById("selectMonth");
	var birthday = day.options[day.selectedIndex].value + month.options[month.selectedIndex].value + year.options[year.selectedIndex].value;
	var surname = $("#surname").val();
	var name = $("#firstname").val();
	var age = document.getElementById("age");
	age = age.options[age.selectedIndex].value
	//window.tagid.write_identity(surname, name, sexe, age, age_unit, birthday);
	var id = 123;
	var id = window.tagid.write_date_hours_gps(time, latitude, longitude, signe, accuracy);
//	if (id != false) {
//		ws.send( '{"id":"'+id+'","identity":{"birthday":"'
//                	+birthday+'","age":"'
//                	+age+'","name":"'
//                	+name+'","sexe":"'
//                	+sexe+'","surname":"'
//                	+surname+'"},}') ;
//	}
	
	 sendJsonToServer(id, birthday, age, name, sexe, surname);

}

function readIdentity() {

	var y = window.tagid.read_identity();

	if (y != false) {
		var firstname = y[0];
		var surname = y[1];
		var sexe = y[2];
		var age = y[3];
		var age_unit = y[4];
		var birthday = y[5];
		var day = birthday.substring(0, 2);
		var month = birthday.substring(2, 4);
		var year = birthday.substring(4, 8);

		if (sexe == '1') {
			$('#identity_sexe_1').attr("checked", true).checkboxradio("refresh");
			$('#identity_sexe_2').attr("checked", false).checkboxradio("refresh");
			$('#identity_sexe_3').attr("checked", false).checkboxradio("refresh");
		} else if (sexe == '2') {
			$('#identity_sexe_1').attr("checked", false).checkboxradio("refresh");
			$('#identity_sexe_2').attr("checked", true).checkboxradio("refresh");
			$('#identity_sexe_3').attr("checked", false).checkboxradio("refresh");
		} else {
			$('#identity_sexe_1').attr("checked", false).checkboxradio("refresh");
			$('#identity_sexe_2').attr("checked", false).checkboxradio("refresh");
			$('#identity_sexe_3').attr("checked", true).checkboxradio("refresh");
		}

		if (age_unit == '1') {
			$('#identity_age_1').attr("checked", true).checkboxradio("refresh");
			$('#identity_age_2').attr("checked", false).checkboxradio("refresh");
			$('#identity_age_3').attr("checked", false).checkboxradio("refresh");
		} else if (age_unit == '2') {
			$('#identity_age_1').attr("checked", false).checkboxradio("refresh");
			$('#identity_age_2').attr("checked", true).checkboxradio("refresh");
			$('#identity_age_3').attr("checked", false).checkboxradio("refresh");
		} else {
			$('#identity_age_1').attr("checked", false).checkboxradio("refresh");
			$('#identity_age_2').attr("checked", false).checkboxradio("refresh");
			$('#identity_age_3').attr("checked", true).checkboxradio("refresh");
		}

		$("#firstname").val(firstname);
		$("#surname").val(surname);

		$("#age").val(age);
		$("#selectDay").val(day);
		$("#selectMonth").val(month);
		$("#selectYear").val(year);

		/* Récupération du select */
		var elmt = document.getElementById('age');
		$("#age option:selected").attr("selected", '');
		/* On parcourt les options du select */
		for ( var i = 0; i < elmt.options.length; i++) {
			/* Si l'élément à la bonne valeur on le sélectionne */
			if (elmt.options[i].value == age) {

				elmt.selectedIndex = i;
				$("#age option[value='+i+']").attr("selected", "selected");

			}
		}
		$('age').selectmenu('refresh', true);
	}
}

// CHAMP LIBRE

function write_champlibre() {
	window.tagid.write_champlibre($("#textfield").val());

}

function read_champlibre() {
	var y = window.tagid.read_champlibre();
	if (y != false) {
		$("#textfield").val(y[0]);
	}
}

// LESION

function read_lesion() {
	var y = window.tagid.read_lesion();
	if (y != false) {
		var lesion_infos = y[0];

		for (i = 1; i <= 77; i++) {
			if (lesion_infos.charAt(i - 1) == '0') {
				$('#option' + i).attr("checked", false).checkboxradio("refresh");
			} else {
				$('#option' + i).attr("checked", true).checkboxradio("refresh");
			}
		}
	}
}

function write_lesion() {
	var data = "";
	for (i = 1; i < 78; i++) {
		var x = "option" + i;
		if (document.getElementById(x).checked == true) {
			data += "1";
		} else {
			data += "0";
		}
	}
	data += "000";
	var l = data.length / 4;

	window.tagid.write_lesion(data);

}

// Categories victimes

function read_victim_category() {
	var y = window.tagid.read_victim_category();
	if (y != false) {
		var category = parseInt(y[0]);
		for (i = 1; i <= 5; i++) {
			if (i != category) {
				$('input[type=radio][name=categorie_victime][value=' + i + ']').attr('checked', false).checkboxradio("refresh");
			} else {
				$('input[type=radio][name=categorie_victime][value=' + category + ']').attr('checked', true).checkboxradio("refresh");
			}
		}
	}
}

function write_victim_category() {
	var category = $('input[type=radio][name=categorie_victime]:checked').attr('value');

	if (category < 6) {
		var id = window.tagid.write_victim_category(category);
		alert(id[0]);
		if (id != false) {
			 ws.send( '{"id":"'+id+'","cat_victim":"'+category+'"}') ;
		}

	} else {
		alert('Cocher une categorie' + category);
	}
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
