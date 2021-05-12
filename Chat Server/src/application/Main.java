package application;
	
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;
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
	
	public static ExecutorService threadpool; //���� ��������� ȿ�������� �����ϱ� ���� ���̺귯��
	public static Vector<Client> clients = new Vector<Client>(); 
	
	ServerSocket serversocket;
	ServerSocket objserversocket;
	
	public void startServer(String IP, int port) {
		try {
			serversocket= new ServerSocket();
			objserversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress(IP, port));
			objserversocket.bind(new InetSocketAddress(IP, port + 1));
		}catch(Exception e) {
			e.printStackTrace();
			if(!serversocket.isClosed() && !objserversocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Socket socket = serversocket.accept();
						Socket objsocket = objserversocket.accept();
						clients.add(new Client(socket, objsocket));
						System.out.println("[Ŭ���̾�Ʈ ����] " +
									socket.getRemoteSocketAddress() +
									": " + Thread.currentThread().getName());
					}catch(Exception e) {
						if(!serversocket.isClosed() && !objserversocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		threadpool = Executors.newCachedThreadPool();
		threadpool.submit(thread);
	}
	
	public void stopServer() {
		try {
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				client.objsocket.close();
				iterator.remove();
			}
			if(serversocket != null && !serversocket.isClosed()) {
				serversocket.close();
			}
			if(objserversocket != null && !objserversocket.isClosed()) {
				objserversocket.close();
			}
			if(threadpool != null && !threadpool.isShutdown()) {
				threadpool.shutdown();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws UnknownHostException {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		TextArea textarea = new TextArea();
		textarea.setEditable(false);
		textarea.setFont(new Font("�������", 15));
		root.setCenter(textarea);
		
		Button togglebutton = new Button("�����ϱ�");
		togglebutton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(togglebutton, new Insets(1, 0, 0, 0));
		root.setBottom(togglebutton);
		
		String IP = "127.0.0.1";
		int port = 5678;
		
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
		launch(args);
	}
}
