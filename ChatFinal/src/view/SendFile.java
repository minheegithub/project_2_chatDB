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
	 JTextField tf;  //���ϰ��
	 JButton findbutton, sendbutton, clearbutton;  //����ã��, ������ ,��� ��ư
	 FileDialog fd;  //���ϴ��̾Ʒα�
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
	        findbutton=new JButton("����ã��");
	        findbutton.setBackground(Color.CYAN);
	        findbutton.addActionListener(this);
	        sendbutton=new JButton("������");
	        sendbutton.setBackground(Color.YELLOW);
	        sendbutton.addActionListener(this);
	        clearbutton=new JButton("���");
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
        //�١ڡ١ڡ١ڡ١ڡ١�        ����ã�� ��ư Ŭ��      �١ڡ١ڡ١ڡ١ڡ١ڡ١� 
    if(title.equals(findbutton)){
        fd = new FileDialog(this,"���������ۡ�",FileDialog.LOAD);  //���ϴ��̾�α׻���
        fd.setVisible(true);
        tf.setText("");
//�١ڡ١ڡ١ڡ١ڡ١ڡ١�         �������ϰ� ���丮�� �����س��´�    �١ڡ١ڡ١ڡ١ڡ١ڡ١�
//        wr.directory = fd.getDirectory();  //���丮��
//        wr.file = fd.getFile();  //���ϸ�
//   
//        tf.setText(wr.directory+wr.file);     

		System.out.println("���丮��+���ϸ�"+fd.getDirectory()+"+"+fd.getFile());
       tf.setText(fd.getDirectory()+fd.getFile());     
       

//�١ڡ١ڡ١ڡ١ڡ١ڡ١�      �������ư Ŭ��      �١ڡ١ڡ١ڡ١ڡ١ڡ١ڡ١�
    }else if(title.equals(sendbutton)){ 
       
        try{
        if(!tf.getText().equals("")){
//            String reUser = wr.ch.l.getSelectedValue()+"";  //���� �޴���
//            wr.section="555";  //���� �����ðڽ��ϱ�? �޼���â
//            wr.sendMessage(reUser+"|"+wr.file); //�޴��� | ���ϸ�
        	outputStream.writeUTF(nickname+"��"+selectName+"���� \n" + "|"+fd.getFile()+"���Ϻ���");
        	
        	this.setVisible(false);
           
        }
        }catch(Exception e){
            System.out.println(e);
        }
    }else {  //��ҹ�ư
        this.setVisible(false);
    }
		
	}
	
//	 //�١ڡ١ڡ١ڡ١ڡ١ڡ١�       ���ϼ��������� �����ϸ� ����     �١ڡ١ڡ١ڡ١ڡ١ڡ١�
//    public void FileSend(String ip){
//        this.ip=ip;
//        System.out.println(" ���� ���� ~~~~~~~");
//        try{
//            fileSocket = new Socket(ip,9797);    //�����ϴ��� ip�� �޴��� ���Ƕ� ���ƾ��Ѵ�.9797���� 
//            System.out.println(ip+"    ������");   //127.0.0.1 ������
//        // ���ϳ��� ����
//        DataInputStream dis = new DataInputStream(new FileInputStream(new File(directory+file)));
//        DataOutputStream dos = new DataOutputStream(fileSocket.getOutputStream());
//        System.out.println(directory+file+"���۵ǳ�");   //���� �ְ�ޱ� Stream
//       
//        int b=0;
//        while((b=dis.read()) != -1){
//            dos.writeByte(b); //��������
//            dos.flush();   //�ڿ�����
//        }
//        fileSocket.close();   //��Ĺ�� �ݴ´�
//        fileSocket = null;
//       
//        }catch(Exception e){
//            System.out.println(e+"��������  ���� ");
//        }
//    }
//   
//   
//    //�١ڡ١ڡ١ڡ١ڡ١ڡ١�        ���ϼ���--> ���Ͽ���     �١ڡ١ڡ١ڡ١ڡ١ڡ١�  
//   
//    public void fileSendOk(){
//        try{
//            ss = new ServerSocket(9797);
//            fd=new FileDialog(this,"SENDFILE",FileDialog.SAVE);   //���� ���� ���̾�α� ����.
//            fd.setVisible(true);
//           
//            directory = fd.getDirectory();   //������ ���丮�� ��� ����ȴ�.
//            file = fd.getFile();             //������ ��´�.
//           
//            f = new File(directory+file);//���ο� ���ϰ�ü����
//            FileOutputStream out = new FileOutputStream(f);
//            System.out.println(directory + file+ "�޴������̸� ");
//            section = "505";
//            sendMessage(SendUser+"|"+socket.getInetAddress().getHostAddress());  //�����ּ� �޾ƿ���
//            //�����»�� �̸� | �������ּ�
//            System.out.println("�١ڡ١ڡ�  �Ͷ� �Ͷ� �Ͷ�   �١ڡ١ڡ١�");
//            fileSocket = ss.accept();      //���������
//            System.out.println("���� "+fileSocket+" �� �����");
//           
//            is = fileSocket.getInputStream();   //�Է½�Ʈ���� �����´�.
//            int i=0;
//            while( (i=is.read() )!=-1){
//                out.write((char)i);
//                out.flush();
//            }
//           
//            ch.ta.append("\t����������ġ: "+directory+file+"\n");
//   
//            ss.close();           //���� ��Ĺ�� �ݴ´�.
//            fileSocket.close();   // ���� ��Ĺ�� �ݴ´�.
//            ss = null;
//            fileSocket = null;
//           
//        }catch(Exception e){
//            System.out.println(e+"�޼��� �۽ſ���");
//        }
//    }
   
//    public void sendMessage(String server_message){//������ ������
//        try{  // name+"|"+roomname+"|"+authority+"|"+to;
//          
//            writer.write(section+server_message+"\n");
//            writer.flush();
//            section = "100"; //�Ϲݸ޼���
//        }catch(Exception e){
//        System.out.println("sendMessage ����  :"+e);
//        }
//    }
}
