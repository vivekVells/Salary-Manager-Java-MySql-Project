package ViewInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import MainHub.Main;
import UserBlock.LogInPage;
import javafx.scene.transform.Scale;

public class ExpenseView {
	private static Connection cxn = ConnectionEstablish.ConnectToDB.getMySqlConnection();
	private static Statement st;
	private static ResultSet rs;
	private static Scanner scan = new Scanner(System.in);
	
	public static void expenseInfoViewScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Expense Information Page\n");
	}
	
	public static void redirectExpenseViewMenu() {
		int userId = LogInPage.getValidatedUserId();
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
			redirectExpenseViewMenu();
		}
	}
	
	public static void displayExpenseStatus() throws SQLException {
		int userId = LogInPage.getValidatedUserId();
		
		expenseInfoViewScreenBanner();

		Connection con = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		Statement stmt = con.createStatement();
		String getIncomeInfoQuery = "select note, expenseAmount, spentOnDate from expense where userId=" + String.valueOf(userId);
		ResultSet result = stmt.executeQuery(getIncomeInfoQuery);
		ResultSetMetaData resultMetaData = result.getMetaData();
		
		int totalColumnCount = resultMetaData.getColumnCount();
		
		String expenseAmount = "expenseAmount";
		String note = "note";
		String spentOnDate = "spentOnDate";
		int sno = 1;
		System.out.println("Sno   \t" + note + "\t\t" + expenseAmount + "\t" + spentOnDate);
		while(result.next()) {
			System.out.print(sno + "   ");
			System.out.print(result.getString(note) + "\t\t");
			System.out.print(result.getString(expenseAmount) + "\t\t");
			System.out.println(result.getDate(spentOnDate) + "\t");
			sno++;
		}sno=0;
		redirectExpenseViewMenu();
		
	}
	
	public static void main(String...args) {}
}
 