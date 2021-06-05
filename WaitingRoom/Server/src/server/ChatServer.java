package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ChatServer {

	public static ArrayList<ArrayList<PrintWriter>> m_OutputLists = new ArrayList<ArrayList<PrintWriter>>();
	public static ArrayList<ArrayList<Socket>> ClientSocketList = new ArrayList<ArrayList<Socket>>();
	public static ArrayList<ArrayList<String>> NameLists = new ArrayList<ArrayList<String>>();
	
	public static final int PORT = 8000;
	
	static int CurUserCount = 0;
	static int RoomCount =0;
	static int RoomCountToEx = 0;
	static int IGameType;
	private static int isAble =1;
	private static int isUnable = 999;
	private static int MaxUserCount = 4;
	public static int[] RoomNumber = new int[100];
	public static int[] IsPlayerReady = new int[100];
	public static int[] CurUserCountArr = new int[100];
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
						
		
			try {
				//소켓 생성
				ServerSocket s_socket = new ServerSocket();
				
				//bind
				String hostAddress = InetAddress.getLocalHost().getHostAddress();
				s_socket.bind(new InetSocketAddress(hostAddress, PORT));
				System.out.println("Waiting - " + hostAddress + ":" + PORT);
				
				while(true)
				{
										
					Socket c_socket = s_socket.accept();
					RoomCountToEx = -1;
					
					
					BufferedReader tmpbuffer = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
					
					int IRoomNumber =0;

					IGameType = tmpbuffer.read();			
					System.out.printf("Selected Game Type : %d\n", IGameType);
					
					//새로운 방?
					if(IGameType ==1) {
												
						ArrayList<PrintWriter> m_OutputList = new ArrayList<>();
						ArrayList<Socket> ClientSocketList = new ArrayList<>();
						ArrayList<String> NameList = new ArrayList<>();
						
						
						m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
						ClientSocketList.add(c_socket);
						
										
						RoomCountToEx = RoomCount;
						
						//방의 고유 번호 생성
						int a;
						Random r = new Random();
						a = r.nextInt(10000)+10000;

						for(int i=0; i<RoomCount; i++) {
							if(RoomNumber[i]==a) {
								a = r.nextInt(10000)+10000;
								i=0;
							}
						}
						RoomNumber[RoomCountToEx] = a;
						System.out.printf("RoomNumber : %d\n", RoomNumber[RoomCountToEx]);					
						m_OutputLists.add(RoomCountToEx, m_OutputList);
						NameLists.add(RoomCountToEx, NameList);
						
						
						ClientManagerThread c_thread = new ClientManagerThread();												
						c_thread.setSocket(c_socket);
						//c_thread.ThisRoomID = RoomCount;
						c_thread.start();
						RoomCount++;
													
					} //방 참가
					else if(IGameType ==2) {						
						String temp;
						temp = tmpbuffer.readLine();
						IRoomNumber = Integer.parseInt(temp);
				
						for(int i=0; i<RoomCount; i++) {
							if(RoomNumber[i]==IRoomNumber) {
								//System.out.printf("참가하려는 방 인덱스 :%d\n", i);
								RoomCountToEx = i;		
							}						
						}
							
						PrintWriter out = new PrintWriter(c_socket.getOutputStream(), true);
												
						if(RoomCountToEx == -1) {
							out.println("0");	
							out.flush();
						}
						else {
							out.println("1");
							out.flush();
							System.out.printf("RoomCountoTx : %d\n", RoomCountToEx);
							m_OutputLists.get(RoomCountToEx).add(new PrintWriter(c_socket.getOutputStream()));

						
							ClientManagerThread c_thread = new ClientManagerThread();
							c_thread.setSocket(c_socket);	
							c_thread.start();
						}
					}									
			
				}
								
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
