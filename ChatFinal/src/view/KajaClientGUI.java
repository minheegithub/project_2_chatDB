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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	JButton btnBring = new JButton("������ȭ");
	JButton btnFile = new JButton("���Ͽ���");
	JButton btnSendFile = new JButton("��������");
	JButton btnWhisper = new JButton("�ӼӸ�");
	
	JLabel fileLb = new JLabel();//���� �̸�
	
	File f;
	byte[] byteBae;
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
		
        btnWhisper.setBackground(SystemColor.activeCaption);
        btnWhisper.setBounds(460, 30, 105, 30);
        panel.add(btnWhisper);
        btnWhisper.setFont(new Font("����", Font.BOLD, 17));
        btnWhisper.addActionListener(this);
		
		btnBring.setBackground(SystemColor.activeCaption);
		btnBring.setBounds(340, 30, 105, 30);
		panel.add(btnBring);
		btnBring.setFont(new Font("����", Font.BOLD, 17));		
		btnBring.addActionListener(this);
		
        btnFile.setBackground(SystemColor.activeCaption);
        btnFile.setBounds(40, 30, 105, 30);
        panel.add(btnFile);
        btnFile.setFont(new Font("����", Font.BOLD, 17));
        btnFile.addActionListener(this);
		
		fileLb.setForeground(Color.WHITE);
		fileLb.setBackground(Color.BLACK);
		fileLb.setFont(new Font("����", Font.BOLD, 17));
		fileLb.setBounds(160, 30, 210, 33);
        contentPane.add(fileLb);
	      
        JLabel lbuser = new JLabel("�г���");
        lbuser.setForeground(Color.WHITE);
        lbuser.setBackground(Color.BLACK);
        lbuser.setFont(new Font("����", Font.BOLD, 17));
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
      
        btnSendFile.setBackground(SystemColor.activeCaption);
        btnSendFile.setBounds(600, 501, 105, 30);
        panel.add(btnSendFile);
        btnSendFile.setFont(new Font("����", Font.BOLD, 17));
        btnSendFile.addActionListener(this);
        btnSendFile.setVisible(false);
        
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
		if (e.getSource() == jtfield1) {
			try {
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
		}else if(e.getSource() == btnFile) {
			JFileChooser choice = new JFileChooser();
//			Ȯ���ڸ� ����("�������� Ȯ����ǥ��","Ȯ����")
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, jpg", "jpg");
			choice.setFileFilter(filter);
			//�����ִ� �޼ҵ� 
			//null - ��üȭ���� �߾�
			//�����ư - approve �ɼ� ��ȯ, ��ҹ�ư - cancel�ɼ� ��ȯ
			int returnVal = choice.showOpenDialog(null);
			if(returnVal != JFileChooser.APPROVE_OPTION){
				JOptionPane.showMessageDialog(null, "���ϼ����� �ϼ���", "���", JOptionPane.WARNING_MESSAGE);
				return;			
			}
			//���õ� ������ ��ΰ� ������
			String filePath = choice.getSelectedFile().getPath();
			System.out.println(filePath);
			f = choice.getSelectedFile();
			System.out.println(f.getName());
			fileLb.setText(f.getName());

			FileInputStream fis1;
			try {
				fis1 = new FileInputStream(f);
				DataInputStream dis1 = new DataInputStream(fis1);
				byteBae = new byte[(int)f.length()];
				//������ ���̸�ŭ ����Ʈ �迭�� ����
				System.out.println("file--> byte��....");
				dis1.readFully(byteBae);//*���ϳ��� -->����Ʈ �迭�� �b�´�.* 100����Ƽ��� 1000100100....
				btnSendFile.setVisible(true);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(e.getSource() == btnSendFile) {
			String name = (String)list.getSelectedValue();
			if(name == null) {
				JOptionPane.showMessageDialog(this, "���Ӹ�� �Ѹ��� �������ּ���");
			}else {
				String fileText = " /f "+name+" "+f.getName();//
				// nickname�� client�� chat�� ������
				try {
					outputStream.writeUTF(nickname + " �� " + fileText);
					outputStream.writeInt(byteBae.length);//���� ���� ���� ���� 4����Ʈ Ȯ��, 0100001/  100�� ������. 
					outputStream.write(byteBae);//������ü�� ����Ʈ �迭 ����
					System.out.println("�����ߴ�~~~");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else if(e.getSource() == btnWhisper) {
			String name = (String)list.getSelectedValue();
			if(name == null) {
				JOptionPane.showMessageDialog(this, "���Ӹ�� �Ѹ��� �������ּ���");
			}else {
				String fileText = " /w "+name+" "+jtfield1.getText();
				// nickname�� client�� chat�� ������
				try {
					outputStream.writeUTF(nickname + " �� " + fileText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jtfield1.setText("");
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
				if(strServer1.split("/f").length == 2) {
//					JOptionPane.showMessageDialog(this, "������ �����߽��ϴ�.");
					int result = JOptionPane.showConfirmDialog(this, "������ �����ðڽ��ϱ�?","���� Ȯ��",JOptionPane.YES_NO_OPTION);
					
					if(result == JOptionPane.YES_OPTION) {//����ڰ� ���� ������ ���
						
						String bea[] = strServer1.split("/f");
						jtarea1.append("\n" + bea[1]);
						String filename1 = inputStream.readUTF();
						jtarea1.append("\n" + filename1+" �ٿ�ε� �Ϸ�");
						//file����, ����޾� ---> byte�迭��
						int len1 = inputStream.readInt();//������ ���� ���� ���� ���� �޾ƿ�
						byte[] byteBae2 = new byte[len1];
						inputStream.readFully(byteBae2);//�׸��� ����޾� ����Ʈ �迭�� 
						
//						File f = new File("C:/"+nickname+"chat");
						File f = new File(".\\src\\"+nickname+"FileDown");
						if(!f.exists()) {
							f.mkdir();
						}
						
						FileOutputStream fos1 = new FileOutputStream(".\\src\\"+nickname+"FileDown\\"+filename1);
						fos1.write(byteBae2);//���� ����Ʈ �迭 ---> ����
					
					}else{//�پ��̾� �α� â�� ���� ��� + �ƴϿ��� ������ ���
						
						String bea[] = strServer1.split("/f");
						jtarea1.append("\n" + bea[1]);
						String filename1 = inputStream.readUTF();
						jtarea1.append("\n" + filename1+" ���� ���� �ź�");
						//file����, ����޾� ---> byte�迭��
						int len1 = inputStream.readInt();//������ ���� ���� ���� ���� �޾ƿ�
						byte[] byteBae2 = new byte[len1];
						inputStream.readFully(byteBae2);//�׸��� ����޾� ����Ʈ �迭�� 
					
					}
				
				
				}else {
					
					jtarea1.append("\n" + strServer1);	
				}
				
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

