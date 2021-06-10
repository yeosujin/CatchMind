package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	
	public static ExecutorService threadpool; //���� ��������� ȿ�������� �����ϱ� ���� ExecutorService�������̽��� ����� ������ Ǯ ����
	public static ArrayList<Client> clients = new ArrayList<Client>(4); //Ŭ���̾�Ʈ�� ��Ƶ� �迭, �ִ� 4
	public static ArrayList<Socket> ClientSocket = new ArrayList<Socket>();
	static int playTurn = 0;
	static String currentWord;
	
	ServerSocket serversocket; //ä�� �޼����� �ְ��ޱ� ���� ���� �����̴�.
	
	
	public void startServer() { 		
		//ClientSocket = ChatServer.ClientSocketList.get(1);
		word GameWord = new word();
		currentWord = GameWord.getRandWord();
		
		Runnable thread = new Runnable() { //ExecutorService�� ������ Runnable�������̽��� ������ ���� 

			@Override
			public void run() {
				// TODO Auto-generated method stub
						
				System.out.printf("�����ڼ� : %d��\n", ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).size());
				for(int i=0; i<ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).size();i++) {
					System.out.printf("[������]%d\n", i);
					clients.add(new Client(ChatServer.ClientRoomSocketList.get(ClientManagerThread.ThisRoomID).get(i), 
							ChatServer.NameLists.get(ClientManagerThread.ThisRoomID).get(i), ClientManagerThread.ThisRoomID)); 
					//�� Ŭ���̾�Ʈ ���� �ø��� Ŭ���̾�Ʈ �迭�� ���������� ��� �ִ� Client��ü �߰�
					clients.get(playTurn).isExaminer = true;
					try {
						Thread.sleep(100);
					   } catch (InterruptedException e) {
						e.printStackTrace();
					   }
					
				}			
			}
		};
		threadpool = Executors.newCachedThreadPool(); //������ Ǯ ����
		threadpool.submit(thread); //Runnable �������̽��� ������ Ŭ������ �־� ������ Ǯ�� ���۽�Ŵ 
	}

}