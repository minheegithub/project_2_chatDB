package chatDB;

import java.util.Date;

public class ChatVO {
	String nickname;
	String serverIp;
	String clientip;
	int serverPort;
	String contents;
	Date chatDate;
	
	
	public ChatVO(String nickname, String serverIp, String clientip, int serverPort, String contents, Date chatDate) {
		super();
		this.nickname = nickname;
		this.serverIp = serverIp;
		this.clientip = clientip;
		this.serverPort = serverPort;
		this.contents = contents;
		this.chatDate = chatDate;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getServerIp() {
		return serverIp;
	}


	public void setServerIp(String serverIp) {
		serverIp = serverIp;
	}


	public String getClientip() {
		return clientip;
	}


	public void setClientip(String clientip) {
		this.clientip = clientip;
	}


	public int getServerPort() {
		return serverPort;
	}


	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public Date getChatDate() {
		return chatDate;
	}


	public void setChatDate(Date chatDate) {
		this.chatDate = chatDate;
	}
	
	
	
	
}
