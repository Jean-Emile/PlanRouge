var ipaddress = {	
		ipAddress: function(adresse) {
			Toast.shortshow("ipaddress");
			cordova.exec(function(success) {
				Toast.shortshow(success);

			}, function(error) {
				Toast.shortshow(error);
				
			}, "WebsocketPlugin", "ipAddress", [ adresse ]);
		},
};

Toast.shortshow("TEST IP ADDRESS");