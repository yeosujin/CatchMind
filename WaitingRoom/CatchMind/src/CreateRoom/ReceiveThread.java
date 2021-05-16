package CreateRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveThread extends Thread{

	private Socket m_Socket;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
			
			String receiveString;
			String split;

			WaitingRoom.CurUserCount = tmpbuf.read();
			receiveString = tmpbuf.readLine();
			WaitingRoom.ChatWindow.append(WaitingRoom.CurUserCount+receiveString+"처음받은데이터\n");
				
			while(true)
			{
				
				/*split = receiveString.split(">");
				split = "INFO";
				if(receiveString == "INFO")
				{
					WaitingRoom.ChatWindow.append(receiveString+"정보전달합니다\n");
					WaitingRoom.ChatWindow.append(receiveString+"\n");				
					continue;
				}
			*/
				
				//receiveString = tmpbuf.readLine();
				
				WaitingRoom.CurUserCount = tmpbuf.read();
				receiveString = tmpbuf.readLine();		
				System.out.println(receiveString);
				WaitingRoom.ChatWindow.append(receiveString+"\n");
				//WaitingRoom.ChatWindow.append(WaitingRoom.CurUserCount+"현재 접속자수\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public void setSocket(Socket _socket)
	{
		m_Socket = _socket;
	}

}
