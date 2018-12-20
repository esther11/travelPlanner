package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;
import external.GoogleMapsSearchPlaceAPI;
import rpc.RpcHelper;

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
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void setFavoritePlaces(String userId, List<String> placeIds) {
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
				updateOrder(userId, placeId, ++largestOrder);
			}
			
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
		
		int largestOrder = 0;
		List<String> placeList = new ArrayList<>();
		try {
			// delete rows first
			String sql = "DELETE FROM favorites WHERE user_id = ? AND place_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			for (String placeId : placeIds) {
				ps.setString(2, placeId);
				ps.execute();
			}
			
			// reset order after delete
			sql = "SELECT * FROM favorites WHERE user_id = ? ORDER BY access_order ASC";					
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				placeList.add(rs.getString("place_id"));
			}
			
			for (String placeId : placeList) {
				updateOrder(userId, placeId, ++largestOrder);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateFavoritePlaces(String userId, List<String> placeIds) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		int largestOrder = 0;
		try {
			for (String placeId : placeIds) {
				updateOrder(userId, placeId, ++largestOrder);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getFavoritePlaceIds(String userId) {
		// TODO Auto-generated method stub
		// the sequence in the list relies on order!
		return null;
	}

	@Override
	public List<Place> getFavoritePlaces(String userId) {
		// TODO Auto-generated method stub
		// the sequence in the list relies on order!
		return null;
	}

	@Override
	public Set<String> getPhotos(String placeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getTypes(String placeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrder(String userId, String placeId, int order) {
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
					stmt.execute();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { // it it is not in the table yet, insert the column with new order
				try {
					sql = "INSERT IGNORE INTO favorites(user_id, place_id, access_order) VALUES (? ,? ,?)";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, userId);
					stmt.setString(2, placeId);
					stmt.setInt(3, order);
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

	// deletable
	@Override
	public List<Place> searchPlaces(String placeName, String placeType) {
		// TODO Auto-generated method stub
		return new ArrayList<Place>();
	}

	@Override
	public void savePlace(Place place) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getUsername(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
