package accessChat;

import java.awt.Color;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ser_cli.TcpMulServer;

public class ServerAccess extends JFrame {

	Container cnp;
	
	JLabel lbPort = new JLabel("PORT");
	JTextField txtPort = new JTextField();
	
	JButton btnAccess = new JButton("서버가동");
	
	public ServerAccess() {
		setTitle("서버 가동");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		cnp = getContentPane();
		setLayout(null);
		
		//배경이미지 
		Image bgimg = new ImageIcon(ServerAccess.class.getResource("/image/talkserver.PNG")).getImage();

		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bgimg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		panel.setBounds(0, 0, 893, 593);
		panel.setLayout(null);
		
		
		lbPort.setBounds(80, 110, 50, 40);
		lbPort.setFont(new Font("굴림", Font.BOLD, 14));
		txtPort.setBounds(140,115,200,30);
		txtPort.setFont(new Font("굴림", Font.BOLD, 17));
		txtPort.setText("12345");
		cnp.add(lbPort);
		cnp.add(txtPort);
		
		btnAccess.setBounds(240, 160, 100, 30);
		btnAccess.setBackground(Color.black);
		btnAccess.setForeground(Color.white );
		cnp.add(btnAccess);
		
		btnAccess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String port2 = txtPort.getText().trim();
				
				if(port2.length() == 0) {
					JOptionPane.showMessageDialog(null, "포트번호를 입력하셔야 합니다.");

				}else{
					try {
						new TcpMulServer(Integer.parseInt(port2));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		});
		
		cnp.add(panel);
		setBounds(100,100,430,340);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ServerAccess();
	}

}
