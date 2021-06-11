package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	
	public static ExecutorService threadpool; //여러 스레드들을 효과적으로 관리하기 위해 ExecutorService인터페이스를 사용해 스레드 풀 구현
	public static ArrayList<Client> clients = new ArrayList<Client>(4); //클라이언트를 모아둔 배열, 최대 4
	public static ArrayList<Socket> ClientSocket = new ArrayList<Socket>();
	static int playTurn = 0;
	static String currentWord;
	
	ServerSocket serversocket; //채팅 메세지를 주고받기 위한 서버 소켓이다.
	
	
	public void startServer(int Roomid) { 		
		//ClientSocket = ChatServer.ClientSocketList.get(1);
		word GameWord = new word();
		currentWord = GameWord.getRandWord();
		
		Runnable thread = new Runnable() { //ExecutorService는 생성된 Runnable인터페이스의 관리가 가능 

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.printf("Roomid : %d\n", Roomid);
						
				System.out.printf("접속자수 : %d명\n", ChatServer.ClientRoomSocketList.get(Roomid).size());
				for(int i=0; i<ChatServer.ClientRoomSocketList.get(Roomid).size();i++) {
					System.out.printf("[연결중]%d\n", i);
					clients.add(new Client(ChatServer.ClientRoomSocketList.get(Roomid).get(i), 
							ChatServer.NameLists.get(Roomid).get(i), Roomid)); 
					//새 클라이언트 접속 시마다 클라이언트 배열에 소켓정보를 담고 있는 Client객체 추가
					clients.get(playTurn).isExaminer = true;
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
