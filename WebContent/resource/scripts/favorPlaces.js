var user_id = '1111';
var placeList = [];
var map;
var infowindow;
var directionsService;
var directionsDisplay;

function initialize() {
	// Register event listeners
	document.getElementById("panel-openbtn").addEventListener('click', openNav);
	document.getElementById("panel-closebtn").addEventListener('click', closeNav);

	// Initialize map and infowindow
    var mapOptions = {
        // Location of Los Angeles
        center: new google.maps.LatLng(34.067727, -118.211488),
        zoom: 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        mapOptions);

    infowindow = new google.maps.InfoWindow();

    // Initialize directions service and display
    directionsService = new google.maps.DirectionsService;
	directionsDisplay = new google.maps.DirectionsRenderer({
        map: map,
        suppressMarkers: true
    });

	// Fetch data of user's favorite places from DB
	loadFavoritePlaces();
}

// AJAX helper
function ajax(method, url, data, callback, errorHandler) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.onload = function() {
    	if (xhr.status === 200) {
			callback(xhr.responseText);
		} else {
			errorHandler();
		}
    };
    xhr.onerror = function() {
        console.error("The request couldn't be completed.");
        errorHandler();
    };
    if (data === null) {
        xhr.send();
    } else {
        xhr.setRequestHeader("Content-Type",
            "application/json;charset=utf-8");
        xhr.send(data);
    }
}

function loadFavoritePlaces() {
    var url = 'http://localhost:8080/travelPlanner/favorite';
    var params = 'user_id=' + user_id;
    var req = JSON.stringify({});

    // make AJAX call
    ajax('GET', url + '?' + params, req, function(res) {
        var places = JSON.parse(res);
        if (!places || places.length === 0) {
            alert('No favorite places added, turn to SEARCH page to add your favorite places');
        } else {
            placeList = places;
            addMarkers();
            // by setting last argument as true, we use Google recommended routes
            renderRoutes(directionsService, directionsDisplay, true);
            console.log(placeList);
        }
    }, function() {
        alert('Cannot load favorite places.');
    });
}

// Render routes according to place list
function renderRoutes(directionsService, directionsDisplay, optimizeWaypoints) {
	if (placeList.length <= 1) return;

	var waypoints = [];
	for (var i = 1; i < placeList.length - 1; i++) {
		waypoints.push({
			location: {placeId: placeList[i].place_id},
			stopover: true
		})
	}

	directionsService.route({
		origin: {placeId: placeList[0].place_id},
		destination: {placeId: placeList[placeList.length - 1].place_id},
		waypoints: waypoints,
		optimizeWaypoints: optimizeWaypoints,
		travelMode: 'DRIVING'
	}, function(response, status) {
		if (status === 'OK') {
			directionsDisplay.setDirections(response);
			// if we choose to use Google recommended routes,
			// re-order place list and re-render the place panel accordingly
			if (optimizeWaypoints) {
				waypointOrder = response.routes[0].waypoint_order;
				sortPlaceList(waypointOrder);
				renderPlacePanel();
			}
		} else {
			window.alert('Directions request failed due to ' + status);
		}
	});
}

// Rearrange order of place list according to recommended waypoint order
function sortPlaceList(waypointOrder) {
	var newPlaceList = [];
	newPlaceList.push(placeList[0]);
	for (var i = 0; i < waypointOrder.length; i++) {
		newPlaceList.push(placeList[waypointOrder[i] + 1]);
	}
	newPlaceList.push(placeList[placeList.length - 1]);
	placeList = newPlaceList;
}

// Render place panel according to place list
function renderPlacePanel() {
	var ul = document.getElementById('place-list');
	ul.innerHTML = '';

	for (var i = 0; i < placeList.length; i++) {
		var placeName = placeList[i].name;
		var placeId = placeList[i].place_id;
		var placeIcon = placeList[i].icon;
		var placeAddress = placeList[i].address;

		// create a list item and set attributes
		var li = document.createElement('li');
		li.setAttribute("class", "place");
		li.setAttribute("id", placeId);

		// add content in the list item
		var icon = li.appendChild(document.createElement('img'));
		icon.setAttribute("src", placeIcon);
		var section = li.appendChild(document.createElement('div'));
		var title = section.appendChild(document.createElement('p'));
		title.innerHTML = placeName;
		title.setAttribute("class", "place-title");
		var address = section.appendChild(document.createElement('p'));
		address.innerHTML = placeAddress;
		address.setAttribute("class", "place-address");

		ul.appendChild(li);
	}
}

// Display customized markers on map and add them to markers[]
function addMarkers() {
	for (var i = 0; i < placeList.length; i++) {
		displayMarkerWithTimeout(placeList[i].place_id, placeList[i].lat, placeList[i].lon, placeList[i].name, placeList[i].address, i * 100);
	}	
}

// Display a marker on map after specified timeout
function displayMarkerWithTimeout(lat, lng, title, address, timeout) {
	window.setTimeout(function() {
		var marker = new google.maps.Marker({
			position: {lat: lat, lng: lng},
			map: map,
			title: title,
			animation: google.maps.Animation.DROP
		});

		console.log(marker);

		marker.addListener('mouseover', function() {
			infowindow.setContent("<p style='font-weight: bold;'>" + title
					+ "</p><p>" + address + "</p>"
			);
		    infowindow.open(map, marker);
		});
	}, timeout);
}

// Open side panel of the place list
function openNav() {
    document.getElementById("panel").style.marginLeft = "0px";
}

// Close side panel of the place list
function closeNav() {
    document.getElementById("panel").style.marginLeft = "-310px";
}