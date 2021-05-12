package CreateRoom;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatWriting extends Thread{ // 서버로 메세지 보내는 Thread
	Socket socket = null;

	Scanner scanner = new Scanner(System.in); //		
	public ChatWriting(Socket socket) {
		this.socket = socket;
	}		
	public void run() {
		try {
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			while(true) {
				writer.println(WaitingRoom.message); // 입력한 메세지 발송				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}