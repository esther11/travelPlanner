package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;

import db.DBConnection;

public class MySQLConnection implements DBConnection{

	private Connection conn;
	
	public MySQLConnection() {
		try {
				Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
				conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
