package MainHub;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import UserBlock.*;
import TransactionEnable.*;

// have to make a singleton pattern. user object attrib association. temporary fix given as of now.

public class Main {
	private static Scanner scan = new Scanner(System.in);

	public static void welcomeScreenBanner() {
		System.out.println("********************************************************************************************************************************************************");
		System.out.println("\t\t\t\t\t\tWelcome to Salary Manager");
		System.out.println("********************************************************************************************************************************************************");
		System.out.println("");
	}
	
	public static void mainScreenBanner() {
		System.out.println("\t\t\t\t\t\tHome Page\n");
	}
	
	public static void enableNewLineTrick() {
		clearScreen();
	}
	
	public static void selectViewOrTransactionMenu() {
		enableNewLineTrick();
		welcomeScreenBanner();
		System.out.println("\t\t\t\t\t\t  View or Transaction\n");
		
		int selectedOption = 0; 
		
		System.out.println("1. View Income Information");
		System.out.println("2. View Expense Information");
		System.out.println("3. View Balance Information");
		System.out.println("4. Make Income Transaction");
		System.out.println("5. Make Expense Transaction");
		System.out.println("6. Log Out");
		System.out.println("7. Exit");
		System.out.print("\nOption: ");
		selectedOption = scan.nextInt();
		
		try {
			switch(selectedOption) {
		 	case 1:
				enableNewLineTrick();
				welcomeScreenBanner();
				ViewInfo.IncomeView.displayIncomeStatus();
				break;
			case 2:
				enableNewLineTrick();
				welcomeScreenBanner();
				ViewInfo.ExpenseView.displayExpenseStatus();
				break;
			case 3:
				enableNewLineTrick();
				welcomeScreenBanner();
				ViewInfo.SummaryView.displaySummaryStatus();				
				break;
			case 4:
				IncomeTransaction.getIncomeInput();
				break;
			case 5:
				ExpenseTransaction.getExpenseInput();				
				break;
			case 6:
				UserBlock.UserLogDirectory.userLogOut();
				System.out.println("\n\nLogging Out...");
				try { Thread.sleep(500);
				System.out.println("Redirecting to home page menu....");
				Thread.sleep(1000);
				} catch (InterruptedException e) { e.printStackTrace();}
				
				enableNewLineTrick();
				welcomeScreenBanner();
				getInput();
				break;
			case 7:
				UserBlock.UserLogDirectory.userLogOut();
				System.out.println("\nSecurely Logging Out...");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Exiting the application...");
				enableNewLineTrick();
				System.exit(0);
			default:
				System.out.println("Seriously!!! Please input only the number options available above!!!");
				try { Thread.sleep(2000);
				} catch (InterruptedException e) { e.printStackTrace();}
				selectViewOrTransactionMenu();
			}
		}catch(InputMismatchException | SQLException e) {
			scan.next();
			System.out.println("Seriously!!! Please input only the number options available above!!!");
			System.out.println(e);
			try { Thread.sleep(2000);
			} catch (InterruptedException e1) { e1.printStackTrace();}
			
			selectViewOrTransactionMenu();			
		}
	}
	public static void clearScreen() {  
	    try {
	        if (System.getProperty("os.name").contains("Windows"))
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
	            Runtime.getRuntime().exec("clear");
	    } catch (IOException ex) {System.out.println(ex);}
	} 
	
	public static void getInput() {
		int selectedOption = 0;
		
		enableNewLineTrick(); 
		welcomeScreenBanner();
		mainScreenBanner();
		
		System.out.println("Enter your choice properly");
		System.out.println("1. Login");
		System.out.println("2. Create New User Account");
		System.out.println("3. Forgot Username/Password");
		System.out.println("4. Exit");
		
		try {
			System.out.print("\nInput: ");
			selectedOption = scan.nextInt();
			scan.nextLine();
		}catch(InputMismatchException ime) {
			System.out.println("Input only numbers... Try again!!!");
			System.out.println("Redirecting to LogIn page menu...");
			scan.nextLine();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			enableNewLineTrick();
			welcomeScreenBanner();
			getInput();			
		}
		
		if(selectedOption == 1) {
			try {
				enableNewLineTrick();
				welcomeScreenBanner();
				LogInPage.getUserCredentials();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(selectedOption == 2) {
			clearScreen();
			welcomeScreenBanner();
			UserBlock.NewRegistration.createNewUserAccount();
		}else if(selectedOption == 3) {
			clearScreen();
			welcomeScreenBanner();
			UserBlock.AccountRecovery.accountRecoveryHelp();
		}else if(selectedOption == 4){
			System.out.println("Exiting the application...");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			enableNewLineTrick();
			System.exit(0);
		}else {
			System.out.println("Invalid input... Try again!!!");
			System.out.println("Redirecting to LogIn page menu....");
			try{Thread.sleep(1000);
			} catch (InterruptedException e) { e.printStackTrace();}
			
			enableNewLineTrick();
			welcomeScreenBanner();
			getInput();
		}
	}
	
	public static void main(String...args) {
		welcomeScreenBanner();
		getInput();
	}
}
