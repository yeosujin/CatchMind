package CreateRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveThread extends Thread{

	private Socket m_Socket;
	public static String CurUserNameList;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		super.run();
		
		try {
			BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
			
			String receiveString;
			
			String split;
			int temp =0;
			
			CurUserNameList = tmpbuf.readLine();
			System.out.printf("list : %s\n", CurUserNameList);
			UserNameListCreate.UserNameListCreate();
			
			receiveString = tmpbuf.readLine();
			temp = Integer.parseInt(receiveString);
			WaitingRoom.ThisRoomNumber = temp;
			System.out.printf("WaitingRoom.ThisRoomNumber : %d\n", temp);

			temp = tmpbuf.read();								
			WaitingRoom.CurUserCount = temp;
			System.out.printf("WaitingRoom.CurUserCount : %d\n", temp);
			
			receiveString = tmpbuf.readLine();
			WaitingRoom.ChatWindow.append(receiveString+"\n");
			System.out.printf("text : %s\n", receiveString);
			
		
			//System.out.printf("ThisRoomNumber : %d, CurUserCount : %d\n",WaitingRoom.ThisRoomNumber,WaitingRoom.CurUserCount);
			
				
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
				
				CurUserNameList = tmpbuf.readLine();
				System.out.printf("list : %s\n", CurUserNameList);
				UserNameListCreate.UserNameListCreate();
				
				receiveString = tmpbuf.readLine();
				temp = Integer.parseInt(receiveString);
				WaitingRoom.ThisRoomNumber = temp;
				System.out.printf("WaitingRoom.ThisRoomNumber : %d\n", temp);

				temp = tmpbuf.read();								
				WaitingRoom.CurUserCount = temp;
				System.out.printf("WaitingRoom.CurUserCount : %d\n", temp);
				
				receiveString = tmpbuf.readLine();
				WaitingRoom.ChatWindow.append(receiveString+"\n");
				System.out.printf("text : %s\n", receiveString);
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
