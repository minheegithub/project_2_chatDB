package chatDB;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;



public class ChatDAO {

	private Connection con;
	private static ChatDAO dao;
	
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	private ChatDAO() throws ClassNotFoundException, SQLException {
		con = new DBConn().getConnection();
		System.out.println("접속됨");
	}
	
	public static ChatDAO getInstance() throws ClassNotFoundException, SQLException {
		if(dao == null)
			dao = new ChatDAO();
		return dao;  //싱글톤 디자인패턴// 변수선언 x, 클래스.getInstance().함수(); 디비 커넥션을 한번만 진행하고 객체를 불러와서 쓴다.
	}
	

	public boolean insertDb(String nickname, String serverIp, String clientIp, int serverPort,String contents) {
		String sql = "insert into chatt1 values(?,?,?,?,?,sysdate)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			pstmt.setString(2, serverIp);
			pstmt.setString(3, clientIp);
			pstmt.setInt(4, serverPort);
			pstmt.setString(5, contents);

			pstmt.executeQuery();
			System.out.println("대화내용 저장");
		} catch (SQLException e) {
			System.out.println("insert Exception");
			return false;
		} finally {
			if (pstmt != null)
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return true;
	}

	public ArrayList<ChatVO> selAllChat() throws SQLException {

		ArrayList<ChatVO> carray = new ArrayList<ChatVO>();

		String sql = "SELECT * FROM chatt1 ORDER BY chat_date DESC";
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			
			String nickname = rs.getString(1);
			String serverIp = rs.getString(2);
			String clientIp = rs.getString(3);
			int serverPort = rs.getInt(4);
			String contents = rs.getString(5);
			Date d = rs.getTimestamp(6);
			
			ChatVO tv = new ChatVO(nickname, serverIp, clientIp, serverPort,contents, d);

			carray.add(tv);

		} // while_end
		return carray;

	}// getAllInfo()-end

	public boolean checkUser(String nickname) {
//		String sql = "SELECT nickname FROM userList WHERE nickname = ?";
		String sql = "SELECT nickname FROM memberList WHERE nickname = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			rs = pstmt.executeQuery();
	
			if(rs.next()) {
				return true;//유저가 있으면 트루반환
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (pstmt != null)
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;//유저 없으면 false
	}
	
	public void insertUser(String nickname) {
		String sql = "insert into userList values(?, TO_CHAR(sysdate, ?) ||?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			pstmt.setString(2, "dd");
			pstmt.setString(3, "일");
			
			pstmt.executeQuery();
//			System.out.println("대화내용 저장");
		} catch (SQLException e) {
			System.out.println("insert Exception");
		} finally {
			if (pstmt != null)
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
	
	public ArrayList<String> CurrentMem() throws SQLException {

		ArrayList<String> memList = new ArrayList<String>();

		String sql = "SELECT * FROM memberList";
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			
			String nickname = rs.getString("nickname");
			memList.add(nickname);

		} // while_end
		return memList;

	}// getAllInfo()-end
	
	public void insertList(String nickname) {
		String sql = "insert into memberList values(?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			
			pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("insert Exception");
		} finally {
			if (pstmt != null)
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
	public void deleteList(String nickname) {
		String sql = "DELETE FROM memberList WHERE nickname = ?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			
			pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("delete Exception");
		} finally {
			if (pstmt != null)
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	public void truncateList() throws SQLException {
		
		String sql = "TRUNCATE TABLE memberList";
		pstmt = con.prepareStatement(sql);
		pstmt.executeQuery();
		
		pstmt.close();
	}

}
