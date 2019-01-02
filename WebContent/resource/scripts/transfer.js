// Load
function clientSideInclude(id, url) {
	var req = false;
	if(window.XMLHttpRequest) {// Safari, Firefox, and any other browser but not belong to MS
		try {
			req = new XMLHttpRequest();
		}catch(e) {
			req = false;
		}
	}else if(window.ActiveXObject) {	
		try {
			req = new ActiveXObject("Msxml2.XMLHTTP");// For Internet Explorer on Windows
		} catch(e) {
			try{
				req= new ActiveXObject("Microsoft.XMLHTTP");
			} catch(e) {
				req = false;
			}
		}
	}
	var element = document.getElementById(id);
	if(req) {
		req.open('GET', url, false);// Synchronized request
		req.send(null);
		element.innerHTML = req.responseText;
	} else {
		element.innerHTML =
			"Please update your brower to Internet Explorer 5" +"or access the website with Firefox/Safari";
	}
}


