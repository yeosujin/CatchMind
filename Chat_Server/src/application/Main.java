package application;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;


public class Main extends Application {
	
	public static ExecutorService threadpool; //여러 스레드들을 효과적으로 관리하기 위해 ExecutorService인터페이스를 사용해 스레드 풀 구현
	public static ArrayList<Client> clients = new ArrayList<Client>(4); //클라이언트를 모아둔 배열, 최대 4
	static int startClickedUser = 0;
	static int playTurn = 0;
	
	ServerSocket serversocket; //채팅 메세지를 주고받기 위한 서버 소켓이다.
	
	public void startServer(String IP, int port) { 
		try {
			serversocket= new ServerSocket(); //소켓 생성
			serversocket.bind(new InetSocketAddress(IP, port)); //서버 인스턴스에 소켓 정보를 바인드 
		}catch(Exception e) { //오류 발생 시 
			e.printStackTrace();
			if(!serversocket.isClosed()) { //소켓이 열려있다면 닫아줌
				stopServer();
			}
			return;
		}
	
		Runnable thread = new Runnable() { //ExecutorService는 생성된 Runnable인터페이스의 관리가 가능 

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Socket socket = serversocket.accept(); //연결요청이 오면 accept로 연결 수락
						if(clients.size() == 4) {
							
							//sendError(client outputstream with IP and port, "There is no empty user space");
						}
						clients.add(new Client(socket, "", 0)); //새 클라이언트 접속 시마다 클라이언트 배열에 소켓정보를 담고 있는 Client객체 추가
						System.out.println("[클라이언트 접속] " +
									socket.getRemoteSocketAddress() +
									": " + Thread.currentThread().getName()); //클라이언트가 잘 접속했음을 알려줌. (지워도 됨) 
					}catch(Exception e) {
						if(!serversocket.isClosed()) {
							stopServer(); //마찬가지로 오류발생 시 소켓 닫아줌
						}
						break;
					}
				}
			}
		};
		threadpool = Executors.newCachedThreadPool(); //스레드 풀 생성
		threadpool.submit(thread); //Runnable 인터페이스로 구현한 클래스를 넣어 스레드 풀을 동작시킴 
	}


	public void stopServer() { //클라이언트의 소켓과 서버소켓을 모두 닫아주는 함수 (오류, 종료 시 쓰임)
		try {
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			if(serversocket != null && !serversocket.isClosed()) {
				serversocket.close();
			}
			if(threadpool != null && !threadpool.isShutdown()) {
				threadpool.shutdown();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws UnknownHostException { //javafx에서 파일 실행 시 start함수가 가장 먼저 실행
		BorderPane root = new BorderPane(); //BorderPane으로 화면 구성. Top, Center, Bottom으로 나누어 배치  
		root.setPadding(new Insets(5)); //5만큼의 padding을 준다.
		
		TextArea textarea = new TextArea(); //서버의 실행 여부를 알려주는 텍스트 공간
		textarea.setEditable(false);
		textarea.setFont(new Font("나눔고딕", 15));
		root.setCenter(textarea); 
		
		Button togglebutton = new Button("시작하기"); //시작 버튼
		togglebutton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(togglebutton, new Insets(1, 0, 0, 0)); //top에만 margin을 줌
		root.setBottom(togglebutton);
		
		String IP = "127.0.0.1";
		int port = 5678; 
		
		//시작하기 버튼을 누르면 startServer함수 실행. 스레드 풀 생성해서 실행. 
		togglebutton.setOnAction(event -> {
			if(togglebutton.getText().equals("시작하기")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String msg = String.format("[서버 시작]\n",IP, port);
					textarea.appendText(msg);
					togglebutton.setText("종료하기");
				});
			}else {
				stopServer();
				Platform.runLater(()->{
					String msg = String.format("[서버 종료]\n",IP, port);
					textarea.appendText(msg);
					togglebutton.setText("시작하기");
				});
			}
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[채팅 서버]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args); //start함수가 실행됨
	}
}
