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
			s1 = ss1.accept();//클라이언트마다 포트번호 다르다.
			System.out.println("TcpMulServer 클라이언트 아이피 주소: " + s1.getInetAddress() + " , 클라이언트 접속포트: " + s1.getPort()+", 서버포트:"+s1.getLocalPort());
			serverIp = s1.getLocalAddress().toString();
			clientIp = s1.getInetAddress().toString();//+
			serverPort = s1.getLocalPort();//+
//			System.out.println(serverIp.substring(1, serverIp.length()));
			serverIp = serverIp.substring(1, serverIp.length());
			clientIp = clientIp.substring(1, clientIp.length());
			// 한명이 접속하면 ThreadServerClass 쓰레드에 올려놓음
			ThreadServerClass tServer1 = new ThreadServerClass(s1,serverIp, clientIp, serverPort);
			tServer1.start();

			threadList.add(tServer1);
			System.out.println("TcpMulServer접속자 수 : " + threadList.size());//클라이언트가 나갔을때 콘솔에 표시
		} // whle-end

	}//생성자
	//스레드 클래스(사람이 접속할때마다 생성이 된다.)
	class ThreadServerClass extends Thread {// 한명 접속마다 처리할 쓰레드 클래스
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
			
			System.out.println("TcpMulServer 소켓:"+s1);
			inputStream = new DataInputStream(s1.getInputStream());
			outputStream = new DataOutputStream(s1.getOutputStream());
			
		}

		@Override
		public void run() { // remember !!!!!! 한사람 서버로 접속한 경우임
			try {
				if (inputStream != null) {
					try {
						nickname = inputStream.readUTF();
						ChatDAO.getInstance().insertList(nickname);//현접속명단에 저장 
						ChatDAO.getInstance().insertUser(nickname);//userList명단에 추가, 접속 history
						sendChat("☞"+nickname + " 님 입장하셨습니다.");//모든 클라이언트에 알린다.outputStream
						userList.add(nickname);
					} catch (ClassNotFoundException | SQLException e) {
					}

				}
				//클라이언트 한사람에게 들어온 대화 내용을 다른사람들에게 뿌려줄 준비를 하겠다.
				while (inputStream != null) {
					contents = inputStream.readUTF();
					if(contents.split("/w").length == 1) {  
						try {
							//귓속말이 아닌경우에만 대화내용 db에 저장
							ChatDAO.getInstance().insertDb(nickname, serverIp1, clientIp1, serverPort1, contents);
						} catch (ClassNotFoundException | SQLException e) {
						}
					}
					//클라이언트가 보낸 채팅 내용을 sendChat메소드를 이용해 접속한 사람(들)에게 내보내기로 한다.
					sendChat(contents); //outputStream
				
				} // 정상채팅의 경우는 계속 while 문안에서 반복 loop

			} catch (IOException e) { // 여기로 왔단 얘기는 에러가 발생한 것 //나가버린 경우
				System.out.println("TcpMulServer IOException : "+nickname+"나갔다."); 
			} finally {
				System.out.println("finally");
				for (int i = 0; i < threadList.size(); i++) {
					
					if (socket1.equals(threadList.get(i).socket1)) {
						try {
							threadList.remove(i);
							ChatDAO.getInstance().deleteList(nickname);//현접속인원 테이블에서 삭제
							sendChat("☞"+nickname + " 님 께서 퇴장하셨습니다. ");
							userList.remove(i);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				System.out.println("TcpServer 퇴장 후 접속자 수 : " + threadList.size() + " 명");
			} // finally-end

		}// run-end
	}// ThreadServerClass-end
	
	public void sendChat(String chat) throws IOException {
		//닉네임 정지훈이 채팅방에 귓속말로 김태희에게 "하이"라고 입력했을때
	      //입력 : /w 김태희 하이
	      //실제 보내지는 내용 : 정지훈 ▶ /w 김태희 하이 로 chat을 받음
	      System.out.println("TcpMulServer "+chat);  //서버 확인 차 작성
	      String chatset="";
	      //gui에서 입력된 chat값을 받아 옴
//        outputStream.writeUTF(nickname+"-->" + jtfield1.getText());
	      if(chat.split("/w").length !=1) { //만약 chat에 /w 이라는 문구가 존재한다면
//	    	    정지훈 ▶ /w 김태희 하이 방가
    		  String w[] = chat.split("/w");   //저장되는 값 [정지훈 ▶ , 김태희 하이 방가]
	    	  String from=w[0].split("▶")[0].trim(); //from 뒤에 공백이 하나 있었음 , 정지훈
	          String w2[]=w[1].split(" ");  //저장되는 값 [ ,김태희,하이,방가]
	          String to=w2[1];//김태희
	          for(int z=2;z<w2.length;z++) {
	            chatset+=w2[z]+" "; //띄어쓰기 구분으로 배열에 들어가있기 때문에 함께 넣어줌
	          }
	    				  
			  for(int i=0; i<threadList.size(); i++) {
				  if(userList.get(i).equals(to)) {
					  String send="[귓속말 수신]"+from+"님에게 옴"+"\n"+"<< "+chatset;
					  threadList.get(i).outputStream.writeUTF(send);	                    
				  }
				  if(userList.get(i).equals(from)) {//똑같이 보낸쪽 쓰레드도 찾아서 보내줌
					  String send="[귓속말 송신]"+to+"님에게 보냄"+"\n"+"<< "+chatset;
					  threadList.get(i).outputStream.writeUTF(send);	                    
				  }
			  }

	       }else{ //귓속말이 아니면 모든 사람에게 전송
	         for(int i=0;i<threadList.size();i++)
	        	 threadList.get(i).outputStream.writeUTF(chat);//모든 클라이언트들에게 뿌려준다.
	             //처음에 nickname이  채팅관련 모든 사람에게 전송      
	      }
	   }//sendChat-end

}// ServerClass-end