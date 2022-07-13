package view;

import java.awt.Container;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import chatDB.ChatDAO;
import chatDB.ChatVO;

public class ShowData extends JFrame {

	ChatDAO cdao;

	String[][] data = new String[100][6];
	String[] colName = { "�г���", "�Է� ���� ������ ", "Ŭ���̾�Ʈ ������", "�Է� ���� ��Ʈ","��ȭ����", "ä�ó�¥/�ð�" };

	JTable table;
	JScrollPane jp = new JScrollPane();
	
	String nickname;
	String serverIp;
	String clientIp;
	String serverPort;
	String contents;
	String chatdate;
	

	public ShowData() throws ClassNotFoundException, SQLException {
		super("������ȭ����");
		Container cp = this.getContentPane();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(300, 300, 1100, 500); // �������� ũ�� ����

		table = new JTable(data, colName);
		
		
		
		table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(0).setWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(150);
        table.getColumnModel().getColumn(1).setMinWidth(150);
        table.getColumnModel().getColumn(1).setWidth(150);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(150);
        table.getColumnModel().getColumn(2).setWidth(150);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(3).setWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(400);
        table.getColumnModel().getColumn(4).setMinWidth(400);
        table.getColumnModel().getColumn(4).setWidth(400);
        table.getColumnModel().getColumn(5).setMaxWidth(200);
        table.getColumnModel().getColumn(5).setMinWidth(200);
        table.getColumnModel().getColumn(5).setWidth(200);
        jp = new JScrollPane(table);
		cp.add(jp);
		getData();
	}

	public void getData() throws ClassNotFoundException {
		table.removeAll();
		ArrayList<ChatVO> carray = new ArrayList<ChatVO>();
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 6; j++) {
				data[i][j] = "";
			}
		} // for��
		
		try {
			carray = ChatDAO.getInstance().selAllChat();
			
			for(int i = 0; i < carray.size() ; i++) {
				if(i == 100) {//100���� ������, dao���� selAllChat �ֽż����� ���ĵǰ� ������ ä���� ������ �ö�����ߴ�. 100���� �Ѿ�� ���� �������ź��� ��������.
					return;//���Ϲ����� 100�� �Ǵ¼��� ���� 0~99���� ���
				}else {
					nickname = carray.get(i).getNickname();
					serverIp = carray.get(i).getServerIp();
					clientIp = carray.get(i).getClientip();
					serverPort = carray.get(i).getServerPort()+"";
					contents = carray.get(i).getContents();
					chatdate = carray.get(i).getChatDate().toString();
				
				
					data[i][0] = nickname;
					data[i][1] = serverIp;
					data[i][2] = clientIp;
					data[i][3] = serverPort;
					data[i][4] = contents;
					data[i][5] = chatdate;
				}
				
			}
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}

		table.repaint();

	}// showData
}
