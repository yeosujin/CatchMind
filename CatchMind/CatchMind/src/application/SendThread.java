package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread{

	private Socket m_Socket;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(System.in));
			
			PrintWriter sendWriter = new PrintWriter(m_Socket.getOutputStream());
			
			String sendString;		
			//WaitingRoom.ChatWindow.append("사용할 ID를 입력해주십시오 : ");
			//System.out.println("사용할 ID를 입력해주십시오 : ");
			//ChooseType.UserID = tmpbuf.readLine();
			
			sendWriter.println("IDhighkrs12345" + ChooseType.UserID);
			sendWriter.flush();
			
			while(true)
			{
				//sendString = tmpbuf.readLine();
				
				sendString = WaitingRoom.ChatField.getText().trim();
				
				if(!sendString.equals("")) {
					sendWriter.println(sendString);
					WaitingRoom.ChatField.setText("");
				}
				if(sendString.equals("exit"))
				{
					break;
				}							
				sendWriter.flush();
			}
			
			sendWriter.close();
			tmpbuf.close();
			m_Socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setSocket(Socket _socket)
	{
		m_Socket = _socket;
	}	
}
