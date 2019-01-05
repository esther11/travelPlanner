package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);

			if (conn == null) {
				return;
			}

			// Step 2 Drop tables in case they exist.
			Statement stmt = conn.createStatement();

			String sql = "DROP TABLE IF EXISTS favorites";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS types";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS photos";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS places";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS users";
			stmt.executeUpdate(sql);

			// Step 3 Create new tables
			sql = "CREATE TABLE places ("
					+ "place_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "rating FLOAT,"
					+ "address VARCHAR(255),"
					// + "url VARCHAR(255),"  discuss with FrontEnd
					+ "icon VARCHAR(255),"
					+ "latitude FLOAT,"
					+ "longitude FLOAT,"
					+ "city VARCHAR(255),"
					+ "PRIMARY KEY (place_id)"
					+ ")";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE photos ("
					+ "place_id VARCHAR(255) NOT NULL,"
					+ "photo VARCHAR(700),"
					+ "PRIMARY KEY (place_id, photo),"
					+ "FOREIGN KEY (place_id) REFERENCES places(place_id)"
					+ ")";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE types ("
					+ "place_id VARCHAR(255) NOT NULL,"
					+ "type VARCHAR(255),"
					+ "PRIMARY KEY (place_id, type),"
					+ "FOREIGN KEY (place_id) REFERENCES places(place_id)"
					+ ")";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "username VARCHAR(255),"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE favorites ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "place_id VARCHAR(255) NOT NULL,"
					+ "access_order INT,"
					+ "city VARCHAR(255),"
					+ "PRIMARY KEY (user_id, place_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
					+ "FOREIGN KEY (place_id) REFERENCES places(place_id)"
					+ ")";
			stmt.executeUpdate(sql);

			// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO users VALUES ('1111', '3229c1097c00d497a0fd282d586be050', 'JohnSmith')";
			stmt.executeUpdate(sql);

			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
