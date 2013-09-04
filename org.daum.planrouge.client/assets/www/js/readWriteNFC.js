// Victim Category

function read_victim_category() {
	var y = window.tagid.read("category");
	if (y != false) {
		var category = parseInt(y[0]);
		
		for (i = 1; i <= 5; i++) {
			if (i != category) {
				$('input[type=radio][name=categorie_victime][value=' + i + ']').prop('checked', false).checkboxradio("refresh");
				$('#cat'+i).css({"opacity":"0.3"});
			} else {
				$('#cat'+i).css({"opacity":"1.0"});
				$('input[type=radio][name=categorie_victime][value=' + category + ']').prop('checked', true).checkboxradio("refresh");
			}
		}
	}
}


function write_victim_category() {
	var category = $('input[type=radio][name=categorie_victime]:checked').attr('value');
	if (category < 6) {
		window.tagid.write("category",category);
	} else {
		alert('Cocher une categorie');
	}
}


//Identity

function writeIdentity() {
	var sexe = $('input[type=radio][name=identity_sexe]:checked').attr('value');
	var age = document.getElementById("age");
	age = age.options[age.selectedIndex].value
	
	var day = document.getElementById("selectDay");
	var year = document.getElementById("selectYear");
	var month = document.getElementById("selectMonth");
	year = year.options[year.selectedIndex].value;
	month = month.options[month.selectedIndex].value
	day = day.options[day.selectedIndex].value
	var birthday = day+ month+year;
	if (year != '0000'){
			
		var myDate = new Date();

		var dateday = myDate.getDate();
		var datemonth = myDate.getMonth() + 1;
		var dateyear = myDate.getFullYear();
		
		var age = dateyear-year;
		
		if(month>datemonth){
			age= age-1;
		}else if(month == datemonth){
			if (day>dateday){
				age=age-1;
			}
		}
	}
	var name = $("#surname").val();
	var firstname = $("#firstname").val();
	

	window.tagid.write("identity",firstname, name, sexe, age, birthday);
}

function readIdentity() {

	var y = window.tagid.read("identity");
	if (y != false) {
		var firstname = y[0];
		var surname = y[1];
		var sexe = y[2];
		var age = y[3];
		var birthday = y[4];
		var day = birthday.substring(0, 2);
		var month = birthday.substring(2, 4);
		var year = birthday.substring(4, 8);

		if (sexe == '1') {
			$('input[type=radio][name=identity_sexe][value=' + sexe + ']').prop('checked', true).checkboxradio("refresh");
			$('input[type=radio][name=identity_sexe][value=' + 2 + ']').prop('checked', false).checkboxradio("refresh");
		} else{
			$('input[type=radio][name=identity_sexe][value=' + sexe + ']').prop('checked', true).checkboxradio("refresh");
			$('input[type=radio][name=identity_sexe][value=' + 1 + ']').prop('checked', false).checkboxradio("refresh");
		} 	
		
		$("#firstname").val(firstname);
		$("#surname").val(surname);

		$("#selectDay option[value='"+day+"']").prop("selected", true);
		$('#selectDay').selectmenu('refresh', true);

		$("#selectDay option[value='"+day+"']").prop("selected", true);
		$('#selectDay').selectmenu('refresh', true);
		
		$("#selectMonth option[value='"+month+"']").prop("selected", true);
		$('#selectMonth').selectmenu('refresh', true);

		$("#selectYear option[value='"+year+"']").prop("selected", true);
		$('#selectYear').selectmenu('refresh', true);
		

		while(age.toString().length<3){
			age='0'+age;	
		}
		$("#age option[value='"+age+"']").prop("selected", true);
		$('#age').selectmenu('refresh', true);

	}
}


//GPS & Hours

function writeDateHoursGps(gpsHours) {

	navigator.geolocation.getCurrentPosition(function(position) {
		var time = showTime();
		var coordonneesGPS 
		var lat = Math.round((position.coords.latitude) * 10000000);
		var long = Math.round((position.coords.longitude) * 10000000);
		var latitude = lat;
		var longitude = long;
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

		window.tagid.write("gpsHours",time, latitude,longitude,signe , accuracy);

	}, function() {
		alert('code: ' + error.code + '\n' + 'message: ' + error.message + '\n');
	});

}

function traitementGPS(latitude1, longitude1,accuracy1) {

		var time = showTime();
		var latitude = Math.round(latitude1 * 10000000);
		var longitude = Math.round(longitude1 * 10000000);

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
		
	

		
		window.tagid.write("gpsHours",time, latitude,longitude,signe , accuracy1);

		
}



function writeDateHoursGps(gpsHours){
	startLoading();
	
	
	
	var distanceRef = window.localStorage.getItem("distance");
	var date1 = gpsHours[0];
	var date2 = gpsHours[4];
	var date3 = gpsHours[8];
	var date4 = gpsHours[12];
	var gps;


	navigator.geolocation.getCurrentPosition(function(position) {
		var lat = position.coords.latitude;
		var long= position.coords.longitude;
		var accuracy = Math.round(position.coords.accuracy);
	if(parseInt(date4)!=0){

		var lat4 = gpsHours[13]/10000000;
		var long4 = gpsHours[14]/10000000;
		var accuracy4 = gpsHours[15];
		//alert("DISTANCE 4: "+lat+"  "+long+"  "+lat4+"  "+long4+"     "+distance(lat, long, lat4, long4));
		if(distance(lat, long, lat4, long4)> distanceRef){
			traitementGPS(lat, long,accuracy);
		}else{
			stopLoading();
		}
	}else if(parseInt(date3)!=0){

		var lat3 = gpsHours[9]/10000000;
		var long3 = gpsHours[10]/10000000;
		var accuracy3 = gpsHours[11];
		//alert("DISTANCE 3: "+lat+"  "+long+"  "+lat3+"  "+long3+"     "+distance(lat, long, lat3, long3));
		
		if(distance(lat, long, lat3, long3)>distanceRef){
			traitementGPS(lat, long,accuracy);
		}else{
			stopLoading();
		}
	}else if (parseInt(date2)!=0){
		
		var lat2 = gpsHours[5]/10000000;
		var long2 = gpsHours[6]/10000000;
		var accuracy2 = gpsHours[7];
		//alert("DISTANCE 2: "+lat+"  "+long+"  "+lat2+"  "+long2+"     "+distance(lat, long, lat2, lon2));
		if(distance(lat, long, lat2, long2)>distanceRef){
			traitementGPS(lat, long,accuracy);
		}else{
			stopLoading();
		}
	}else if (parseInt(date1)!=0){

		var lat1 = gpsHours[1]/10000000;
		var long1 = gpsHours[2]/10000000;		
		var accuracy1 = gpsHours[3];
	//	alert("DISTANCE 1: "+lat+"  "+long+"  "+lat1+"  "+long1+"     "+(distance(lat, long, lat1, long1));

		
		if( distance(lat, long, lat1, long1) > distanceRef){ 
			traitementGPS(lat, long,accuracy);
		}else{
			stopLoading();
		}
	}else {
		//alert("DATE 0");
		traitementGPS(lat, long,accuracy);
	}
	
	}, function() {
		alert('ERROR GPS');
	});
	
}

function readDateHoursGps() {

	var y = window.tagid.read("gpsHours");
	if (y != false) {
		
		var date1 = new Date(parseInt(y[0]));
		var latitude1 = y[1]/10000000;
		var longitude1 = y[2]/10000000;
		var accuracy1 = y[3];
		if(parseInt(y[0])==0){
			date1='-----'
		}
		
		var date2 = new Date(parseInt(y[4]));
		if(parseInt(y[4])==0){
			date2='-----'
		}
		
		var latitude2 = y[5]/10000000;
		var longitude2 = y[6]/10000000;
		var accuracy2 = y[7];
		
		var date3 = new Date(parseInt(y[8]));
		var latitude3 = y[9]/10000000;
		var longitude3 = y[10]/10000000;
		var accuracy3 = y[11];
		if(parseInt(y[8])==0){
			date3='-----'
		}
		
		var date4 = new Date(parseInt(y[12]));	
		var latitude4 = y[13]/10000000;
		var longitude4 = y[14]/10000000;
		var accuracy4 = y[15];
		if(parseInt(y[12])==0){
			date4='-----'
		}
		
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


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//LESION

function read_lesion() {
	var y = window.tagid.read('lesion');
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

	window.tagid.write('lesion',data);

}



//TextField

function write_champlibre() {
	window.tagid.write('textField',$("#textfield").val());

}

function read_champlibre() {
	var y = window.tagid.read('textField');
	if (y != false) {
		$("#textfield").val(y[0]);
	}
}


//Destination

function write_destination() {
	var evacuation = $('input[type=radio][name=evacuation]:checked').attr('value');

	window.tagid.write('destination',evacuation, $("#destination").val(), $("#ville_destination").val(), $("#code_postal").val());
}

function read_destination() {

	var y = window.tagid.read('destination');
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


//Bilan complementaire
function getInfoBilanComplementaire() {

	var y = window.tagid.read('bilanComplementaire');
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

	window.tagid.write('bilanComplementaire', data, $("#oxygene").val(), $("#chocs").val(), $("#immobilisation").val(), $("#extension").val(), $(
			"#maladies").val(), $("#hospitalisations").val(), $("#traitements").val(), $("#allergies").val());
}


//vital urgency
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

	window.tagid.write('bilanUrgence',data, $("#perte_connaissance").val(), $("#freq_respiratoire").val(), $("#saturation").val(), $("#freq_cardiaque").val(), $(
			"#pression_arterielle").val());

}

function getInfoVitalEmergency() {

	var y = window.tagid.read('bilanUrgence');
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