package CreateRoom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatListening extends Thread{ // �������� ���� �޼��� �д� Thread
	Socket socket = null;
	//WaitingRoom WR = new WaitingRoom();
	public ChatListening(Socket socket) {
		this.socket = socket;
	}	
	public void run() {
		try {
			// InputStream - Server���� ���� �޼����� Ŭ���̾�Ʈ�� ������
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