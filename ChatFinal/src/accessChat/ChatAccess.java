package accessChat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chatDB.ChatDAO;
import ser_cli.TcpMulClient;

public class ChatAccess extends JFrame {
	
	Container cnp;
	
	JLabel lbIP = new JLabel("Server IP");
	JLabel lbPort = new JLabel("Server PORT");
	JLabel lbNickname = new JLabel("NICKNAME");
	
	JTextField txtIp = new JTextField();
	JTextField txtPort = new JTextField();
	JTextField txtNickname = new JTextField();
	
	JButton btnAccess = new JButton("입장");
	
	public ChatAccess() {
		setTitle("채팅방 입장");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		cnp = getContentPane();
		setLayout(null);
		
		//배경이미지 
		Image bgimg = new ImageIcon(ChatAccess.class.getResource("/image/talktalk.PNG")).getImage();

		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bgimg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		panel.setBounds(0, 0, 893, 593);
		panel.setLayout(null);
		
		//ip
		lbIP.setBounds(46, 396, 99, 31);
		lbIP.setFont(new Font("굴림", Font.BOLD, 17));
		txtIp.setBounds(172, 400, 221, 31);
		txtIp.setFont(new Font("굴림", Font.BOLD, 17));
		txtIp.setText("127.0.0.1");
		cnp.add(lbIP);
		cnp.add(txtIp);
		
		//port
		lbPort.setBounds(46, 339, 120, 31);
		lbPort.setFont(new Font("굴림", Font.BOLD, 17));
		txtPort.setBounds(172, 339, 221, 31);
		txtPort.setFont(new Font("굴림", Font.BOLD, 17));
		txtPort.setText("12345");
		cnp.add(lbPort);
		cnp.add(txtPort);
		
		//nickname
		lbNickname.setBounds(46, 274, 99, 37);
		lbNickname.setFont(new Font("굴림", Font.BOLD, 17));
		txtNickname.setBounds(172, 278, 221, 31);
		txtNickname.setFont(new Font("굴림", Font.BOLD, 17));
		
		cnp.add(lbNickname);
		cnp.add(txtNickname);
		
		btnAccess.setBounds(46, 465, 351, 37);
		btnAccess.setBackground(Color.BLACK);
		btnAccess.setForeground(Color.white);
		btnAccess.setFont(new Font("굴림", Font.BOLD, 17));
		cnp.add(btnAccess);
		
		btnAccess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ip2 = txtIp.getText().trim();
				String port2 = txtPort.getText().trim();
				String nickname2 = txtNickname.getText().trim(); 
				
				if(ip2.length() == 0 || port2.length() == 0 || nickname2.length() == 0) {
					JOptionPane.showMessageDialog(null, "아이피 ,포트번호 또는 닉네임을 입력하셔야 합니다.");

				}else{
					boolean chkuser;
					try {
						chkuser = ChatDAO.getInstance().checkUser(nickname2);//현재 접속한 동일한 유저네임이 있는지 체크
						if (chkuser) {
							JOptionPane.showMessageDialog(null, "현재 동일한 닉네임으로 접속중입니다.");
						}else {
							new TcpMulClient(ip2, port2, nickname2);
							setVisible(false);
						}
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});

		txtNickname.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip2 = txtIp.getText().trim();
				String port2 = txtPort.getText().trim();
				String nickname2 = txtNickname.getText().trim();
				if(ip2.length() == 0 || port2.length() == 0 || nickname2.length() == 0) {
					JOptionPane.showMessageDialog(null, "아이피 ,포트번호 또는 닉네임을 입력하셔야 합니다.");

				}else{
					boolean chkuser;
					try {
						chkuser = ChatDAO.getInstance().checkUser(nickname2);//현재 접속한 동일한 유저네임이 있는지 체크
						if (chkuser) {
							JOptionPane.showMessageDialog(null, "현재 동일한 닉네임으로 접속중입니다.");
						}else {
							new TcpMulClient(ip2, port2, nickname2);
							setVisible(false);
						}
					} catch (ClassNotFoundException | SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
		});
		cnp.add(panel);
		setBounds(550,100, 452, 597);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ChatAccess();
	}

}
