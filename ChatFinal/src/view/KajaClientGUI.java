package view;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import chatDB.ChatDAO;


//public class KajaClientGUI extends JFrame implements Runnable, ActionListener {
public class KajaClientGUI extends JFrame implements Runnable, ActionListener {

	
	ChatDAO dao;
	private JPanel contentPane;//+
	
	// console모드에서 넘어오는 3개 인자를 받아 저장할 준비필드
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;
	
	JTextArea jtarea1 = new JTextArea();
	JTextField jtfield1 = new JTextField();
	JScrollPane jScrollPane = new JScrollPane(jtarea1);
//    JTextField textField = new JTextField();
	JLabel lbNick = new JLabel();
	
	//접속자리스트
//	Vector user_list = new Vector();//
	JList list = new JList();//User_list
	
	JButton btnSelect = new JButton("이전대화내용 불러오기");//+
	JButton btnSend = new JButton("전송");//
	
	JPopupMenu popup = new JPopupMenu();
	JMenuItem sendFile;
	public KajaClientGUI(DataOutputStream outputStream, DataInputStream inputStream, String nickname) throws ClassNotFoundException, SQLException {// 생성자
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		dao = ChatDAO.getInstance();

		contentPane = new JPanel();//+
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));//+
		setContentPane(contentPane);//+
		contentPane.setLayout(null);//+
		
		//배경이미지 
		Image bgimg = new ImageIcon(KajaClientGUI.class.getResource("/image/chatroom4.png")).getImage();

		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bgimg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		panel.setBounds(0, 0, 787, 581);
		panel.setLayout(null);//+
		
		btnSelect.setBackground(SystemColor.activeCaption);//
		btnSelect.setBounds(372, 31, 194, 33);//
		panel.add(btnSelect);//
		btnSelect.setFont(new Font("굴림", Font.BOLD, 15));//		
		btnSelect.addActionListener(this);
	      
	      JLabel lbuser = new JLabel("닉네임");//
	      lbuser.setForeground(Color.WHITE);//
	      lbuser.setBackground(Color.BLACK);//
	      lbuser.setFont(new Font("굴림", Font.BOLD, 14));//
	      lbuser.setBounds(581, 27, 62, 33);//
	      contentPane.add(lbuser);//
	      
	      //닉네임
	    /*  textField.setBounds(645, 27, 89, 26);
	      textField.setColumns(10);
	      contentPane.add(textField);*/
	      lbNick.setBounds(645, 27, 89, 26);
	      lbNick.setFont(new Font("굴림", Font.BOLD, 17));
	      lbNick.setForeground(Color.white);
	      contentPane.add(lbNick);
	      
	      JLabel lbList = new JLabel("접속자 목록");//
	      lbList.setForeground(Color.WHITE);//
	      lbList.setBackground(Color.BLACK);//
	      lbList.setFont(new Font("굴림", Font.BOLD, 19));//
	      lbList.setBounds(581,68, 133, 33);//
	      contentPane.add(lbList);//
	      
	      
	      btnSend.setBackground(SystemColor.activeCaption);//
	      btnSend.setBounds(580, 505, 105, 30);//
	      panel.add(btnSend);//
	      btnSend.setFont(new Font("굴림", Font.BOLD, 17));//

	      btnSend.addActionListener(this);

		jtarea1.setFont(new Font("굴림", Font.BOLD, 18));

		jtarea1.setEditable(false);

		// chat 입력
		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("굴림", Font.BOLD, 18));
		jtfield1.setBounds(42, 501, 524, 38);//
		panel.add(jtfield1);//
		jtfield1.setColumns(10);//
		jtfield1.addActionListener(this);
		
		
	    list.setBounds(581, 105, 151, 380);//
		list.setFont(new Font("굴림", Font.BOLD, 19));
		panel.add(list);//
		
		jScrollPane.setBounds(42, 76, 525, 413);//
		panel.add(jScrollPane);//

		
		contentPane.add(panel);//
		setBounds(550, 100, 795, 617);
//		setSize(800, 800);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				dispose();
				System.exit(0);
				setVisible(false);
			}
		}
		);
		
		 sendFile=new JMenuItem("파일전송");
	        sendFile.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("파일전송하기~~~~~~~~~~~~~!");
		            new SendFile(outputStream,inputStream,nickname,(String)list.getSelectedValue());
					
				}
			});
	      popup.add(sendFile); 
		
		// 서버로 부터 받아 textarea에 뿌려주는 쓰레드
//		textField.setText(nickname);
	    lbNick.setText(nickname);
		Thread th1 = new Thread(this);
		th1.start();
		
	}// 생성자-end

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend || e.getSource() == jtfield1) {
			try {
				System.out.println(nickname+"클라이언트에서 output으로 내보내는 대화내용 : "+nickname + " ▶ " + jtfield1.getText());
				outputStream.writeUTF(nickname + " ▶ " + jtfield1.getText());
				// nickname과 client의 chat을 서버로
			
			} catch (IOException ee) {
				// ee.printStackTrace();
			}
			jtfield1.setText("");
		}else if(e.getSource() == btnSelect){
			try {
				new ShowData().setVisible(true);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}// actionPerformed - end

	Toolkit tk1 = Toolkit.getDefaultToolkit();
	
	public void run() { // for 받는 thread
		try {
			while (true) {
				String strServer1 = inputStream.readUTF();
				if (strServer1 == null) {
					jtarea1.append("\n" + "종료");
					return;
				}
				System.out.println(nickname+"클라이언트에서 input으로 들어온 대화 내용"+strServer1);
				jtarea1.append("\n" + strServer1);	
				

				DefaultListModel listModel = new DefaultListModel<>();//+, 리스트에들어갈 모델 생성 
				ArrayList<String> arrList = dao.CurrentMem(); //모델에 넣어줄 어레이리스트와 거기에 들어갈 현재접속인원 테이블 
				for(String s : arrList)
					listModel.addElement(s);//모델에 반복문으로 하나씩 접속인원 넣어줌
				
				list.setModel(listModel);//리스트에 모델을 넣어줌 
				
				 list.addMouseListener(new MouseAdapter() {
			        	@Override
			        	public void mousePressed(MouseEvent e) {
			        		System.out.println("마우스 누른다.");
			        		int value = e.getButton();//마우스누르면 int값 생성
			        		System.out.println(value);
			                if(value==3){ //오른쪽 마우스 누르면
			                	
			                if(list.getSelectedIndex()!=-1 && !(list.getSelectedValue().equals(nickname))) {
			                	System.out.println("팝업 나오나");     
//			                	   //☆★☆★☆★      팝업선택    ☆★☆★☆★     
			                            popup.show(e.getComponent(),e.getX(),e.getY());
			                            popup.setVisible(true);
			                }
			                   
//			                    if(list.getSelectedIndex()==-1){
////			                    	JOptionPane.showMessageDialog( null, "대상을 선택하세요");
//			                   }else if(list.getSelectedValue().equals(nickname)){
////			                	   JOptionPane.showMessageDialog( null, nickname+"님 본인말고 다른사용자를 선택하세요");
//			                   }else{                   
////			                	   JOptionPane.showMessageDialog( null, list.getSelectedValue()+"님에게 파일을 보냅니다.");
//			                       
//			                	   System.out.println("팝업 나오나");     
//			                	   //☆★☆★☆★      팝업선택    ☆★☆★☆★     
//			                            popup.show(e.getComponent(),e.getX(),e.getY());
//			                            popup.setVisible(true);
//			                     }
			                }
			        	}
			        
			        });
				 
	

				// ---------이것해야 스크롤바가 생긴후 맨 마지막 내용이 잘 보임 -----
				int kkeut = jtarea1.getText().length();
				jtarea1.setCaretPosition(kkeut);// 커서를 맨뒤로
				// jtarea1.setCaretPosition(0);//커서를 맨처음에
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

	//ui만들때 사용
//	public static void main(String[] args) {
//		new KajaClientGUI();
//	}

}// class-end

