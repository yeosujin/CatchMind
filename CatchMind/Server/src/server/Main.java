package server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	
	public static ExecutorService threadpool; //여러 스레드들을 효과적으로 관리하기 위해 ExecutorService인터페이스를 사용해 스레드 풀 구현
	public static ArrayList<Client> clients = new ArrayList<Client>(4); //클라이언트를 모아둔 배열, 최대 4
	public static ArrayList<Socket> ClientSocket = new ArrayList<Socket>();
	
	static int startClickedUser = 0;
	static int playTurn = 0;
	
	ServerSocket serversocket; //채팅 메세지를 주고받기 위한 서버 소켓이다.
	
	
	public void startServer() { 		
		//ClientSocket = ChatServer.ClientSocketList.get(1);
		
		Runnable thread = new Runnable() { //ExecutorService는 생성된 Runnable인터페이스의 관리가 가능 

			@Override
			public void run() {
				// TODO Auto-generated method stub
						
				System.out.printf("접속자수 : %d명\n", ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).size());
				for(int i=0; i<ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).size();i++) {
					System.out.printf("[연결중]%d\n", i);
					clients.add(new Client(ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).get(i), 
							ChatServer.NameLists.get(ClientManagerThread.ThisRoomID).get(i), ClientManagerThread.ThisRoomID)); 
					//새 클라이언트 접속 시마다 클라이언트 배열에 소켓정보를 담고 있는 Client객체 추가
					
					try {
						Thread.sleep(100);
					   } catch (InterruptedException e) {
						e.printStackTrace();
					   }
					
				}			
			}
		};
		threadpool = Executors.newCachedThreadPool(); //스레드 풀 생성
		threadpool.submit(thread); //Runnable 인터페이스로 구현한 클래스를 넣어 스레드 풀을 동작시킴 
	}

}
