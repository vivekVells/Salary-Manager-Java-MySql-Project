package ConnectionEstablish;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MySqlEnvSetup {
	private static Connection cxn;
	private static boolean st;
	
	public static void mySqlEnvInitialize() {
		cxn = ConnectionEstablish.ConnectToDB.getMySqlConnection();
		try {
			st = cxn.createStatement().execute(
					"set @recent_income_amount=0.0 select @recent_income_amount "
					);
			System.out.println(st + ":st");
		} catch (SQLException e1) {
			System.out.println("MySql Env Setup initiation... Connection failure...");
			e1.printStackTrace();
		}
	}
	public static void main(String...args) {
		
	}
}
