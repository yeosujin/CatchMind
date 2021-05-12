package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	Socket socket;
	Socket objsocket;
	TextArea textarea;
	GraphicsContext gc;
	Canvas canvas;
	
	public void startClient(String IP, int port) {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					objsocket = new Socket(IP, port + 1);
					receive();
					receiveDrawing();
				}catch(Exception e) {
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[서버 접속 실패]");
						Platform.exit();
					}
				}
			}
		};
		thread.start();
	}
	
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed() && !objsocket.isClosed()) {
				socket.close();
				objsocket.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int length = in.read(buffer);
				if(length == -1) {
					throw new IOException();
				}
				String msg = new String(buffer, 0, length, "UTF-8");
				Platform.runLater(()->{
					textarea.appendText(msg);
				});
			} catch(Exception e) {
				stopClient();
				break;
			}
		}
	}
	
	public void receiveDrawing() {
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);
		while(true) {
			try {
				ObjectInputStream in = new ObjectInputStream(objsocket.getInputStream());
				try {
					point p = (point)in.readObject(); 
					Platform.runLater(()->{
						gc.beginPath();
						gc.lineTo(p.getX(), p.getY());
						gc.stroke();
					});
				}catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch(Exception e) {
				stopClient();
				break;
			}
		}
	}
	
	public void send(String msg) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = msg.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				}catch(Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	public void sendDrawing(double x, double y) {
		Thread thread = new Thread() {
			public void run() {
				try {
					ObjectOutputStream out =  new ObjectOutputStream(objsocket.getOutputStream());
					out.writeObject(new point(x, y));
					out.flush();
				}catch(Exception e) {
					e.getStackTrace();
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		BorderPane ChatArea = new BorderPane();
		
		StackPane stackpane = new StackPane();
		
		canvas = new Canvas(490, 400);
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);
		
		stackpane.getChildren().add(canvas);
		stackpane.setStyle("-fx-background-color:white");
		
		root.setOnMousePressed(e->{
			gc.beginPath();
			gc.lineTo(e.getSceneX(), e.getSceneY());
			gc.stroke();
			sendDrawing(e.getSceneX(), e.getSceneY());
		});
		
		root.setOnMouseDragged(e->{
			gc.lineTo(e.getSceneX(), e.getSceneY());
			gc.stroke();
			sendDrawing(e.getSceneX(), e.getSceneY());
		});
		
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		
		TextField userName = new TextField();
		userName.setPrefWidth(150);
		userName.setPromptText("닉네임을 입력하세요.");
		HBox.setHgrow(userName, Priority.ALWAYS);
		
		TextField IPText = new TextField("127.0.0.1");
		TextField portText = new TextField("5678");
		portText.setPrefWidth(80);
		
		hbox.getChildren().addAll(userName, IPText, portText);
		
		ChatArea.setTop(hbox);
		
		textarea = new TextArea();
		textarea.setEditable(false);
		ChatArea.setCenter(textarea);
		
		TextField input = new TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		input.setDisable(true);
		
		input.setOnAction(event -> {
			send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		Button sendbutton = new Button("보내기");
		sendbutton.setDisable(true);
		
		sendbutton.setOnAction(event -> {
			send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		Button connectionButton = new Button("접속하기");
		connectionButton.setOnAction(event -> {
			if(connectionButton.getText().equals("접속하기")) {
				int port = 5678;
				try {
					port = Integer.parseInt(portText.getText());
				}catch(Exception e) {
					e.printStackTrace();
				}
				startClient(IPText.getText(), port);
				Platform.runLater(()->{
					textarea.appendText("[채팅방 접속] \n");
				});
				connectionButton.setText("종료하기");
				input.setDisable(false);
				sendbutton.setDisable(false);
				input.requestFocus();
			}else {
				stopClient();
				Platform.runLater(()->{
					textarea.appendText("[채팅방 퇴장] \n");
				});
				connectionButton.setText("접속하기");
				input.setDisable(true);
				sendbutton.setDisable(true);
			}
		});
		
		BorderPane pane = new BorderPane();
		
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendbutton);
		
		//root.setTop(stackpane);
		ChatArea.setBottom(pane);
		
		root.setTop(stackpane);
		root.setBottom(ChatArea);
		
		Scene scene = new Scene(root, 500, 800);
		primaryStage.setTitle("[채팅 클라이언트]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
		
		connectionButton.requestFocus();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
