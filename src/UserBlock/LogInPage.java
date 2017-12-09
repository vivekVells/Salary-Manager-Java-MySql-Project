package UserBlock;
import MainHub.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
 
public class LogInPage {
	
	private static Connection cxn= ConnectionEstablish.ConnectToDB.getMySqlConnection();;
	private static Statement st;
	private static ResultSet rs;
	static Scanner scan = new Scanner(System.in);
	private static String username = null;
	private static String password = null;
	private static int userId; 
	public static boolean isValidUser;
	
	public static void logInScreenBanner() {
		System.out.println("\t\t\t\t\t\t  LogIn Page");
		System.out.println();
	}
	
	public static void getUserCredentials() throws SQLException {
		logInScreenBanner();
		System.out.print("Input UserName: " );
		username = scan.nextLine();
		System.out.print("Input Password: ");
		password = scan.nextLine();
		try {
			validateUser();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected static void setValidatedUserId(int userIdArg) {
		userId = userIdArg;
	}
	
	public static int getValidatedUserId() {
		return userId;
	}
	
	public static void validateUser() throws SQLException, InterruptedException {
		st = cxn.createStatement();

		String getAllUserQuery = "SELECT * FROM USER";
		rs = st.executeQuery(getAllUserQuery);
		
		System.out.println("\n\nValidating the user...");
		
		while(rs.next()) {
			if( username.equals(rs.getString("username")) 
					&& password.equals(rs.getString("password")) ){
				userId = rs.getInt("userId");
				isValidUser = true;
				break;
			}else {
				isValidUser = false;
			}		
		}
		
		if(!isValidUser) { 
			userId=0;
			setValidatedUserId(userId);
			System.out.println("Invalid Username or Password!!!\nTry Again!!!");
			System.out.println("Redirecting to LogIn Page menu....");
			Thread.sleep(2000);
			Main.enableNewLineTrick();
			Main.welcomeScreenBanner();
			Main.getInput();
		}else {
			setValidatedUserId(userId);			
			UserBlock.UserLogDirectory.userLogIn();
			System.out.println("Login successfull....");
			//Thread.sleep(500);
			
			System.out.println("Redirecting to view/transaction menu....");
			Thread.sleep(500);
			
			System.out.println("Initiating DB environment values...");
			//ConnectionEstablish.MySqlEnvSetup.mySqlEnvInitialize();
			Thread.sleep(1000);
			Main.selectViewOrTransactionMenu();
		}
	}
	
	public static void main(String...args) {}
}
