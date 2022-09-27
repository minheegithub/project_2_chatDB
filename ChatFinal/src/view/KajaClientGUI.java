package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import chatDB.ChatDAO;

public class KajaClientGUI extends JFrame implements Runnable, ActionListener {

	ChatDAO dao;
	private JPanel contentPane;
	
	// console��忡�� �Ѿ���� 3�� ���ڸ� �޾� ������ �غ��ʵ�
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;
	
	JTextArea jtarea1 = new JTextArea();
	JTextField jtfield1 = new JTextField();
	JScrollPane jScrollPane = new JScrollPane(jtarea1);
	JLabel lbNick = new JLabel();
	
	//�����ڸ���Ʈ
	JList list = new JList();//User_list
	
	JButton btnBring = new JButton("������ȭ���� �ҷ�����");
	JButton btnSend = new JButton("����");//
	
	public KajaClientGUI(DataOutputStream outputStream, DataInputStream inputStream, String nickname) throws ClassNotFoundException, SQLException {// ������
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		dao = ChatDAO.getInstance();
	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//����̹��� 
		Image bgimg = new ImageIcon(KajaClientGUI.class.getResource("/image/chatroom4.png")).getImage();
	
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bgimg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		panel.setBounds(0, 0, 787, 581);
		panel.setLayout(null);
		
		btnBring.setBackground(SystemColor.activeCaption);
		btnBring.setBounds(372, 31, 194, 33);
		panel.add(btnBring);
		btnBring.setFont(new Font("����", Font.BOLD, 15));		
		btnBring.addActionListener(this);
	      
        JLabel lbuser = new JLabel("�г���");
        lbuser.setForeground(Color.WHITE);
        lbuser.setBackground(Color.BLACK);
        lbuser.setFont(new Font("����", Font.BOLD, 14));
        lbuser.setBounds(581, 27, 62, 33);
        contentPane.add(lbuser);
      
        //�г���
        lbNick.setBounds(645, 27, 89, 26);
        lbNick.setFont(new Font("����", Font.BOLD, 17));
        lbNick.setForeground(Color.white);
        contentPane.add(lbNick);
      
        JLabel lbList = new JLabel("������ ���");
        lbList.setForeground(Color.WHITE);
        lbList.setBackground(Color.BLACK);
        lbList.setFont(new Font("����", Font.BOLD, 19));
        lbList.setBounds(581,68, 133, 33);
        contentPane.add(lbList);
      
        btnSend.setBackground(SystemColor.activeCaption);
        btnSend.setBounds(580, 505, 105, 30);
        panel.add(btnSend);
        btnSend.setFont(new Font("����", Font.BOLD, 17));
        btnSend.addActionListener(this);

		jtarea1.setFont(new Font("����", Font.BOLD, 18));
		jtarea1.setEditable(false);

		// chat �Է�
		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("����", Font.BOLD, 18));
		jtfield1.setBounds(42, 501, 524, 38);
		panel.add(jtfield1);
		jtfield1.setColumns(10);
		jtfield1.addActionListener(this);
		
	    list.setBounds(581, 105, 151, 380);
		list.setFont(new Font("����", Font.BOLD, 19));
		panel.add(list);
		
		jScrollPane.setBounds(42, 76, 525, 413);
		panel.add(jScrollPane);
		
		contentPane.add(panel);
		setBounds(550, 100, 795, 617);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				dispose();
				System.exit(0);
				setVisible(false);
			}
		});
		
		// ������ ���� �޾� textarea�� �ѷ��ִ� ������
	    lbNick.setText(nickname);
		Thread th1 = new Thread(this);
		th1.start();
		
	}// ������-end

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend || e.getSource() == jtfield1) {
			try {
				System.out.println("KajaClientGUI "+nickname+"Ŭ���̾�Ʈ���� ���۹�ư ������ output���� �������� ��ȭ���� : "+nickname + " �� " + jtfield1.getText());
				// nickname�� client�� chat�� ������
				outputStream.writeUTF(nickname + " �� " + jtfield1.getText());
			
			} catch (IOException ee) {
				// ee.printStackTrace();
			}
			jtfield1.setText("");
		}else if(e.getSource() == btnBring){
			try {
				new ShowData().setVisible(true);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}// actionPerformed - end

	Toolkit tk1 = Toolkit.getDefaultToolkit();
	
	public void run() { // for �޴� thread
		try {
			while (true) {
				
				String strServer1 = inputStream.readUTF();
				System.out.println("KajaClientGUI "+nickname+"runnable Ŭ���̾�Ʈ����  input���� ���� ��ȭ ����"+strServer1);
				if (strServer1 == null) {
					jtarea1.append("\n" + "����");
					return;
				}
				
				jtarea1.append("\n" + strServer1);	
				
				DefaultListModel listModel = new DefaultListModel<>();// ����Ʈ���� �� ���� 
				ArrayList<String> arrList = dao.CurrentMem(); //�𵨿� �־��� ��̸���Ʈ�� �ű⿡ �� ���������ο� ���̺� 
				for(String s : arrList)
					listModel.addElement(s);//�𵨿� �ݺ������� �ϳ��� �����ο� �־���
				
				list.setModel(listModel);//����Ʈ�� ���� �־��� 

				// ---------�̰��ؾ� ��ũ�ѹٰ� ������ �� ������ ������ �� ���� -----
				int kkeut = jtarea1.getText().length();
				jtarea1.setCaretPosition(kkeut);// Ŀ���� �ǵڷ�
				// jtarea1.setCaretPosition(0);//Ŀ���� ��ó����
				tk1.beep();
				
			}
		} catch (Exception eee) {
			jtarea1.append("\n" + eee);
			try {
				ChatDAO.getInstance().truncateList();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}// run-end

}// class-end

