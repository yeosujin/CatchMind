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
	
	public static ExecutorService threadpool; //���� ��������� ȿ�������� �����ϱ� ���� ExecutorService�������̽��� ����� ������ Ǯ ����
	public static ArrayList<Client> clients = new ArrayList<Client>(4); //Ŭ���̾�Ʈ�� ��Ƶ� �迭, �ִ� 4
	static int startClickedUser = 0;
	static int playTurn = 0;
	
	ServerSocket serversocket; //ä�� �޼����� �ְ�ޱ� ���� ���� �����̴�.
	
	public void startServer(String IP, int port) { 
		try {
			serversocket= new ServerSocket(); //���� ����
			serversocket.bind(new InetSocketAddress(IP, port)); //���� �ν��Ͻ��� ���� ������ ���ε� 
		}catch(Exception e) { //���� �߻� �� 
			e.printStackTrace();
			if(!serversocket.isClosed()) { //������ �����ִٸ� �ݾ���
				stopServer();
			}
			return;
		}
	
		Runnable thread = new Runnable() { //ExecutorService�� ������ Runnable�������̽��� ������ ���� 

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Socket socket = serversocket.accept(); //�����û�� ���� accept�� ���� ����
						if(clients.size() == 4) {
							
							//sendError(client outputstream with IP and port, "There is no empty user space");
						}
						clients.add(new Client(socket, "", 0)); //�� Ŭ���̾�Ʈ ���� �ø��� Ŭ���̾�Ʈ �迭�� ���������� ��� �ִ� Client��ü �߰�
						System.out.println("[Ŭ���̾�Ʈ ����] " +
									socket.getRemoteSocketAddress() +
									": " + Thread.currentThread().getName()); //Ŭ���̾�Ʈ�� �� ���������� �˷���. (������ ��) 
					}catch(Exception e) {
						if(!serversocket.isClosed()) {
							stopServer(); //���������� �����߻� �� ���� �ݾ���
						}
						break;
					}
				}
			}
		};
		threadpool = Executors.newCachedThreadPool(); //������ Ǯ ����
		threadpool.submit(thread); //Runnable �������̽��� ������ Ŭ������ �־� ������ Ǯ�� ���۽�Ŵ 
	}


	public void stopServer() { //Ŭ���̾�Ʈ�� ���ϰ� ���������� ��� �ݾ��ִ� �Լ� (����, ���� �� ����)
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
	public void start(Stage primaryStage) throws UnknownHostException { //javafx���� ���� ���� �� start�Լ��� ���� ���� ����
		BorderPane root = new BorderPane(); //BorderPane���� ȭ�� ����. Top, Center, Bottom���� ������ ��ġ  
		root.setPadding(new Insets(5)); //5��ŭ�� padding�� �ش�.
		
		TextArea textarea = new TextArea(); //������ ���� ���θ� �˷��ִ� �ؽ�Ʈ ����
		textarea.setEditable(false);
		textarea.setFont(new Font("�������", 15));
		root.setCenter(textarea); 
		
		Button togglebutton = new Button("�����ϱ�"); //���� ��ư
		togglebutton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(togglebutton, new Insets(1, 0, 0, 0)); //top���� margin�� ��
		root.setBottom(togglebutton);
		
		String IP = "127.0.0.1";
		int port = 5678; 
		
		//�����ϱ� ��ư�� ������ startServer�Լ� ����. ������ Ǯ �����ؼ� ����. 
		togglebutton.setOnAction(event -> {
			if(togglebutton.getText().equals("�����ϱ�")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String msg = String.format("[���� ����]\n",IP, port);
					textarea.appendText(msg);
					togglebutton.setText("�����ϱ�");
				});
			}else {
				stopServer();
				Platform.runLater(()->{
					String msg = String.format("[���� ����]\n",IP, port);
					textarea.appendText(msg);
					togglebutton.setText("�����ϱ�");
				});
			}
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ä�� ����]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args); //start�Լ��� �����
	}
}
