package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveThread extends Thread{

	private Socket m_Socket;
	public static String CurUserNameList;
	public static String SetReadyName;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		super.run();
		
		try {
			BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
			
			String receiveString;
			
			int temp =0;
			
			CurUserNameList = tmpbuf.readLine();
			UserNameListCreate.UserNameListCreate();
			
			receiveString = tmpbuf.readLine();
			temp = Integer.parseInt(receiveString);
			WaitingRoom.ThisRoomNumber = temp;

			temp = tmpbuf.read();								
			WaitingRoom.CurUserCount = temp;
			
			receiveString = tmpbuf.readLine();
			WaitingRoom.ChatWindow.append(receiveString+"\n");
			//WaitingRoom.ChatWindow.setCaretPosition(WaitingRoom.ChatWindow.getDocument().getLength());

			
			while(true)
			{
				SetReadyName = "";
				
				CurUserNameList = tmpbuf.readLine();
				
				String[] split = CurUserNameList.split("&");	
				if(split.length == 2 && split[0].equals("SETREADY")) {
					SetReadyName = split[1];
					
					for(int i=0; i<WaitingRoom.CurUserNameList.size(); i++) {
						if(SetReadyName.equals(WaitingRoom.CurUserNameList.get(i)))
								WaitingRoomManageThread.NumToChange =i;
					}								
					continue;								
				}
				else if(split.length == 2 && split[0].equals("SETREADY_CANCELED")) {
					SetReadyName = split[1];	
					
					for(int i=0; i<WaitingRoom.CurUserNameList.size(); i++) {
						if(SetReadyName.equals(WaitingRoom.CurUserNameList.get(i)))
								WaitingRoomManageThread.NumToChange =-i;
					}
					
					continue;								
				}
				else if(split.length == 2 && split[0].equals("GAME_START")) {
					while(true) {			
						receiveString = tmpbuf.readLine();
						WaitingRoom.ChatWindow.append(receiveString+"\n");				
						if(receiveString.equals("게임시작!")) {
							System.out.println("게임시작!");
							
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							WaitingRoom.isDispose = 1;
							GameRoom.main(null);
							
							
							
						}					
					}
					//게임시작쓰레드 파트						
				}
						
				UserNameListCreate.UserNameListCreate();
				
				receiveString = tmpbuf.readLine();
				temp = Integer.parseInt(receiveString);
				WaitingRoom.ThisRoomNumber = temp;

				temp = tmpbuf.read();								
				WaitingRoom.CurUserCount = temp;
				
				receiveString = tmpbuf.readLine();
				WaitingRoom.ChatWindow.append(receiveString+"\n");
				//WaitingRoom.
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
