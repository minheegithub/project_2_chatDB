package ser_cli;

import java.net.*;

import java.io.*;
import java.util.Scanner;

import view.KajaClientGUI;

public class TcpMulClient{
	
	public TcpMulClient(String ip, String port, String nickname) {
		try { // ip�ּ� //��Ʈ��ȣ
			Socket s1 = new Socket(ip, Integer.parseInt(port));

			DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
			DataInputStream inputStream = new DataInputStream(s1.getInputStream());
			outputStream.writeUTF(nickname);//�г��� ���� ������.

			new KajaClientGUI(outputStream, inputStream, nickname){
				
				public void closeWork() throws IOException {
					outputStream.close();
					inputStream.close();
					System.exit(0);
				}
			}; // new KajaClientGUI -end
				
		} catch (Exception e) {
			// e.printStackTrace();//�ּ��� �޾ƾ� ������ ȭ���� �ε巴�� ����
		}
	}
}



