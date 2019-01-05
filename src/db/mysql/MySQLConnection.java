package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import entity.Place;
import entity.Place.PlaceBuilder;
import external.GoogleMapsSearchPlaceAPI;

public class MySQLConnection implements DBConnection {

	private Connection conn;

	public MySQLConnection() {
		try {
				Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
				conn = DriverManager.getConnection(MySQLDBUtil.URL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	   	 if (conn != null) {
	   		 try {
	   			 conn.close();
	   		 } catch (Exception e) {
	   			 e.printStackTrace();
	   		 }
	   	 }
	}

	@Override
	public void setFavoritePlaces(String userId, List<String> placeIds, String city) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		int largestOrder = 0;
		try {
			String sql = "SELECT MAX(access_order) FROM favorites WHERE user_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			// if there is at least one favorite place for this user, get the order for further use
			if (rs.next()) {
				largestOrder = rs.getInt(1);
			}

			for (String placeId : placeIds) {
				updateOrder(userId, placeId, ++largestOrder, city);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOtherCityFavoritePlaces(String userId, String city) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		
		try {
			String sql = "DELETE FROM favorites WHERE user_id = ? AND city <> ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, city);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void unsetFavoritePlaces(String userId, List<String> placeIds) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {
			// delete rows first
			String sql = "DELETE FROM favorites WHERE user_id = ? AND place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			for (String placeId : placeIds) {
				ps.setString(2, placeId);
				ps.execute();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateFavoritePlaces(String userId, List<String> placeIds, String city) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		int largestOrder = 0;
		try {
			for (String placeId : placeIds) {
				updateOrder(userId, placeId, ++largestOrder, city);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getFavoritePlaceIds(String userId) {
		// TODO Auto-generated method stub
		if(conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}

		List<String> favoritePlaces = new ArrayList<>();

		try {
			String sql = "SELECT * FROM favorites WHERE user_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String placeId = rs.getString("place_id");
				favoritePlaces.add(placeId);
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return favoritePlaces;
	}

	@Override
	public List<Place> getFavoritePlaces(String userId) {
		// TODO Auto-generated method stub
		if(conn == null) {
			return new ArrayList<>();
		}

		List<Place> favoritePlaces = new ArrayList<>();
		List<String> placeIds = getFavoritePlaceIds(userId);
		try {
			String sql = "SELECT * FROM places WHERE place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			for(String placeId : placeIds) {
				ps.setString(1, placeId);
				ResultSet rs = ps.executeQuery();
				PlaceBuilder builder = new PlaceBuilder();
				while(rs.next()) {
					builder.setPlaceId(rs.getString("place_id"));
					builder.setName(rs.getString("name"));
					builder.setRating(rs.getDouble("Rating"));
					builder.setAddress(rs.getString("address"));
					builder.setIcon(rs.getString("icon"));
					builder.setLat(rs.getDouble("latitude"));
					builder.setLon(rs.getDouble("longitude"));
					builder.setTypes(getTypes(rs.getString("place_id")));
					builder.setPhotos(getPhotos(rs.getString("place_id")));
					builder.setCity(rs.getString("city"));

					favoritePlaces.add(builder.build());
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return favoritePlaces;
	}

	@Override
	public Set<String> getPhotos(String placeId) {
		// TODO Auto-generated method stub
		if(conn == null) {
			return null;
		}
		Set<String> photos = new HashSet<>();
		try {
			String sql = "SELECT photo from photos WHERE place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, placeId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String photo =rs.getString("photo");
				photos.add(photo);
			}

		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return photos;
	}

	@Override
	public Set<String> getTypes(String placeId) {
		// TODO Auto-generated method stub
		if(conn == null) {
			return null;
		}
		Set<String> types = new HashSet<>();
		try {
			String sql = "SELECT type from types WHERE place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, placeId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String type =rs.getString("type");
				types.add(type);
			}

		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return types;
	}

	@Override
	public void updateOrder(String userId, String placeId, int order, String city) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {
			String sql = "SELECT * FROM favorites WHERE user_id = ? AND place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, placeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) { // if it is already in the table, update the order
				try {
					sql = "UPDATE favorites f "
							+ "SET f.access_order = ? "
							+ "WHERE f.user_id = ? AND f.place_id = ?";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setInt(1, order);
					stmt.setString(2, userId);
					stmt.setString(3, placeId);
					stmt.setString(4, city);
					stmt.execute();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { // it it is not in the table yet, insert the column with new order
				try {
					sql = "INSERT IGNORE INTO favorites(user_id, place_id, access_order, city) VALUES (? ,? ,?, ?)";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, userId);
					stmt.setString(2, placeId);
					stmt.setInt(3, order);
					stmt.setString(4, city);
					stmt.execute();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String checkOrder(String userId, List<String> placeIds) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public List<Place> searchPlaces(String placeName, String city) {
		// TODO Auto-generated method stub
		GoogleMapsSearchPlaceAPI googleMapsSearchPlaceAPI = new GoogleMapsSearchPlaceAPI();
		List<Place> places = googleMapsSearchPlaceAPI.search(placeName, city);

		for (Place place : places) {
			savePlace(place);
		}

		return places;
	}

	@Override
	public void savePlace(Place place) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {
			String sql = "INSERT IGNORE INTO places VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, place.getPlaceId());
			ps.setString(2, place.getName());
			ps.setDouble(3, place.getRating());
			ps.setString(4, place.getAddress());
			ps.setString(5, place.getIcon());
			ps.setDouble(6, place.getLat());
			ps.setDouble(7, place.getLon());
			ps.setString(8, place.getCity());
			ps.execute();

			if (place.getTypes() != null) {
				sql = "INSERT IGNORE INTO types VALUES(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, place.getPlaceId());
				for (String type : place.getTypes()) {
					ps.setString(2, type);
					ps.execute();
				}
			}

			if (place.getPhotos() != null) {
				sql = "INSERT IGNORE INTO photos VALUES(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, place.getPlaceId());
				for (String photo : place.getPhotos()) {
					ps.setString(2, photo);
					ps.execute();
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		if (conn == null) {
			return null;
		}

		String name = null;
		try {
			String sql = "SELECT username FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				name = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addUser(String userId, String password, String username) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			statement.setString(3, username);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean verifyUserId(String userId) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) { // if user doesn't exist, return true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
