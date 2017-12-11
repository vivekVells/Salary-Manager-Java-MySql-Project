package UserBlock;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import MainHub.Main;

public class AccountRecovery {
	private static Scanner scan = new Scanner(System.in);
	private static int  userId = UserBlock.LogInPage.getValidatedUserId();
	private static String retrievedUserName = null, retrievedRecoveryAnswer = null, retrievedPassword = null, retrievedEmail = null;

	public static void accountRecoveryScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Account Recovery Page\n");
	}
	
	public static void accountRecoveryHelp() {
		accountRecoveryScreenBanner();
		int selectedOption = 0;
		
		System.out.println("\n\nEnter your choice correctly to Recover your account");
		System.out.println("1. By Valid Answer");
		System.out.println("2. By Email");	
		System.out.println("3. Home Page");
		System.out.println("4. Exit");
		try {
			System.out.print("\nOption: ");
			selectedOption = scan.nextInt();
			scan.nextLine();
		}catch(InputMismatchException ime) {
			System.out.println("Input only numbers... Try again!!!");
			System.out.println("Redirecting to LogIn page menu...");
			scan.nextLine();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryHelp();			
		}
		
		if(selectedOption == 1) {
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryScreenBanner();
			accountRecoveryByAnswer();
		}else if(selectedOption == 2) {
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryScreenBanner();
			accountRecoveryByEmail();
		}else if(selectedOption == 3) {
			Main.getInput();
		}else if(selectedOption == 4){
			System.out.println("Exiting the application...");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.clearScreen();
			System.exit(0);
		}else {
			System.out.println("Invalid input... Try again!!!");
			System.out.println("Redirecting to Account Recovery page menu....");
			try{Thread.sleep(1000);
			} catch (InterruptedException e) { e.printStackTrace();}
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryHelp();
		}		
	}
	
	public static void accountRecoveryByAnswer() {
		Main.clearScreen();
		Main.welcomeScreenBanner();
		accountRecoveryScreenBanner();
		
		String givenUserName = null, givenUserAnswer = null;
		
		System.out.println("Input the followings properly (case-sensitive)");
		System.out.print("Username: ");
		givenUserName = scan.nextLine();
		System.out.print("Answer: ");
		givenUserAnswer = scan.nextLine();
		
		System.out.println("\nValidating the information provided...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		retrieveUserInformation(givenUserName);
		validateGivenAccountRecoveryByAnswer(givenUserName, givenUserAnswer);
	}
	
	private static void retrieveUserInformation(String givenUserName) {
		// this done because of static variable usage. have to re-initalize....
		retrievedUserName = null; retrievedRecoveryAnswer = null;
		retrievedPassword = null; retrievedEmail = null;
		
		Connection con = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		try {
			Statement stmt1 = con.createStatement();
			ResultSet result = stmt1.executeQuery("select username, recoveryAnswer, password, recoveryEmail from user where username=" + "'" + givenUserName + "'");
			
			while(result.next()) {
				retrievedUserName = result.getString("username");
				retrievedRecoveryAnswer= result.getString("recoveryAnswer");
				retrievedPassword = result.getString("password");
				retrievedEmail = result.getString("recoveryEmail");
			}
		} catch (SQLException e) {
			System.out.println("Account recovery by answer request failed...");
			e.printStackTrace();
		}
	}
	
	public static void validateGivenAccountRecoveryByAnswer(String givenUserName, String givenUserAnswer) {
		
		if(givenUserName.equals(retrievedUserName) && givenUserAnswer.equals(retrievedRecoveryAnswer)) {
			System.out.println("Given Information is correct...");
			System.out.println("\n\nThe followings are your account log in information\n");
			System.out.println("Username: " + givenUserName);
			System.out.println("Password: " + retrievedPassword);
			
			System.out.println("\n\nYou will be redirected to Home page in 7 seconds...");
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.getInput();
		}else {
			System.out.println("\nInvalid Information Provided... Try Again!!!");
			System.out.println("Redirecting to the Account Recovery Page...");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryHelp();				
		}
	}
	
	public static void accountRecoveryByEmail() {
		String givenRecoveryEmail = null, givenUserName = null;
		
		Main.clearScreen();
		Main.welcomeScreenBanner();
		accountRecoveryScreenBanner();		
		
		System.out.println("Input the followings properly (case-sensitive)");
		System.out.print("Username: ");
		givenUserName = scan.nextLine();
		System.out.print("Email: ");
		givenRecoveryEmail = scan.nextLine();

		retrieveUserInformation(givenUserName);
		System.out.println("Validating the information... Please wait...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(givenRecoveryEmail.equals(retrievedEmail)) {
			Utility.SendEmail.sendEmailToUser(retrievedEmail, retrievedUserName, retrievedPassword);
			
			System.out.println("\nThe given information is correct...");
			System.out.println("\nCheck your " + givenRecoveryEmail);
			System.out.println("You will be receiving an email with your account recovery information");
			System.out.println("\n\nYou will be redirected to Home page in 9 seconds...");
			try {
				Thread.sleep(9000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.getInput();
		}else {
			System.out.println("\nInvalid Information Provided... Try Again!!!");
			System.out.println("Redirecting to the Account Recovery Page...");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.clearScreen();
			Main.welcomeScreenBanner();
			accountRecoveryHelp();				
		}		
	}
	
	public static void main(String[] args) {}
}
