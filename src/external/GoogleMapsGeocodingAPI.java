package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleMapsGeocodingAPI {
	private static final String DEFAULT_CITY = "Los Angeles";
	private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
	private static final String GEOMETRY = "geometry";

	// Google GEOCODE API URL
	private static final String GEOCODEREQUEST = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	
	private double[] getGeocode(JSONArray results) throws JSONException {
		double[] geocode = new double[2];
		for (int i = 0; i < results.length(); ++i) {
			JSONObject result = results.getJSONObject(i);
			
			JSONObject obj = result.getJSONObject(GEOMETRY).getJSONObject("location");
			geocode[0] = (obj.getDouble("lat"));
			geocode[1] = (obj.getDouble("lng"));
		}
		return geocode;
	}
	
	public double[] search(String city, String API_KEY) {
		if (city == null || city.equals("")) {
			city = DEFAULT_CITY;
		}
		try {
			city = URLEncoder.encode(city, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		// Find geocode from the user's selected city
		// https://maps.googleapis.com/maps/api/geocode/json?address=los%20angeles&key=YOUR_API_KEY
		String query = String.format("address=%s&key=%s", city, API_KEY);

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
				JSONArray results = obj.getJSONArray("results");
				return getGeocode(results);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return new JSONArray();
		return new double[2];
    }
}
