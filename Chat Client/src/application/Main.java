package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	Socket socket;
	Socket objsocket;
	TextArea textarea;
	GraphicsContext gc;
	Canvas canvas;
	Vector<String> UserNameSpace = new Vector<String>();
	
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
	
	public void deleteUserName(String name) {
		for(int i = 0; i < UserNameSpace.size(); i++) {
			if(name.equals(UserNameSpace.get(i))) {
				UserNameSpace.remove(i);
			}
		}
	}
	
	public void addUserName(String name) {
		UserNameSpace.add(name);
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
					System.out.println(msg);
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
		
		Label nicknameInput = new Label("닉네임 ");
		Label ipInput = new Label("IP ");
		Label portInput = new Label("Port ");
		
		nicknameInput.setPrefWidth(80);
		nicknameInput.setAlignment(Pos.CENTER);
		ipInput.setPrefWidth(80);
		ipInput.setAlignment(Pos.CENTER);
		portInput.setPrefWidth(80);
		portInput.setAlignment(Pos.CENTER);
		
		TextField nicknameText = new TextField();
		TextField ipText = new TextField();
		TextField PortText = new TextField();
		
		nicknameText.setPrefWidth(180);
		ipText.setPrefWidth(180);
		PortText.setPrefWidth(180);
		
		ipText.setText("127.0.0.1");
		ipText.setEditable(false);
		PortText.setText("5678");
		PortText.setEditable(false);
		
		Button loginButton = new Button("로그인");
		
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(20));
		grid.add(nicknameInput, 1, 1);
		grid.add(ipInput, 1, 2);
		grid.add(portInput, 1, 3);
		
		grid.add(nicknameText, 2, 1);
		grid.add(ipText, 2, 2);
		grid.add(PortText, 2, 3);
		
		grid.add(loginButton, 2, 4);
		grid.setHalignment(loginButton, HPos.RIGHT);
		grid.setVgap(5);
		
		
		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		root.setStyle("-fx-font-family: \"NanumGothic\"");
		root.setStyle("-fx-background-color:#4374D9");
		
		BorderPane ChatArea = new BorderPane();
		
		StackPane stackpane = new StackPane();
		stackpane.setPrefSize(790, 490);
		
		canvas = new Canvas(790, 490);
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
		
		BorderPane DrawArea = new BorderPane();
		Button ColorSelectArea[] = new Button[5];
		for(int i = 0; i < 5; i++) {
			ColorSelectArea[i] = new Button();
			ColorSelectArea[i].setPrefSize(30, 40);
		}
		ColorSelectArea[0].setStyle("-fx-background-color: #000000");
		ColorSelectArea[1].setStyle("-fx-background-color: #FF0000");
		ColorSelectArea[2].setStyle("-fx-background-color: #0000FF");
		ColorSelectArea[3].setStyle("-fx-background-color: #548235");
		ColorSelectArea[4].setStyle("-fx-background-color: #FFC000");
		
		ColorSelectArea[0].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(0, 0, 0));
		});
		
		ColorSelectArea[1].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(255, 0, 0));
		});
		
		ColorSelectArea[2].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(0, 0, 255));
		});
		
		ColorSelectArea[3].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(54, 82, 35));
		});
		
		ColorSelectArea[4].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(255, 192, 0));
		});
		
		Button eraser = new Button("지우개");
		eraser.setPrefSize(120, 40);
		
		HBox drawTools = new HBox();
		drawTools.setSpacing(5);
		drawTools.getChildren().addAll(ColorSelectArea[0], ColorSelectArea[1],
				ColorSelectArea[2], ColorSelectArea[3],ColorSelectArea[4], eraser);
		
		Button DeleteAllDraw = new Button("모두 지우기");
		DeleteAllDraw.setPrefSize(120, 40);
		DeleteAllDraw.setOnMouseClicked(e->{
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		});
		DrawArea.setPadding(new Insets(5, 0, 5, 5));
		DrawArea.setLeft(drawTools);
		DrawArea.setRight(DeleteAllDraw);
		
		
		textarea = new TextArea();
		textarea.setEditable(false);
		ChatArea.setCenter(textarea);
		
		TextField input = new TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		
		input.setOnAction(event -> {
			send(nicknameText.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		Button sendbutton = new Button("보내기");
		
		sendbutton.setOnAction(event -> {
			send(nicknameText.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		
		
		TextArea UserArea = new TextArea();
		UserArea.setEditable(false);
		UserArea.setPrefSize(190, 200);
		
		Button connectionButton = new Button("종료하기");
		connectionButton.setOnAction(event -> {
			send(nicknameText.getText() + " 님께서 퇴장하셨습니다. \n");
			send("delete_" + nicknameText.getText());
			stopClient();
			UserArea.clear();
			for(int i = 0; i < UserNameSpace.size(); i++) {
				UserArea.appendText(UserNameSpace.get(i));
			}
			
			primaryStage.close();
		});
		
		BorderPane pane = new BorderPane();
		
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendbutton);
		
		ChatArea.setBottom(pane);
		
		ChatArea.setPrefSize(590, 200);
		
		
		BorderPane ChatandUser = new BorderPane();
		ChatandUser.setLeft(ChatArea);
		ChatandUser.setRight(UserArea);
		
		root.setTop(stackpane);
		root.setCenter(DrawArea);
		root.setBottom(ChatandUser);
		
		
		
		Scene scene = new Scene(root, 800, 750);
		Scene loginscene = new Scene(grid, 300, 300);
		primaryStage.setTitle("로그인");
		primaryStage.setScene(loginscene);
		primaryStage.show();
		
		loginButton.setOnAction(event->{
			if(nicknameText != null) {
				primaryStage.close();
				primaryStage.setTitle("[채팅 클라이언트]");
				primaryStage.setScene(scene);
				primaryStage.setOnCloseRequest(e -> stopClient());
				primaryStage.show();
				int port = 5678;
				try {
					port = Integer.parseInt(PortText.getText());
				}catch(Exception e) {
					e.printStackTrace();
				}
				startClient(ipText.getText(), port);
				Platform.runLater(()->{
					textarea.appendText("[채팅방 접속] \n");
				});
				input.requestFocus();
				UserArea.clear();
				for(int i = 0; i < UserNameSpace.size(); i++) {
					UserArea.appendText(UserNameSpace.get(i));
				}
			}
			send(nicknameText.getText() + "님께서 접속하셨습니다. \n");
			
		});
		
		connectionButton.requestFocus();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
