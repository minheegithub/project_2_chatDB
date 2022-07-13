package chatDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

	private Connection con;

	public Connection getConnection() {
		
		return con;
	}
	
	//»ý¼ºÀÚ
	public DBConn() throws ClassNotFoundException, SQLException {
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","chat","chat");
	}
}
