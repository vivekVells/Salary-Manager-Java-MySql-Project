package UserBlock;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

import MainHub.Main;

public class NewRegistration {
	private static Scanner scan = new Scanner(System.in);
	private static String username = null, password = null, recoveryEmail = null, recoveryAnswer = null;
	private static String firstName = null, middleName = null, lastName = null;
	private static BigInteger phoneNumber = null;
	private static int zipcode = 0;
	private static String street = null, city = null, state = null;
	private static HashMap<Integer, String> mapUserInfo = new HashMap<>();
	
	public static void newRegistrationScreenBanner() {
		System.out.println("\t\t\t\t\t\t  New User Account Registration Page\n");
	}
	
	public static void createNewUserAccount() {
		newRegistrationScreenBanner();
		System.out.print("username: ");
		username = scan.nextLine();
		mapUserInfo.put(1, username);
		System.out.print("password: ");
		password = scan.nextLine();
		mapUserInfo.put(2, password);
		System.out.print("Username/Password Recovery Email: ");
		recoveryEmail = scan.nextLine();
		mapUserInfo.put(3, recoveryEmail);
		System.out.print("Password Recovery Answer Key: ");
		recoveryAnswer = scan.nextLine();
		mapUserInfo.put(4, recoveryAnswer);
		
		System.out.print("First Name: ");
		firstName = scan.nextLine();
		mapUserInfo.put(5, firstName);
		System.out.print("Middle Name: ");
		middleName = scan.nextLine();
		mapUserInfo.put(6, middleName);
		System.out.print("Last Name: ");
		lastName = scan.nextLine();
		mapUserInfo.put(7, lastName);
		System.out.print("Phone Number: ");
		phoneNumber = scan.nextBigInteger(); 
		scan.nextLine();
		mapUserInfo.put(8, String.valueOf(phoneNumber));
		
		System.out.print("Street: ");
		street = scan.nextLine();
		mapUserInfo.put(9, street);
		System.out.print("City: ");
		city = scan.nextLine();
		mapUserInfo.put(10, city);
		System.out.print("State: ");
		state = scan.nextLine();
		mapUserInfo.put(11, state);
		System.out.print("Zipcode: ");
		zipcode = scan.nextInt(); scan.nextLine();
		mapUserInfo.put(12, String.valueOf(zipcode));
		System.out.print(mapUserInfo);
		redirectRegisterOptions();
	}
		
	public static void redirectRegisterOptions() {
		int userId = UserBlock.LogInPage.getValidatedUserId();
		Connection con = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		try {
			Statement stmt1 = con.createStatement();
			System.out.println();
			// inserting into user table
			String insertIntoUserTbl = "insert into user(username, password, recoveryEmail, recoveryAnswer) values(" 
					+ "'" + username + "'" + "," + "'" + password + "'" + "," + "'" + recoveryEmail + "'" + "," + "'" + recoveryAnswer + "'" +")";
			System.out.println(insertIntoUserTbl);

			stmt1.executeUpdate(insertIntoUserTbl);
			

			// retrieving new userId
			String newUserId = null, newUserName = null;
			ResultSet result = stmt1.executeQuery("select userId, username from user order by userId DESC limit 1");
			while(result.next()) {
				newUserId = result.getString("userId");
				newUserName = result.getString("username");
			}
			// inserting into user info table
			String insertIntoUserInfoTbl = "insert into userinfo(userId, firstName, middleName, lastName, phoneNumber) "
					+ "values(" + newUserId + "," + "'" + firstName + "'" + "," + "'" + middleName + "'" + "," + "'" + lastName + "'" + "," + phoneNumber +")";
			System.out.println(insertIntoUserInfoTbl);
			stmt1.executeUpdate(insertIntoUserInfoTbl);
			
			// inserting address 
			String insertIntoAddress = "insert into address(street, city, state, zipcode) "
					+ "values(" + "'" + street + "'" + "," + "'" + city + "'" + "," + "'" + state + "'" + "," + zipcode + ")";
			System.out.println(insertIntoAddress);
			stmt1.executeUpdate(insertIntoAddress);
			
			// retrieving new addressId
			String newAddressId = null;
			result = null;
			result = stmt1.executeQuery("select addressId from address order by addressId DESC limit 1");
			while(result.next()) {
				newAddressId = result.getString("addressId");
			}
			
			// inserting userid and new address id into user_address table
			stmt1.executeUpdate("insert into user_address(userId, addressId) values(" + newUserId + "," + newAddressId + ")");
			
			System.out.println("Gathering the information...");
			Thread.sleep(200);
			System.out.println("Registering the user...");
			Thread.sleep(200);
			System.out.println("\n\n" + newUserName + " Account is registered successfully...");
			System.out.println("Redirecting to the Main LogIn Page...");
			Thread.sleep(2000);
			Main.getInput();
					
		} catch (SQLException e) {
			System.out.println("New registration of user failed...");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void registerUserAddressJunction() {
		
	}

	private static void registerUserAddress() {
		
	}

	public static void main(String[] args) {}
}
