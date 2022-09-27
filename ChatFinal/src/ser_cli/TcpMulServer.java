package ser_cli;

import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.*;

import chatDB.ChatDAO;

public class TcpMulServer{
    
	ArrayList<ThreadServerClass> threadList = new ArrayList<ThreadServerClass>();
    ArrayList<String> userList = new ArrayList<String>();

    String contents;

	ServerSocket ss1;
	Socket s1;
	
	public TcpMulServer(int portno) throws IOException, ClassNotFoundException, SQLException {
		
		String serverIp = "";
		String clientIp = "";
		int serverPort = 0;

		Socket s1 = null;
		
		ss1 = new ServerSocket(portno);

		while (true) {
			s1 = ss1.accept();//Ŭ���̾�Ʈ���� ��Ʈ��ȣ �ٸ���.
			System.out.println("TcpMulServer Ŭ���̾�Ʈ ������ �ּ�: " + s1.getInetAddress() + " , Ŭ���̾�Ʈ ������Ʈ: " + s1.getPort()+", ������Ʈ:"+s1.getLocalPort());
			serverIp = s1.getLocalAddress().toString();
			clientIp = s1.getInetAddress().toString();//+
			serverPort = s1.getLocalPort();//+
//			System.out.println(serverIp.substring(1, serverIp.length()));
			serverIp = serverIp.substring(1, serverIp.length());
			clientIp = clientIp.substring(1, clientIp.length());
			// �Ѹ��� �����ϸ� ThreadServerClass �����忡 �÷�����
			ThreadServerClass tServer1 = new ThreadServerClass(s1,serverIp, clientIp, serverPort);
			tServer1.start();

			threadList.add(tServer1);
			System.out.println("TcpMulServer������ �� : " + threadList.size());//Ŭ���̾�Ʈ�� �������� �ֿܼ� ǥ��
		} // whle-end

	}//������
	//������ Ŭ����(����� �����Ҷ����� ������ �ȴ�.)
	class ThreadServerClass extends Thread {// �Ѹ� ���Ӹ��� ó���� ������ Ŭ����
		Socket socket1;
		DataInputStream inputStream;
		DataOutputStream outputStream;
		String nickname;
		
		String serverIp1;
		String clientIp1;
		int serverPort1;
		public ThreadServerClass(Socket s1, String serverIp1, String clientIp1, int serverPort1) throws IOException {
			socket1 = s1;
			this.serverIp1 = serverIp1;
			this.clientIp1 = clientIp1;
			this.serverPort1 = serverPort1;
			
			System.out.println("TcpMulServer ����:"+s1);
			inputStream = new DataInputStream(s1.getInputStream());
			outputStream = new DataOutputStream(s1.getOutputStream());
			
		}

		@Override
		public void run() { // remember !!!!!! �ѻ�� ������ ������ �����
			try {
				if (inputStream != null) {
					try {
						nickname = inputStream.readUTF();
						ChatDAO.getInstance().insertList(nickname);//�����Ӹ�ܿ� ���� 
						ChatDAO.getInstance().insertUser(nickname);//userList��ܿ� �߰�, ���� history
						sendChat("��"+nickname + " �� �����ϼ̽��ϴ�.");//��� Ŭ���̾�Ʈ�� �˸���.outputStream
						userList.add(nickname);
					} catch (ClassNotFoundException | SQLException e) {
					}

				}
				//Ŭ���̾�Ʈ �ѻ������ ���� ��ȭ ������ �ٸ�����鿡�� �ѷ��� �غ� �ϰڴ�.
				while (inputStream != null) {
					contents = inputStream.readUTF();
					if(contents.split("/w").length == 1) {  
						try {
							//�ӼӸ��� �ƴѰ�쿡�� ��ȭ���� db�� ����
							ChatDAO.getInstance().insertDb(nickname, serverIp1, clientIp1, serverPort1, contents);
						} catch (ClassNotFoundException | SQLException e) {
						}
					}
					//Ŭ���̾�Ʈ�� ���� ä�� ������ sendChat�޼ҵ带 �̿��� ������ ���(��)���� ��������� �Ѵ�.
					sendChat(contents); //outputStream
				
				} // ����ä���� ���� ��� while ���ȿ��� �ݺ� loop

			} catch (IOException e) { // ����� �Դ� ���� ������ �߻��� �� //�������� ���
				System.out.println("TcpMulServer IOException : "+nickname+"������."); 
			} finally {
				System.out.println("finally");
				for (int i = 0; i < threadList.size(); i++) {
					
					if (socket1.equals(threadList.get(i).socket1)) {
						try {
							threadList.remove(i);
							ChatDAO.getInstance().deleteList(nickname);//�������ο� ���̺��� ����
							sendChat("��"+nickname + " �� ���� �����ϼ̽��ϴ�. ");
							userList.remove(i);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				System.out.println("TcpServer ���� �� ������ �� : " + threadList.size() + " ��");
			} // finally-end

		}// run-end
	}// ThreadServerClass-end
	
	public void sendChat(String chat) throws IOException {
		//�г��� �������� ä�ù濡 �ӼӸ��� �����񿡰� "����"��� �Է�������
	      //�Է� : /w ������ ����
	      //���� �������� ���� : ������ �� /w ������ ���� �� chat�� ����
	      System.out.println("TcpMulServer "+chat);  //���� Ȯ�� �� �ۼ�
	      String chatset="";
	      //gui���� �Էµ� chat���� �޾� ��
//        outputStream.writeUTF(nickname+"-->" + jtfield1.getText());
	      if(chat.split("/w").length !=1) { //���� chat�� /w �̶�� ������ �����Ѵٸ�
//	    	    ������ �� /w ������ ���� �氡
    		  String w[] = chat.split("/w");   //����Ǵ� �� [������ �� , ������ ���� �氡]
	    	  String from=w[0].split("��")[0].trim(); //from �ڿ� ������ �ϳ� �־��� , ������
	          String w2[]=w[1].split(" ");  //����Ǵ� �� [ ,������,����,�氡]
	          String to=w2[1];//������
	          for(int z=2;z<w2.length;z++) {
	            chatset+=w2[z]+" "; //���� �������� �迭�� ���ֱ� ������ �Բ� �־���
	          }
	    				  
			  for(int i=0; i<threadList.size(); i++) {
				  if(userList.get(i).equals(to)) {
					  String send="[�ӼӸ� ����]"+from+"�Կ��� ��"+"\n"+"<< "+chatset;
					  threadList.get(i).outputStream.writeUTF(send);	                    
				  }
				  if(userList.get(i).equals(from)) {//�Ȱ��� ������ �����嵵 ã�Ƽ� ������
					  String send="[�ӼӸ� �۽�]"+to+"�Կ��� ����"+"\n"+"<< "+chatset;
					  threadList.get(i).outputStream.writeUTF(send);	                    
				  }
			  }

	       }else{ //�ӼӸ��� �ƴϸ� ��� ������� ����
	         for(int i=0;i<threadList.size();i++)
	        	 threadList.get(i).outputStream.writeUTF(chat);//��� Ŭ���̾�Ʈ�鿡�� �ѷ��ش�.
	             //ó���� nickname��  ä�ð��� ��� ������� ����      
	      }
	   }//sendChat-end

}// ServerClass-end