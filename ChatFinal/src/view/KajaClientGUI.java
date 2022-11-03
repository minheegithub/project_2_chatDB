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
	
	// console모드에서 넘어오는 3개 인자를 받아 저장할 준비필드
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;
	
	JTextArea jtarea1 = new JTextArea();
	JTextField jtfield1 = new JTextField();
	JScrollPane jScrollPane = new JScrollPane(jtarea1);
	JLabel lbNick = new JLabel();
	
	//접속자리스트
	JList list = new JList();//User_list
	
	JButton btnBring = new JButton("이전대화");
	JButton btnFile = new JButton("파일열기");
	JButton btnSendFile = new JButton("파일전송");
	JButton btnWhisper = new JButton("귓속말");
	
	JLabel fileLb = new JLabel();//파일 이름
	
	File f;
	byte[] byteBae;
	public KajaClientGUI(DataOutputStream outputStream, DataInputStream inputStream, String nickname) throws ClassNotFoundException, SQLException {// 생성자
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		dao = ChatDAO.getInstance();
	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		panel.setLayout(null);
		
        btnWhisper.setBackground(SystemColor.activeCaption);
        btnWhisper.setBounds(460, 30, 105, 30);
        panel.add(btnWhisper);
        btnWhisper.setFont(new Font("굴림", Font.BOLD, 17));
        btnWhisper.addActionListener(this);
		
		btnBring.setBackground(SystemColor.activeCaption);
		btnBring.setBounds(340, 30, 105, 30);
		panel.add(btnBring);
		btnBring.setFont(new Font("굴림", Font.BOLD, 17));		
		btnBring.addActionListener(this);
		
        btnFile.setBackground(SystemColor.activeCaption);
        btnFile.setBounds(40, 30, 105, 30);
        panel.add(btnFile);
        btnFile.setFont(new Font("굴림", Font.BOLD, 17));
        btnFile.addActionListener(this);
		
		fileLb.setForeground(Color.WHITE);
		fileLb.setBackground(Color.BLACK);
		fileLb.setFont(new Font("굴림", Font.BOLD, 17));
		fileLb.setBounds(160, 30, 210, 33);
        contentPane.add(fileLb);
	      
        JLabel lbuser = new JLabel("닉네임");
        lbuser.setForeground(Color.WHITE);
        lbuser.setBackground(Color.BLACK);
        lbuser.setFont(new Font("굴림", Font.BOLD, 17));
        lbuser.setBounds(581, 27, 62, 33);
        contentPane.add(lbuser);
      
        //닉네임
        lbNick.setBounds(645, 27, 89, 26);
        lbNick.setFont(new Font("굴림", Font.BOLD, 17));
        lbNick.setForeground(Color.white);
        contentPane.add(lbNick);
      
        JLabel lbList = new JLabel("접속자 목록");
        lbList.setForeground(Color.WHITE);
        lbList.setBackground(Color.BLACK);
        lbList.setFont(new Font("굴림", Font.BOLD, 19));
        lbList.setBounds(581,68, 133, 33);
        contentPane.add(lbList);
      
        btnSendFile.setBackground(SystemColor.activeCaption);
        btnSendFile.setBounds(600, 501, 105, 30);
        panel.add(btnSendFile);
        btnSendFile.setFont(new Font("굴림", Font.BOLD, 17));
        btnSendFile.addActionListener(this);
        btnSendFile.setVisible(false);
        
		jtarea1.setFont(new Font("굴림", Font.BOLD, 18));
		jtarea1.setEditable(false);

		// chat 입력
		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("굴림", Font.BOLD, 18));
		jtfield1.setBounds(42, 501, 524, 38);
		panel.add(jtfield1);
		jtfield1.setColumns(10);
		jtfield1.addActionListener(this);
		
	    list.setBounds(581, 105, 151, 380);
		list.setFont(new Font("굴림", Font.BOLD, 19));
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
		
		// 서버로 부터 받아 textarea에 뿌려주는 쓰레드
	    lbNick.setText(nickname);
		Thread th1 = new Thread(this);
		th1.start();
		
	}// 생성자-end

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jtfield1) {
			try {
				// nickname과 client의 chat을 서버로
				outputStream.writeUTF(nickname + " ▶ " + jtfield1.getText());
			
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
//			확장자명 설정("파일유형 확장자표시","확장자")
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, jpg", "jpg");
			choice.setFileFilter(filter);
			//열어주는 메소드 
			//null - 전체화면의 중앙
			//열기버튼 - approve 옵션 반환, 취소버튼 - cancel옵션 반환
			int returnVal = choice.showOpenDialog(null);
			if(returnVal != JFileChooser.APPROVE_OPTION){
				JOptionPane.showMessageDialog(null, "파일선택을 하세요", "경고", JOptionPane.WARNING_MESSAGE);
				return;			
			}
			//선택된 파일의 경로값 얻어오기
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
				//파일의 길이만큼 바이트 배열을 집음
				System.out.println("file--> byte중....");
				dis1.readFully(byteBae);//*파일내용 -->바이트 배열에 얺는다.* 100바이티라면 1000100100....
				btnSendFile.setVisible(true);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(e.getSource() == btnSendFile) {
			String name = (String)list.getSelectedValue();
			if(name == null) {
				JOptionPane.showMessageDialog(this, "접속멤버 한명을 선택해주세요");
			}else {
				String fileText = " /f "+name+" "+f.getName();//
				// nickname과 client의 chat을 서버로
				try {
					outputStream.writeUTF(nickname + " ▶ " + fileText);
					outputStream.writeInt(byteBae.length);//파일 길이 먼저 정송 4바이트 확보, 0100001/  100을 보낸다. 
					outputStream.write(byteBae);//파일자체를 바이트 배열 전송
					System.out.println("전송했당~~~");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else if(e.getSource() == btnWhisper) {
			String name = (String)list.getSelectedValue();
			if(name == null) {
				JOptionPane.showMessageDialog(this, "접속멤버 한명을 선택해주세요");
			}else {
				String fileText = " /w "+name+" "+jtfield1.getText();
				// nickname과 client의 chat을 서버로
				try {
					outputStream.writeUTF(nickname + " ▶ " + fileText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jtfield1.setText("");
			}
		}
	}// actionPerformed - end

	Toolkit tk1 = Toolkit.getDefaultToolkit();
	
	public void run() { // for 받는 thread
		try {
			while (true) {
				
				String strServer1 = inputStream.readUTF();
				System.out.println("KajaClientGUI "+nickname+"runnable 클라이언트에서  input으로 들어온 대화 내용"+strServer1);
				if (strServer1 == null) {
					jtarea1.append("\n" + "종료");
					return;
				}
				if(strServer1.split("/f").length == 2) {
//					JOptionPane.showMessageDialog(this, "파일이 도착했습니다.");
					int result = JOptionPane.showConfirmDialog(this, "파일을 받으시겠습니까?","파일 확인",JOptionPane.YES_NO_OPTION);
					
					if(result == JOptionPane.YES_OPTION) {//사용자가 예를 선택한 경우
						
						String bea[] = strServer1.split("/f");
						jtarea1.append("\n" + bea[1]);
						String filename1 = inputStream.readUTF();
						jtarea1.append("\n" + filename1+" 다운로드 완료");
						//file길이, 내용받아 ---> byte배열로
						int len1 = inputStream.readInt();//서버가 보낸 파일 길이 먼저 받아옴
						byte[] byteBae2 = new byte[len1];
						inputStream.readFully(byteBae2);//그리고 내용받아 바이트 배열로 
						
//						File f = new File("C:/"+nickname+"chat");
						File f = new File(".\\src\\"+nickname+"FileDown");
						if(!f.exists()) {
							f.mkdir();
						}
						
						FileOutputStream fos1 = new FileOutputStream(".\\src\\"+nickname+"FileDown\\"+filename1);
						fos1.write(byteBae2);//받은 바이트 배열 ---> 파일
					
					}else{//다아이얼 로그 창을 닫은 경우 + 아니오를 선택한 경우
						
						String bea[] = strServer1.split("/f");
						jtarea1.append("\n" + bea[1]);
						String filename1 = inputStream.readUTF();
						jtarea1.append("\n" + filename1+" 파일 수신 거부");
						//file길이, 내용받아 ---> byte배열로
						int len1 = inputStream.readInt();//서버가 보낸 파일 길이 먼저 받아옴
						byte[] byteBae2 = new byte[len1];
						inputStream.readFully(byteBae2);//그리고 내용받아 바이트 배열로 
					
					}
				
				
				}else {
					
					jtarea1.append("\n" + strServer1);	
				}
				
				DefaultListModel listModel = new DefaultListModel<>();// 리스트에들어갈 모델 생성 
				ArrayList<String> arrList = dao.CurrentMem(); //모델에 넣어줄 어레이리스트와 거기에 들어갈 현재접속인원 테이블 
				for(String s : arrList)
					listModel.addElement(s);//모델에 반복문으로 하나씩 접속인원 넣어줌
				
				list.setModel(listModel);//리스트에 모델을 넣어줌 

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

}// class-end

