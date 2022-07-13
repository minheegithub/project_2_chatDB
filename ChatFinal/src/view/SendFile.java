package view;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SendFile extends JFrame implements ActionListener{
	 JTextField tf;  //파일경로
	 JButton findbutton, sendbutton, clearbutton;  //파일찾기, 보내기 ,취소 버튼
	 FileDialog fd;  //파일다이아로그
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private String nickname;
	private String selectName;
	public SendFile(DataOutputStream outputStream, DataInputStream inputStream, String nickname, String selectName) {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		this.selectName = selectName;
		
	        tf= new JTextField(17);
	        tf.setEditable(false);
	        findbutton=new JButton("파일찾기");
	        findbutton.setBackground(Color.CYAN);
	        findbutton.addActionListener(this);
	        sendbutton=new JButton("보내기");
	        sendbutton.setBackground(Color.YELLOW);
	        sendbutton.addActionListener(this);
	        clearbutton=new JButton("취소");
	        clearbutton.setBackground(Color.orange);
	        clearbutton.addActionListener(this);
	        this.setLayout(new FlowLayout());
	        add(tf);
	        add(findbutton);
	        add(sendbutton);
	        add(clearbutton);
//	        this.setResizable(false);
	        this.setBounds(300,200,300,100);
	        this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		Object title = ae.getSource();
        //☆★☆★☆★☆★☆★        파일찾기 버튼 클릭      ☆★☆★☆★☆★☆★☆★ 
    if(title.equals(findbutton)){
        fd = new FileDialog(this,"★파일전송★",FileDialog.LOAD);  //파일다이얼로그생성
        fd.setVisible(true);
        tf.setText("");
//☆★☆★☆★☆★☆★☆★         보낼파일과 디렉토리를 저장해놓는다    ☆★☆★☆★☆★☆★☆★
//        wr.directory = fd.getDirectory();  //디렉토리명
//        wr.file = fd.getFile();  //파일명
//   
//        tf.setText(wr.directory+wr.file);     

		System.out.println("디렉토리명+파일명"+fd.getDirectory()+"+"+fd.getFile());
       tf.setText(fd.getDirectory()+fd.getFile());     
       

//☆★☆★☆★☆★☆★☆★      보내기버튼 클릭      ☆★☆★☆★☆★☆★☆★☆★
    }else if(title.equals(sendbutton)){ 
       
        try{
        if(!tf.getText().equals("")){
//            String reUser = wr.ch.l.getSelectedValue()+"";  //파일 받는이
//            wr.section="555";  //파일 받으시겠습니까? 메세지창
//            wr.sendMessage(reUser+"|"+wr.file); //받는이 | 파일명
        	outputStream.writeUTF(nickname+"님"+selectName+"에게 \n" + "|"+fd.getFile()+"파일보냄");
        	
        	this.setVisible(false);
           
        }
        }catch(Exception e){
            System.out.println(e);
        }
    }else {  //취소버튼
        this.setVisible(false);
    }
		
	}
	
//	 //☆★☆★☆★☆★☆★☆★       파일수신측에서 수락하면 전송     ☆★☆★☆★☆★☆★☆★
//    public void FileSend(String ip){
//        this.ip=ip;
//        System.out.println(" 파일 전송 ~~~~~~~");
//        try{
//            fileSocket = new Socket(ip,9797);    //전달하는쪽 ip랑 받는쪽 아피랑 같아야한다.9797동일 
//            System.out.println(ip+"    아이피");   //127.0.0.1 아이피
//        // 파일내용 전송
//        DataInputStream dis = new DataInputStream(new FileInputStream(new File(directory+file)));
//        DataOutputStream dos = new DataOutputStream(fileSocket.getOutputStream());
//        System.out.println(directory+file+"전송되나");   //파일 주고받기 Stream
//       
//        int b=0;
//        while((b=dis.read()) != -1){
//            dos.writeByte(b); //파일전송
//            dos.flush();   //자원정리
//        }
//        fileSocket.close();   //소캣을 닫는다
//        fileSocket = null;
//       
//        }catch(Exception e){
//            System.out.println(e+"파일전송  에러 ");
//        }
//    }
//   
//   
//    //☆★☆★☆★☆★☆★☆★        파일수락--> 소켓연다     ☆★☆★☆★☆★☆★☆★  
//   
//    public void fileSendOk(){
//        try{
//            ss = new ServerSocket(9797);
//            fd=new FileDialog(this,"SENDFILE",FileDialog.SAVE);   //저장 파일 다이얼로그 띄운다.
//            fd.setVisible(true);
//           
//            directory = fd.getDirectory();   //저장할 디렉토리를 열어서 저장된다.
//            file = fd.getFile();             //파일을 얻는다.
//           
//            f = new File(directory+file);//새로운 파일객체생성
//            FileOutputStream out = new FileOutputStream(f);
//            System.out.println(directory + file+ "받는파일이름 ");
//            section = "505";
//            sendMessage(SendUser+"|"+socket.getInetAddress().getHostAddress());  //아피주소 받아오기
//            //보내는사람 이름 | 아이피주소
//            System.out.println("☆★☆★☆  와랏 와랏 와랏   ☆★☆★☆★");
//            fileSocket = ss.accept();      //수락대기중
//            System.out.println("소켓 "+fileSocket+" 에 연결됨");
//           
//            is = fileSocket.getInputStream();   //입력스트림을 가져온다.
//            int i=0;
//            while( (i=is.read() )!=-1){
//                out.write((char)i);
//                out.flush();
//            }
//           
//            ch.ta.append("\t파일저장위치: "+directory+file+"\n");
//   
//            ss.close();           //서버 소캣을 닫는다.
//            fileSocket.close();   // 파일 소캣을 닫는다.
//            ss = null;
//            fileSocket = null;
//           
//        }catch(Exception e){
//            System.out.println(e+"메세지 송신에러");
//        }
//    }
   
//    public void sendMessage(String server_message){//서버로 보내줌
//        try{  // name+"|"+roomname+"|"+authority+"|"+to;
//          
//            writer.write(section+server_message+"\n");
//            writer.flush();
//            section = "100"; //일반메세지
//        }catch(Exception e){
//        System.out.println("sendMessage 에러  :"+e);
//        }
//    }
}
