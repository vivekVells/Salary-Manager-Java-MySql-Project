package TransactionEnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import UserBlock.LogInPage;
import MainHub.*;

public class ExpenseTransaction {
	static Scanner scan = new Scanner(System.in);
	private static Double expenseAmount;
	private static String spentOnDate;
	private static String note;
	private static Connection cxn= ConnectionEstablish.ConnectToDB.getMySqlConnection();
	private static Statement st;
	
	public static void expenseTransactionScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Expense Transaction Page\n");
	}
	
	public static void initialScreenSetup() {
		Main.enableNewLineTrick();
		Main.welcomeScreenBanner(); 
		expenseTransactionScreenBanner();
	}
	
	public static void getExpenseInput() {
		int userId = LogInPage.getValidatedUserId();
		int selectedOption = 0;
		
		initialScreenSetup();
		
		System.out.print("Spent On: ");
		note = scan.nextLine();
		System.out.print("Expense Amount: ");
		expenseAmount = scan.nextDouble();
		scan.nextLine();
		System.out.print("Spent On Date (YYYY/MM/DD): ");
		spentOnDate = scan.nextLine();
		
		try {
			st = cxn.createStatement();
		} catch (SQLException e1) {
			System.out.println("Expense TXN... Connection failure...");
			e1.printStackTrace();
		}

		String insertExpenseAttrib = "INSERT INTO EXPENSE(userId, expenseAmount, spentOnDate, note) "
				+ "VALUES(" + userId + "," + expenseAmount +"," + "'" + spentOnDate + "'" + "," + "'" + note + "')";
		
		String setRecentNetAmountByUserId = "set @recent_net_amount_by_userId = if((select exists(select 1 from transaction where userId = @current_expense_userId limit 1)) !=1,0.0,(select netAmount from transaction where userId = @current_expense_userId order by transactionId DESC limit 1))";
		String insertTransactionAttrib = "insert into transaction(netAmount, userId, isIncome) values((@recent_net_amount_by_userId - @recent_expense_amount), @current_expense_userId, 'N')";
		String insertExpenseTransactionAttrib = "insert into expense_transaction(expenseId, transactionId) values(@recent_expenseId, @recent_transactionId)";

		try {
			st.executeUpdate(insertExpenseAttrib);
			st.executeUpdate(setRecentNetAmountByUserId);
			st.executeUpdate(insertTransactionAttrib);
			st.executeUpdate(insertExpenseTransactionAttrib);
			System.out.println("Expense Transaction Added Successfully....");
		} catch (SQLException e1) {
			System.out.println("insertion query of expense failed...");
			e1.printStackTrace();
		}		
		try { Thread.sleep(2000); } catch (InterruptedException e1) { 
			e1.printStackTrace(); }
		
		//initialScreenSetup();
		System.out.println("\n\n");
		System.out.println("Press 1. Return to View or Transaction Home Page");
		System.out.println("Press 2. Input Expense Transaction again");
		System.out.println("Press 3. Log out");
		System.out.println("Press 4. Exit");
		
		System.out.print("\nOption: ");
		selectedOption = scan.nextInt();
		scan.nextLine();
			
		if(selectedOption == 2) {
			getExpenseInput();
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
			getExpenseInput();
		}
		
	}
	
	public static void main(String...args) {}
}
