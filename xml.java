package corona_aid.xml;

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

public class xml {

	static private String getTextContent(Node parentNode, String childName) {
		NodeList nlist = parentNode.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node n = nlist.item(i);
			String name = n.getNodeName();
			;
			if (name != null && name.equals("tag")
					&& n.getAttributes().getNamedItem("k").getTextContent().equals("addr:" + childName))
				return n.getAttributes().getNamedItem("v").getTextContent();
		}
		return null;
	}

	public static void main(String[] args) {

		// Create Connection to mySQL Database
		Strng dbUrl = ; //Database Connection Link
		String uname = ; //Database Username
		String pwd = ;   //Database Password
		try {
			
			Connection conn = DriverManager.getConnection(dbUrl, uname, pwd);
			conn.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS address"
					+ "(id INT(10) PRIMARY KEY AUTO_INCREMENT, "
					+ "city VARCHAR(100), "
					+ "street VARCHAR(100), "
					+ "housenumber VARCHAR(20), "
					+ "postcode VARCHAR(10));");
			
			try {
				File file = new File("C:/Link/to/your/XML");

				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO address( city, street, housenumber, postcode) VALUES(?, ?, ?, ?) ");

				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

				// Defines a factory API that enables applications to obtain a parser that
				// produces DOM object trees from XML documents.
				DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();

				// The Document interface represents the entire HTML or XML document.
				// Conceptually, it is the root of the document tree, and provides the primary
				// access to the document's data.
				System.out.println("parse");
				Document doc = factory2.newDocumentBuilder().parse(file);

				// Get input element from user
				System.out.print("Enter element name: ");
				String element = reader.readLine();

				// Returns a NodeList of all the Elements in document order with a given tag
				// name and are contained in the document.
				NodeList nodes = doc.getElementsByTagName(element);
				
				
				//Use Nodelist and XML to import Data into Database
				System.out.println("gelesen " + nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					List<String> columns = Arrays.asList( getTextContent(node, "city"), getTextContent(node, "street"),
							getTextContent(node, "housenumber"), getTextContent(node, "postcode"));
					for (int n = 0; n < columns.size(); n++) {
						stmt.setString(n + 1, columns.get(n));
					}
					stmt.execute();
				}

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
