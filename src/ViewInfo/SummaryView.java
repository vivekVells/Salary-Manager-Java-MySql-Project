package ViewInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import MainHub.Main;
import UserBlock.LogInPage;

public class SummaryView {
	private static Connection cxn;
	private static Statement st;
	private static ResultSet rs;
	static Scanner scan = new Scanner(System.in);
	private static String username = null;
	
	public static void summaryInfoViewScreenBanner() {
		System.out.println("\t\t\t\t\t\t  Summary Information Page\n");
	}
	
	public static void displaySummaryStatus() {
		int userId = LogInPage.getValidatedUserId();
		
		summaryInfoViewScreenBanner();
		
		Connection con = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		Statement stmt = null;
		Statement txnStmt = null;
		try {
			stmt = con.createStatement();
			txnStmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String getUserAttribs = "select username from user where userId=" + String.valueOf(userId);
		String getUserLastLoggedInInfo= "select lastLoggedIn from userlog where userId=" + String.valueOf(userId) + " order by lastLoggedIn desc limit 2";
		String getUserInfo = "select firstName, middleName, lastName, phoneNumber from userInfo where userId=" + String.valueOf(userId);
		String getUserAddress = "select street, state, zipcode from address where addressId = (select addressId from user_address where userId= " + String.valueOf(userId) + " )";
		String userLastLoggedIn = null;
		try {
			ResultSet result = stmt.executeQuery(getUserLastLoggedInInfo);
			while(result.next()) {
				userLastLoggedIn = result.getString("lastLoggedIn");
			}
			System.out.printf("%120s %s", "Last Logged In: ", userLastLoggedIn); System.out.println();

			String phoneNumber = null;
			result = null;
			result = stmt.executeQuery(getUserInfo);
			while(result.next()) {
				System.out.println("\n" + result.getString("firstName") + " " + result.getString("middleName")+ " " + result.getString("lastName"));
				phoneNumber = result.getString("phoneNumber");
			}
			
			result = null;
			result = stmt.executeQuery(getUserAddress);
			while(result.next()) {
				System.out.println(result.getString("street") + ", " + result.getString("state")+ ", " + result.getString("zipcode") );
				System.out.println(phoneNumber);
			}
			
			System.out.println("\n");
			System.out.println("Account Balance Summary");
			String getUserNetAmount = "select netAmount from transaction where userId=" + String.valueOf(userId) +" order by asOnDate desc limit 1";
			String getUserNetIncome = "select sum(incomeAmount) as netIncome from income where userId=" + String.valueOf(userId) + " ";
			String getUserNetExpense = "select sum(expenseAmount) as netExpense from expense where userId=" + String.valueOf(userId) + " ";
			
			result = null;
			result = stmt.executeQuery(getUserNetAmount);
			while(result.next()) { getUserNetAmount = result.getString("netAmount"); }
			
			result = null;
			result = stmt.executeQuery(getUserNetIncome);
			while(result.next()) { getUserNetIncome = result.getString("netIncome"); }
			
			result = null;
			result = stmt.executeQuery(getUserNetExpense);
			while(result.next()) { getUserNetExpense = result.getString("netExpense"); }
			
			if(getUserNetAmount.charAt(0) == 's' ) {
				getUserNetAmount = null;
			}
			
			System.out.println("Net Amount : " + getUserNetAmount);
			System.out.println("Net Income : " + getUserNetIncome);
			System.out.println("Net Expense: " + getUserNetExpense);
			
			System.out.println("\n\nTransaction In Detail");
			
			//System.out.println("Sno   Transaction ID   CREDIT/DEBIT   Amount   Description   Created On           Balance    As On Date");
			System.out.println("  Sno  Transaction ID  CREDIT/DEBIT      Amount     Description                    Created On   Balance       As On Date");
			
			String getUserTxn = "select transactionId, netAmount, createdOn, isIncome, asOnDate from transaction where userId=" + String.valueOf(userId) +" and deletedOn IS NULL";
			String txnId = null;
			String txnNetAmount = null;
			String txnCreatedOnDate = null;
			String txnAsOnDate = null;
			
			ResultSet creditOrDebitResult = null;
			String creditOrDebit = null;
			String createdOnCreditOrDebit = null;
			String noteCreditOrDebit = null;
			Double amountCreditOrDebit = 0.0;
			String creditOrDebitQuery = null;
			char txnIsIncome = 0;
			result = null;
			result = stmt.executeQuery(getUserTxn);
			int sno = 1;
			while(result.next()) {
				txnId = result.getString("transactionId");
				txnNetAmount =result.getString("netAmount");
				txnCreatedOnDate = result.getString("createdOn");
				txnAsOnDate = result.getString("asOnDate");
				txnIsIncome = (result.getString("isIncome")).toCharArray()[0];
				
				if(txnIsIncome == 'Y') { 
					creditOrDebit = "Credit";
					creditOrDebitQuery = 
							"select income.incomeAmount as amountCreditOrDebit, income.earnedDate as createdOnCreditOrDebit, income.jobRole as noteCreditOrDebit"
							+ " from income where income.deletedOn is null and "
							+ " incomeId = (select income_transaction.incomeId from income_transaction where transactionId =" + txnId + ")";
				}else { 
					creditOrDebit = "Debit";
					creditOrDebitQuery =
							"select expense.expenseAmount as amountCreditOrDebit, expense.note as noteCreditOrDebit, expense.spentOnDate as createdOnCreditOrDebit"
							+ " from expense where expense.deletedOn is null and "
							+ " expenseId = (select expense_transaction.expenseId from expense_transaction where transactionId =" + txnId + ")";
				}
				
				creditOrDebitResult = txnStmt.executeQuery(creditOrDebitQuery);
				while(creditOrDebitResult.next()) {
					amountCreditOrDebit = creditOrDebitResult.getDouble("amountCreditOrDebit");
					createdOnCreditOrDebit = creditOrDebitResult.getString("createdOnCreditOrDebit");
					noteCreditOrDebit = creditOrDebitResult.getString("noteCreditOrDebit");
				}
				System.out.println("  " + sno + "   " + txnId + "                 " + creditOrDebit + "            " + amountCreditOrDebit + "   " + noteCreditOrDebit + "           "+ createdOnCreditOrDebit + "   " + txnNetAmount + "     "  + txnAsOnDate);
				//System.out.printf("%-5d %-5s %-15s %-13s %-15s\n", sno, creditOrDebit, txnId, txnNetAmount, txnCreatedOnDate );
			
				++sno;	
			}sno=0;			
		} catch (SQLException e) {
			System.out.println("Display Summary balance txns sql failed...");
			e.printStackTrace();
		}	
		
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
			System.out.println("\nSecurely Logging Out...");
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
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Exiting the application...");
			Main.enableNewLineTrick();
			System.exit(0);
		}else {
			System.out.println("Input correctly!");
			redirectIncomeViewMenu();
		}
	}

	public static void main(String...args) {}
}
