var agent = {	
		
		getAgent: function(matricule) {
			var result;
			cordova.exec(function(success) {
				result = success;
				$.mobile.hidePageLoadingMsg();
			}, function(error) {
				result = false;
				Toast.shortshow(error);
				$.mobile.hidePageLoadingMsg();
	
			}, "AgentPlugin", "getAgent", [ matricule ]);
			return result;
		},
};

