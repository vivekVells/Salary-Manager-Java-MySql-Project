package TransactionEnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import UserBlock.LogInPage;
import MainHub.*;

public class IncomeTransaction {
	static Scanner scan = new Scanner(System.in);
	private static Double incomeAmount;
	private static String earnedDate;
	private static String note;
	private static String jobRole;
	private static Connection cxn;
	private static Statement st;
	
	public static void incomeTransactionScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Income Transaction Page\n");
	}
	 
	public static void initialScreenSetup() {
		Main.enableNewLineTrick();
		Main.welcomeScreenBanner();
		incomeTransactionScreenBanner();
	}
	
	public static void getIncomeInput() {
		int userId = LogInPage.getValidatedUserId();
		int selectedOption = 0;
		
		initialScreenSetup();
		System.out.print("Job Role: ");
		jobRole = scan.nextLine();
		System.out.print("Income Amount: ");
		incomeAmount = scan.nextDouble();
		scan.nextLine();
		System.out.print("Earned Date (YYYY/MM/DD): ");
		earnedDate = scan.nextLine();
		System.out.print("Note: ");
		note = scan.nextLine();
		
		cxn = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		try {
			st = cxn.createStatement();
		} catch (SQLException e1) {
			System.out.println("income TXN... Connection failure...");
			e1.printStackTrace();
		}

		String insertIncomeAttrib = "INSERT INTO income(userId, jobRole, incomeAmount, earnedDate, note) "
				+ "VALUES(" + userId + "," + "'" + jobRole + "'" + "," + incomeAmount + "," + "'" + earnedDate + "'" + "," + "'" + note + "'" +");";
		String setRecentNetAmountByUserId = "set @recent_net_amount_by_userId = if( (select exists(select 1 from transaction where userId = @current_income_userId limit 1)) !=1,0.0,(select netAmount from transaction where userId = @current_income_userId order by transactionId DESC limit 1) )";
		String insertTransactionAttrib = "insert into transaction(netAmount, userId, isIncome) values((@recent_income_amount + @recent_net_amount_by_userId), @current_income_userId, 'Y')";
		String insertIncomeTransactionAttrib = "insert into income_transaction(incomeId, transactionId) values(@recent_incomeId, @recent_transactionId)";
		
		try {
			st.executeUpdate(insertIncomeAttrib);
			st.executeUpdate(setRecentNetAmountByUserId);
			st.executeUpdate(insertTransactionAttrib);
			st.executeUpdate(insertIncomeTransactionAttrib);
			System.out.println("income Transaction Added Successfully....");
		} catch (SQLException e1) {
			System.out.println("insertion query of income failed...");
			e1.printStackTrace();
		}		
		try { Thread.sleep(2000); } catch (InterruptedException e1) { 
			e1.printStackTrace(); }
		
		//initialScreenSetup();

		System.out.println("\n\n");
		System.out.println("Press 1. Return to View or Transaction Home Page");
		System.out.println("Press 2. Input income Transaction again");
		System.out.println("Press 3. Log out");
		System.out.println("Press 4. Exit");
		
		System.out.print("\nOption: ");
		selectedOption = scan.nextInt();
		scan.nextLine();
			
		if(selectedOption == 2) {
			getIncomeInput();
		}else if(selectedOption == 1) {
			Main.selectViewOrTransactionMenu();
		}else if(selectedOption == 3){
			UserBlock.UserLogDirectory.userLogOut();
			System.out.println("\n\nLogging Out...");
			try { Thread.sleep(500);
			System.out.println("Redirecting to home page menu....");
			Thread.sleep(1000);
			} catch (InterruptedException e) { e.printStackTrace();}
			
			Main.enableNewLineTrick();
			Main.welcomeScreenBanner();
			Main.getInput();
		}else if(selectedOption == 4){
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
			getIncomeInput();
		}
		
	}
	
	public static void main(String...args) { }
}
