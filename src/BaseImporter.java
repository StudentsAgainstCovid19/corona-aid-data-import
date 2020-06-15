import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BaseImporter {

	static private String getTextContent(Node parentNode, String childName) {
		NodeList nodeList = parentNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			String name = n.getNodeName();
			if (name != null && name.equals("tag")
					&& n.getAttributes().getNamedItem("k").getTextContent().equals("addr:" + childName))
				return n.getAttributes().getNamedItem("v").getTextContent();
		}
		return null;
	}

	public static void main(String[] args) {
		// Establish connection to MySQL database
		String dbUrl = System.getenv("SAC19_DB_URL"); // DB connection url
		String username = System.getenv("SAC19_DB_USER"); // DB username
		String password = System.getenv("SAC19_DB_PASSWORD"); // DB password
		try {
			Connection conn = DriverManager.getConnection(dbUrl, username, password);
			conn.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS address"
					+ "(id INT(10) PRIMARY KEY AUTO_INCREMENT, "
					+ "city VARCHAR(100), "
					+ "street VARCHAR(100), "
					+ "houseNumber VARCHAR(20), "
					+ "postcode VARCHAR(10));");
			
			try {
				File file = new File("data.xml");
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO address ( city, street, houseNumber, postcode) VALUES(?, ?, ?, ?);");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

				// Defines a factory API that enables applications to obtain a parser that
				// produces DOM object trees from XML documents.
				DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();

				// The Document interface represents the entire HTML or XML document.
				// Conceptually, it is the root of the document tree, and provides the primary
				// access to the document's data.
				Document doc = factory2.newDocumentBuilder().parse(file);

				// Get input element from user
				System.out.print("Enter element name: ");
				String element = reader.readLine();

				// Returns a NodeList of all the Elements in document order with a given tag
				// name and are contained in the document.
				NodeList nodes = doc.getElementsByTagName(element);

				//Use Node list and XML to import Data into Database
				System.out.println("Read: " + nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					List<String> columns = Arrays.asList( getTextContent(node, "city"), getTextContent(node, "street"),
							getTextContent(node, "housenumber"), getTextContent(node, "postcode"));
					for (int n = 0; n < columns.size(); n++) {
						stmt.setString(n + 1, columns.get(n));
					}
					stmt.execute();
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}