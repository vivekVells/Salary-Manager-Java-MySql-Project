package UserBlock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserLogDirectory {
	private static Connection cxn = ConnectionEstablish.ConnectToDB.getMySqlConnection();
	private static Statement st;
	
	public static void userLogOut() { 
		int userId = LogInPage.getValidatedUserId();
	
		try {
			st = cxn.createStatement();
		} catch (SQLException e) {
			System.out.println("Connection establishment failed at user Log Directory....");
			e.printStackTrace();
		}
		LogInPage.setValidatedUserId(0);
		String getRecentUserLogId = null;
		try {
			getRecentUserLogId = "set @recent_userLogId = (select userLogId from userlog order by userLogId desc limit 1)";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String insertLogOut = "update userLog set lastLoggedOut= NOW() where userLogId=@recent_userLogId";
		try {
			st.executeUpdate(getRecentUserLogId);
			st.executeUpdate(insertLogOut);
		} catch (SQLException e) {
			System.out.println("LogOut query unsuccessfull");
			e.printStackTrace();
		}
	}
	
	public static void userLogIn() {
		int userId = LogInPage.getValidatedUserId();
		
		try {
			st = cxn.createStatement();
		}catch(SQLException e) {
			System.out.println("Connection Establishment failed at user log directory...");
			e.printStackTrace();
		}
		String insertLogIn = "insert into userlog (userId, lastLoggedIn) values(" + userId + ", NOW())";
		try {
			st.executeUpdate(insertLogIn);
		} catch (SQLException e) {
			System.out.println("LogIn query insertion unsuccessfull");
			e.printStackTrace();
		}
	}
	
	public static void main(String...args) {}
}
