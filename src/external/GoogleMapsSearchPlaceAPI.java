package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Place;
import entity.Place.PlaceBuilder;

public class GoogleMapsSearchPlaceAPI {
	private static final String API_KEY = "Please find this in group docs";
	private static final String DEFAULT_TYPE = "Downtown";
	private static final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final String RADIUS = "50000"; // unit is meter
	
	private static final String ADDRESS = "vicinity";
	private static final String GEOMETRY = "geometry";
	private static final String ICON = "icon";
	private static final String NAME = "name";
	private static final String PLACE_ID = "place_id";
	private static final String RATING = "rating";
	private static final String TYPES = "types";
	private static final String PHOTOS = "photos";
	private static final String PHOTOREFERENCE = "photo_reference";
	// Google Place Photo API URL
	private static final String PHOTOREQUEST = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
	
	
	// https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=PHOTOREFERENCE&key=YOUR_API_KEY
	private Set<String> getPhotos(JSONArray array) throws JSONException {
		Set<String> photos = new HashSet<>();
		if (array != null && array.length() != 0) {
			for (int i = 0; i < array.length(); i++) {
				String photo = array.getJSONObject(i).getString(PHOTOREFERENCE);
				String photoUrl = PHOTOREQUEST + photo + "&key=" + API_KEY;
				photos.add(photoUrl);
			}
		}
		return photos;
	}

	
	// "types" : [ "travel_agency", "restaurant", "food", "establishment" ]
	private Set<String> getTypes(JSONArray array) throws JSONException {
		Set<String> types = new HashSet<>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				// JSONObject type = array.getJSONObject(i);
				types.add(array.getString(i));
			}
		}
		return types;
	}
	
	// Convert JSONArray to a list of places.
	private List<Place> getPlaceList(JSONArray places, String city) throws JSONException {
		List<Place> placeList = new ArrayList<>();	
		
		for (int i = 0; i < places.length(); ++i) {
			JSONObject place = places.getJSONObject(i);
			
			PlaceBuilder builder = new PlaceBuilder();
			if (!place.isNull(TYPES)) {
				builder.setTypes(getTypes(place.getJSONArray(TYPES)));
			}
			if (!place.isNull(PHOTOS)) {
				builder.setPhotos(getPhotos(place.getJSONArray(PHOTOS)));
			}
			builder.setAddress(place.getString(ADDRESS));
			builder.setName(place.getString(NAME));
			builder.setPlaceId(place.getString(PLACE_ID));
			builder.setRating(place.getDouble(RATING));
			builder.setIcon(place.getString(ICON));
			builder.setTypes(getTypes(place.getJSONArray(TYPES)));
			
			JSONObject obj = place.getJSONObject(GEOMETRY).getJSONObject("location");
			builder.setLat(obj.getDouble("lat"));
			builder.setLon(obj.getDouble("lng"));
			builder.setCity(city);
			
			placeList.add(builder.build());
		}
		return placeList;
	}
	
	
	public List<Place> search(String placeName, String city) {
		if (placeName.equals("")) {
			placeName = DEFAULT_TYPE; // Current is "Downtown"
		}
		try {
			placeName = URLEncoder.encode(placeName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		GoogleMapsGeocodingAPI geocodeapi = new GoogleMapsGeocodingAPI();
		double[] geocode = geocodeapi.search(city, API_KEY);
		
		// Process input string first to remove extra spaces and convert space to %20
		String keyword = processString(placeName);
		String query = String.format("location=%s,%s&radius=%s&keyword=%s&key=%s", geocode[0], geocode[1], RADIUS, keyword, API_KEY);

	    
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL + "?" + query).openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			System.out.println("Sending 'GET' request to URL: " + URL + "?" + query);
			System.out.println("Response Code: " + responseCode);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONObject obj = new JSONObject(response.toString());
			if (obj != null) {
				JSONArray places = obj.getJSONArray("results");
				return getPlaceList(places, city);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
    }
	
	// Process input string first to remove extra spaces and convert space to %20
	private String processString(String s) {
		if (s == null || s.length() == 0) return "";
		char[] array = s.toCharArray();
		int left = 0, right = 0;
		while (right < array.length) {
			if (array[right] == ' ' && (right == 0 || array[right -  1] == ' ')) {
				right++;
			} else {
				array[left++] = array[right++];
			}
		}
		StringBuilder sb = new StringBuilder();
		int end = left;
		if (left > 0 && array[array.length - 1] == ' ') {
			end = left - 1;
		}
		for (int i = 0; i < end; i++) {
			if (array[i] == ' ') sb.append("%20");
			else sb.append(array[i]);
		}
		return sb.toString();
	}
	
	private void queryAPI(String placeName, String placeType) {
		List<Place> placeList = search(placeName, placeType);
		try {
			for (Place place : placeList) {
				JSONObject jsonObject = place.toJSONObject();
				System.out.println(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Main entry for sample GoogleMaps API requests.
	public static void main(String[] args) {
		GoogleMapsSearchPlaceAPI googleApi = new GoogleMapsSearchPlaceAPI();
		googleApi.queryAPI("lamborghini dealer","san francisco");
	}
}
