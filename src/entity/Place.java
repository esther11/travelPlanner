package entity;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Place {
	private String place_id;
	private String name;
	private Double rating;
	private String address;
	private String icon;
	private Double lon;
	private Double lat;
	private Set<String> types;
	private Set<String> photos;
	private String city;
	
	// builder pattern
	private Place(PlaceBuilder builder) {
		this.place_id = builder.place_id;
		this.name = builder.name;
		this.rating = builder.rating;
		this.address = builder.address;
		this.icon = builder.icon;
		this.lon = builder.lon;
		this.lat = builder.lat;
		this.types = builder.types;
		this.photos = builder.photos;
		this.city = builder.city;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("place_id", place_id);
			obj.put("name", name);
			obj.put("rating", rating);
			obj.put("address", address);
			obj.put("icon", icon);
			obj.put("lon", lon);
			obj.put("lat", lat);
			obj.put("types", new JSONArray(types));
			obj.put("photos", new JSONArray(photos));
			obj.put("city", city);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public String getPlaceId() {
		return place_id;
	}

	public String getName() {
		return name;
	}

	public Double getRating() {
		return rating;
	}

	public String getAddress() {
		return address;
	}
	
	public String getIcon() {
		return icon;
	}

	public Double getLon() {
		return lon;
	}

	public Double getLat() {
		return lat;
	}
	
	public Set<String> getTypes() {
		return types;
	}
	
	public Set<String> getPhotos() {
		return photos;
	}
	
	public String getCity() {
		return city;
	}
	
	public static class PlaceBuilder {
		private String place_id;
		private String name;
		private Double rating;
		private String address;
		private String icon;
		private Double lon;
		private Double lat;
		private Set<String> types;
		private Set<String> photos;
		private String city;

		public void setPlaceId(String place_id) {
			this.place_id = place_id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setRating(Double rating) {
			this.rating = rating;
		}

		public void setAddress(String address) {
			this.address = address;
		}
		
		public void setIcon(String icon) {
			this.icon = icon;
		}

		public void setLon(Double lon) {
			this.lon = lon;
		}
		
		public void setLat(Double lat) {
			this.lat = lat;
		}
		
		public void setTypes(Set<String> types) {
			this.types = types;
		}
		
		public void setPhotos(Set<String> photos) {
			this.photos = photos;
		}

		public void setCity(String city) {
			this.city = city;
		}
		
		public Place build() {
			return new Place(this);
		}
	}

}
