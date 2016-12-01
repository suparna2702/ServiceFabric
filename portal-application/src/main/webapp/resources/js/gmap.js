var infowindow;
var lastLat;
var lastLong;
var lastContent;

function showInfoWindow(gmap, lat, lng, contentString) {

	if (!lat || lat.length == 0 || !lng || lng.length == 0) {
		return;
	}


	if(lat == lastLat && lng == lastLong && contentString == lastContent)
		return;
	
	lastLat = lat;
	lastLong = lng;
	lastContent = contentString;
	
	
	var gMapLatlng = new google.maps.LatLng(lat, lng);

	if(infowindow) {
		infowindow.close();
		infowindow=null;
	}
	
	var marker = new google.maps.Marker({
		position : gMapLatlng,
		map : gmap,
		title : name
	});

	infowindow = new google.maps.InfoWindow({
		content : contentString
	});

	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(gmap, marker);
	});

	infowindow.open(gmap, marker);
}

function showSearchResultInfoWindow(gmap, lat, lng, name, subtitle, street,
		city, state, zip, country, phone1, phone2, email, url) {

	var contentString = '<div id="content">' + '<h2>' + name + '</h2>'
			+ '<div id="bodyContent">' + subtitle + '<BR/>' + street + '<BR/>'
			+ city + '<BR/>' + state + '<BR/>' + zip + '<BR/>' + country
			+ '<BR/>' + phone1 + '<BR/>' + phone2 + '<BR/>' + email + '<BR/>'
			+ url + '<BR/>' + '</div></div>';

	showInfoWindow(gmap, lat, lng, contentString);
	
}

function showSearchResultInfoWindowReduced(gmap, lat, lng, name, subtitle) {

	var contentString = '<div id="content">' + '<h2>' + name + '</h2>'
			+ '<div id="bodyContent">' + subtitle + '</div></div>';

	showInfoWindow(gmap, lat, lng, contentString);

}
