package ConnectionEstablish;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDB {
	public static Connection getMySqlConnection() {
		Connection cxn = null;
		
		try {
			// registering mysql driver...
			//System.out.println("Loading mysql JDBC Driver settings...");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//System.out.println("Error while registering mysql driver...");
			e.printStackTrace();
		}
		//System.out.println("mysql driver registered...");
		//System.out.println("Establishing connection...");
		
		String dbUrl = "jdbc:mysql://localhost:3306/";
		String databaseSelected = "salary_manager_oops";
		String avoidServerVerification = "?verifyServerCertificate=false&useSSL=true";
		String dbUsername = "dbManager";
		String dbPassword = "dbManager";
		try {
			cxn = DriverManager.getConnection(dbUrl + databaseSelected + avoidServerVerification, dbUsername, dbPassword);
		} catch (SQLException e) {
			System.out.println("Error while establishing connection to " + databaseSelected + "...");
			e.printStackTrace();
		}
		//System.out.println("Connection established to " + databaseSelected + "...");
		return cxn;
	}
		
	public static void main(String...args) {}
}
