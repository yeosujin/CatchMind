package CreateRoom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatListening extends Thread{ // 서버에서 보낸 메세지 읽는 Thread
	Socket socket = null;
	//WaitingRoom WR = new WaitingRoom();
	public ChatListening(Socket socket) {
		this.socket = socket;
	}	
	public void run() {
		try {
			// InputStream - Server에서 보낸 메세지를 클라이언트로 가져옴
			InputStream input = socket.getInputStream(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			while(true) {
				//WR.ChatWindow.append(reader.readLine()+"\n");
				WaitingRoom.ChatWindow.setText(reader.readLine());
				System.out.println(reader.readLine());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
