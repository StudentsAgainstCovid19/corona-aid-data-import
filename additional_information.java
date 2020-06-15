package corona_aid.xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Random;

public class additional_information {

	public static void main(String[] args) {

		// Create Connection to mySQL Database
		String dbUrl = ; //Database Connection Link
		String uname = ; //Database Username
		String pwd = ;   //Database Password

		Connection conn;
		try {
			conn = DriverManager.getConnection(dbUrl, uname, pwd);

			// Create new Database Table if not already exists
			conn.createStatement().execute("CREATE TABLE IF NOT EXISTS additional_information"
					+ "(phonenumber VARCHAR(13), " + "insuranceId VARCHAR(10));");
			
			System.out.println("Table created");

			try {

				// Prepare Statement for insertion into Database
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO additional_information( phonenumber, insuranceId) VALUES(?, ?) ");
				
				System.out.println("Prepared Statement created, start inserting...");
				// Inserting different values into Database Table
				for (int i = 0; i <= 5000; i++) {

					Random rnd = new Random();
					Integer randNr = new Integer(10000000 + rnd.nextInt(90000000));

					Integer ins = new Integer(100000000 + rnd.nextInt(900000000));

					stmt.setString(1, "0721 " + randNr.toString());
					stmt.setString(2, "V" + ins.toString());

					// execute the preparedstatement
					stmt.execute();

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (

		SQLException e1) {
			e1.printStackTrace();
		}
	}
}