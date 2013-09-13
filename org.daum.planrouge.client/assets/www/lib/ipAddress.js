var ipaddress = {	
		ipAddress: function(adresse) {
			cordova.exec(function(success) {
				Toast.shortshow(success);

			}, function(error) {
				Toast.shortshow(error);
				
			}, "WebsocketPlugin", "ipAddress", [ adresse ]);
		},
};