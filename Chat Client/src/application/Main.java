package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	Socket socket;
	TextArea textarea;
	GraphicsContext gc;
	Canvas canvas;
	Vector<String> UserNameSpace = new Vector<String>();
	TextArea UserArea = new TextArea();
	String Username;
	boolean drawStart = false;
	
	public void startClient(String IP, int port, String name) {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					//send("Enter_" + name +"_" + "0");
					//send(name + "���� �����ϼ̽��ϴ�. \n");
					receive();
				}catch(Exception e) {
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[���� ���� ����]");
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
		System.out.println(name);
	}
	
	public void updateUserNameBoard() {
		UserArea.clear();
		for(int i = 0; i < UserNameSpace.size(); i++) {
			UserArea.appendText(UserNameSpace.get(i) + "\n");
		}
	}
	
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
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
				String[] test = msg.split("_");
				if(test[0].equals("Draw")) {
					Platform.runLater(()->{
						gc.moveTo(Double.parseDouble(test[1]), Double.parseDouble(test[2]));
						gc.beginPath();
						gc.lineTo(Double.parseDouble(test[1]), Double.parseDouble(test[2]));
						gc.stroke();
					});
				}
				else if(test[0].equals("Enter")) {
					Platform.runLater(()->{
						System.out.println(msg);
						addUserName(test[1]);
						updateUserNameBoard();
					});
				}
				else {
					Platform.runLater(()->{
						textarea.appendText(msg);
					});
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
	
	@Override
	public void start(Stage primaryStage) {
		
		ToggleButton setStart = new ToggleButton("����");
		setStart.setSelected(false);
		
		
		Label nicknameInput = new Label("�г��� ");
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
		
		Button loginButton = new Button("�α���");
		
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
			send("Draw_" + e.getSceneX() + "_" + e.getSceneY() + "\n");
		});
		
		root.setOnMouseDragged(e->{
			gc.lineTo(e.getSceneX(), e.getSceneY());
			gc.stroke();
			send("Draw_" + e.getSceneX() + "_" + e.getSceneY() + "\n");
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
		
		Button eraser = new Button("���찳");
		eraser.setPrefSize(120, 40);
		
		HBox drawTools = new HBox();
		drawTools.setSpacing(5);
		drawTools.getChildren().addAll(ColorSelectArea[0], ColorSelectArea[1],
				ColorSelectArea[2], ColorSelectArea[3],ColorSelectArea[4], eraser);
		
		Button DeleteAllDraw = new Button("��� �����");
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
		
		Button sendbutton = new Button("������");
		
		sendbutton.setOnAction(event -> {
			send(nicknameText.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		setStart.selectedProperty().addListener(e->{
			send(Username + "�Բ��� �����ϼ̽��ϴ�. \n");
		});
		
		
		
		
		
		UserArea.setEditable(false);
		UserArea.setPrefSize(190, 200);
		
		Button exitButton = new Button("�����ϱ�");
		exitButton.setOnAction(event -> {
			send(Username + " �Բ��� �����ϼ̽��ϴ�. \n");
			stopClient();
			UserArea.clear();
			for(int i = 0; i < UserNameSpace.size(); i++) {
				UserArea.appendText(UserNameSpace.get(i));
			}
			
			primaryStage.close();
		});
		
		BorderPane pane = new BorderPane();
		
		pane.setLeft(exitButton);
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
		primaryStage.setTitle("�α���");
		primaryStage.setScene(loginscene);
		primaryStage.show();
		
		
		loginButton.setOnAction(event->{
			if(nicknameText != null) {
				Username = nicknameText.getText();
				primaryStage.setTitle("[ä�� Ŭ���̾�Ʈ]");
				primaryStage.setScene(scene);
				primaryStage.setOnCloseRequest(e -> stopClient());
				int port = 5678;
				try {
					port = Integer.parseInt(PortText.getText());
				}catch(Exception e) {
					e.printStackTrace();
				}
				startClient(ipText.getText(), port, Username);
				Platform.runLater(()->{
					setStart.setSelected(true);
				});
				input.requestFocus();
				
				
				exitButton.requestFocus();
			}
		});
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
