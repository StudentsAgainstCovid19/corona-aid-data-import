import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Random;

public class AdditionalInformationImporter {

	public static void main(String[] args) {

		// Create Connection to mySQL Database
		String dbUrl = System.getenv("SAC19_DB_URL"); // DB connection url
		String username = System.getenv("SAC19_DB_USER"); // DB username
		String password = System.getenv("SAC19_DB_PASSWORD"); //DB password

		try {
			Connection conn = DriverManager.getConnection(dbUrl, username, password);

			// Create new Database Table if not already exists
			conn.createStatement().execute("CREATE TABLE IF NOT EXISTS additional_information (phone VARCHAR(13), insuranceId VARCHAR(10));");
			
			System.out.println("Table created");

			try {
				// Prepare Statement for insertion into Database
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO additional_information (phone, insuranceId) VALUES(?, ?);");
				
				System.out.println("Prepared statement created, start inserting ...");
				// Inserting different values into Database Table
				for (int i = 0; i <= 5000; i++) {
					Random rnd = new Random();
					int randNr = 10000000 + rnd.nextInt(90000000);
					int ins = 100000000 + rnd.nextInt(900000000);

					stmt.setString(1, "0721 " + randNr);
					stmt.setString(2, "V" + ins);

					// execute the prepared statement
					stmt.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}