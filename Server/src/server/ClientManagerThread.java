package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManagerThread extends Thread{

	private Socket m_socket;
	private String m_ID;
	private int ThreadFlag =0;
	private static final int isRoomOwner = ChatServer.IGameType;
	static final int ThisRoomID = ChatServer.RoomCountToEx;
	static final int ThisRoomNumber = ChatServer.RoomNumber[ThisRoomID];
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		super.run();
		try {
			BufferedReader tmpbuffer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
			
			String text;
								
			while(true)
			{		
			//	System.out.printf("CM %s ", ChatServer.NameLists.get(ThisRoomID));
			//	System.out.printf("\n");
				System.out.println("CM������");
				
				try {
					sleep(100);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				text = tmpbuffer.readLine();
				System.out.printf("%s\n", text);
				
				if(text == null)
				{
					ChatServer.NameLists.get(ThisRoomID).remove(m_ID);
					System.out.printf("CM System: "+m_ID + "���� �������ϴ�. Room : %d\n", ThisRoomID );
					ChatServer.CurUserCountArr[ThisRoomID]--;
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ChatServer.NameLists.get(ThisRoomID));
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ThisRoomNumber);
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).write(ChatServer.CurUserCountArr[ThisRoomID]);
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("System: " + m_ID + "���� �������ϴ�.");						
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}
					break;
				}
				
				String[] split = text.split("U");
				if(split.length == 2 && split[0].equals("ID"))
				{
					m_ID = split[1];
					ChatServer.NameLists.get(ThisRoomID).add(m_ID);
					System.out.printf("CM System: " + m_ID + "���� �����Ͽ����ϴ�. Room : %d\n", ThisRoomID);
					ChatServer.CurUserCountArr[ThisRoomID]++;
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{
						
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ChatServer.NameLists.get(ThisRoomID));
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ThisRoomNumber);
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).write(ChatServer.CurUserCountArr[ThisRoomID]);					
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("System: " + m_ID + "���� �����Ͽ����ϴ�.");						
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}
					continue;
					
				}
				if(text.equals("PLAYER_SET_READY")) {					
					ChatServer.IsPlayerReady[ThisRoomID]++;	
					System.out.printf("CM" + m_ID + "�غ�!, %d�� �غ�\n",ChatServer.IsPlayerReady[ThisRoomID] );
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{	
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("SETREADY&"+m_ID);
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}			
					
					while(ChatServer.ThreadFlag[ThisRoomID]==0)	{
						//System.out.println("�ڽ� ������");	
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(ChatServer.ThreadFlag[ThisRoomID]==1) {
							System.out.println("�ڽ� ������ ����");	
							ThreadFlag = 1;
							break;
							
						}
							
					}
					
					
					
				}
				if(text.equals("PLAYER_SET_READY_CANCELED")) {					
					ChatServer.IsPlayerReady[ThisRoomID]--;	
					System.out.printf("CM" + m_ID + "�غ����, %d�� �غ�\n",ChatServer.IsPlayerReady[ThisRoomID] );
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{	
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("SETREADY_CANCELED&"+m_ID);
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}			
					continue;
				}
				
				
				if(text.equals("GAME_START")) {					
					System.out.printf("CM ���ӽ���\n",ChatServer.IsPlayerReady[ThisRoomID]);
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{	
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("GAME_START&������ �����մϴ�.");
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}			
					
					
					for(int j=5; j>0; j--) {
						for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
						{	
							ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("���ӽ��۱��� " + j+ "����...");
							ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();							
						}		
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}							
					}
					
					for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
					{	
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).println("���ӽ���!");
						ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
					}	
					
					ChatServer.ThreadFlag[ThisRoomID] =1;
								
					//Main m = new Main();
					
					if(isRoomOwner == 1)
					{
						Main m = new Main();
						m.startServer(ThisRoomID);
						System.out.println("������ �����");					
						break;
					}
					else
						break;
			
					//break; //���������
				}
				
				
				for(int i = 0; i < ChatServer.m_OutputLists.get(ThisRoomID).size(); ++i)
				{	
					ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ChatServer.NameLists.get(ThisRoomID));
					ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(ThisRoomNumber);
					ChatServer.m_OutputLists.get(ThisRoomID).get(i).write(ChatServer.CurUserCountArr[ThisRoomID]);
					ChatServer.m_OutputLists.get(ThisRoomID).get(i).println(m_ID + "> "+ text);
					ChatServer.m_OutputLists.get(ThisRoomID).get(i).flush();
				}
				
				
				
				if(ThreadFlag == 1)
					break;
							
			}
			
			//ChatServer.m_OutputLists.get(ThisRoomID).remove(new PrintWriter(m_socket.getOutputStream()));
			//m_socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSocket(Socket _socket)
	{
		m_socket = _socket;
	}
}
