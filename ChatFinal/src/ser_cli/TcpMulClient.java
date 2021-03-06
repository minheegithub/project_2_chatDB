package ser_cli;

import java.net.*;

import java.io.*;
import java.util.Scanner;

import view.KajaClientGUI;

public class TcpMulClient{
	
	public TcpMulClient(String ip, String port, String nickname) {
		try { // ip주소 //포트번호
			Socket s1 = new Socket(ip, Integer.parseInt(port));
			System.out.println("클라이언트에서 서버에 찾아감.....");

			DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
			DataInputStream inputStream = new DataInputStream(s1.getInputStream());
//			outputStream.writeUTF("##" + nickname);//닉네임 먼저 보내기.
			outputStream.writeUTF(nickname);//닉네임 먼저 보내기.
			// 닉네임 먼저 보내는 것은 같다

			new KajaClientGUI(outputStream, inputStream, nickname){
				
				public void closeWork() throws IOException {
					outputStream.close();
					inputStream.close();
					System.exit(0);
				}
			}; // new KajaClientGUI -end
				
		} catch (Exception e) {
			// e.printStackTrace();//주석을 달아야 에러시 화면이 부드럽게 진행
		}
	}
}



