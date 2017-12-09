package ViewInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import MainHub.Main;
import UserBlock.LogInPage;

public class IncomeView extends TableViewFormat{
	private static Connection cxn;
	private static Statement st;
	private static ResultSet rs;
	static Scanner scan = new Scanner(System.in);
	private static String username = null;  
	
	public static void incomeInfoViewScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Income Information Page\n");
	}
	
	public static void displayIncomeStatus() throws SQLException {		
		int userId = LogInPage.getValidatedUserId();
		
		incomeInfoViewScreenBanner();
		
		Connection con = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		Statement stmt = con.createStatement();
		String getIncomeInfoQuery = "select jobRole, incomeAmount, note, earnedDate from income where userId=" + String.valueOf(userId);
		ResultSet result = stmt.executeQuery(getIncomeInfoQuery);
		ResultSetMetaData resultMetaData = result.getMetaData();
		
		int totalColumnCount = resultMetaData.getColumnCount();
		
		String jobRole = "jobRole";
		String incomeAmount = "incomeAmount";
		String note = "note";
		String earnedDate = "earnedDate";
		int sno = 1;
		System.out.println("Sno   \t" +jobRole + "\t\t\t" + incomeAmount + "\t" + note + "\t" + earnedDate);
		while(result.next()) {
			System.out.print(sno + "  ");
			System.out.print(result.getString(jobRole) + "\t\t\t");
			System.out.print(result.getString(incomeAmount) + "\t\t");
			System.out.print(result.getString(note) + "\t\t");
			System.out.println(result.getDate(earnedDate) );
		sno++;
		}sno=0;
		redirectIncomeViewMenu();
					
	}

	public static void redirectIncomeViewMenu() {
		int selectedOption;
		
		System.out.println("\n\n");
		System.out.println("Press 1. Return to View or Transaction Home Page");
		System.out.println("Press 2. Log out");
		System.out.println("Press 3. Exit");
		
		System.out.print("\nOption: ");
		selectedOption = scan.nextInt();
		scan.nextLine();
		
		if(selectedOption == 1) {
			Main.selectViewOrTransactionMenu();
		}else if(selectedOption == 2){
			UserBlock.UserLogDirectory.userLogOut();
			System.out.println("\n\nLogging Out...");
			try { Thread.sleep(500);
			System.out.println("Redirecting to home page menu....");
			Thread.sleep(1000);
			} catch (InterruptedException e) { e.printStackTrace();}
			
			Main.enableNewLineTrick();
			Main.welcomeScreenBanner();
			Main.getInput();
		}else if(selectedOption == 3){
			UserBlock.UserLogDirectory.userLogOut();
			System.out.println("\nSecurely Logging Out...");
			System.out.println("Exiting the application...");

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.enableNewLineTrick();
			System.exit(0);
		}else {
			System.out.println("Input correctly!");
			redirectIncomeViewMenu();
		}
	}
	
	public static void test() {
		// TODO Auto-generated method stub
		List<String> headersList = Arrays.asList("XXX", "UUUU", "MARRIED", "AGE", "SALARY($)");
		List<List<String>> rowsList = Arrays.asList(
		        Arrays.asList("Eddy", "Male", "No", "23", "1200.27"),
		        Arrays.asList("Libby", "Male", "No", "17", "800.50"),
		        Arrays.asList("Rea", "Female", "No", "30", "10000.00"),
		        Arrays.asList("Deandre", "Female", "No", "19", "18000.50"),
		        Arrays.asList("Alice", "Male", "Yes", "29", "580.40"),
		        Arrays.asList("Alyse", "Female", "No", "26", "7000.89"),
		        Arrays.asList("Venessa", "Female", "No", "22", "100700.50")
		);
		//bookmark 1
		Board board = new Board(75);
		String tableString = board.setInitialBlock(new Table(board, 75, headersList, rowsList).tableToBlocks()).build().getPreview();
		System.out.println(tableString);
	}
	public static void main(String...args) {
		

	}
}
