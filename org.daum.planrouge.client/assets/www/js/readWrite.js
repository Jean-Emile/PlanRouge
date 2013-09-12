var r = true;

function WriteCancel(){
	$.mobile.hidePageLoadingMsg();
	r = false;
	cordova.exec(function(success) {}, function(error) {}, "ReadWritePlugin", "raz", []);
}

function alertCallback(buttonIndex, type, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10) {
	alert('Envoi Annulé' + buttonIndex);

	window.tagid.write(type, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10);
}

var tagid = {	
	read : function(type, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10) {
		var x;

		cordova.exec(function(success) {
			//Toast.shortshow(success);
			x = success;
		}, function(error) {
			x = false;
			Toast.shortshow(error);

		}, "ReadWritePlugin", "read", [ type, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10 ]);
		return x;
	},

	write : function(type, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10) {
		var x;

		cordova.exec(function(success) {

			x = success;

			$.mobile.hidePageLoadingMsg();	


			if(success[1] != 'GPS'){
			
				writeDateHoursGps(success[1]);
			}else {
				window.location = "file:///android_asset/www/waitTag/index.html";
			}

			
		}, function(error) {
			x = false;
		

			$.mobile.loading( 'show', {
				text: 'Approcher le TAG !! ',
				textVisible: true,
				theme: 'e',
				html: "<div style=' text-align: center;'><img  src='file:///android_asset/www/images/ajax-loader.gif' /><h3>Approcher le TAG !</h3><input type='button' value='Annuler' onClick='WriteCancel();' /></div>"
			});


				setTimeout(function() { 
					if (r){
					tagid.write(type, param1, param2, param3, param4, param5, param6, param7, param8, param9, window.localStorage.getItem("matriculeAgent")) 
					}
					else {
						r = false;
					}
				}, 1000);

			
		}, "ReadWritePlugin", "write", [ type, param1, param2, param3, param4, param5, param6, param7, param8, param9, window.localStorage.getItem("matriculeAgent") ]);
		return x;
	},

};
