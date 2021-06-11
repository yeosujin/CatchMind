package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class GameRoom extends Application {
	
	Socket socket = WaitingRoomConnect.c_socket; //�޼��� ���� ����
	TextArea textarea; //ä��ġ�� �޼��� �ߴ� â (input�� �۾� �ļ� ���� -> textarea)
	GraphicsContext gc; 
	Canvas canvas; //gc, canvas �� �� ĵ���� �۾� �� �ʿ��� ����
	Vector<String> UserNameSpace = new Vector<String>(); //���� ���� ���� �̸� �����ϴ� �迭 
	TextArea UserArea = new TextArea(); //���� ���� ���� �����ִ� â
	String Username = ChooseType.Nickname; //���� ���� �̸� 
	TextField input = new TextField();
	int currentColorNum = 0; //default
	int currentRound = 1;
	String currentWord;
	boolean isExaminer;
	Color[] colorPalette = {
			Color.rgb(0, 0, 0),
			Color.rgb(255, 0, 0),
			Color.rgb(0, 0, 255),
			Color.rgb(54, 82, 35),
			Color.rgb(255, 192, 0)
	};
	Label wordInfo = new Label();
	Label roundInfo = new Label("Round " + Integer.toString(currentRound));
	
	public void startClient() {
		Thread thread = new Thread() {
			public void run() {
				try {
					//socket = new Socket(IP, port); //���� ����
					sendEnter();
					receive(); //receive�Լ� ���� -> ������ �޼��� ���� �غ�
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
	
	public synchronized void sendEnter() {
		send("Enter_\n"); //Enter_Username_0 (0�� roomNumber), ���߿� split���� ���� ����
	}
	
	public void startDeleteUser(String name) {
    	send("Delete_" + name + "\n");
	}
	
	
	public synchronized void deleteUserName(String name) { //���� ���� �� 
		if(Username.equals(name)) {
			stopClient();
		}
		for(int i = 0; i < UserNameSpace.size(); i++) {
			if(name.equals(UserNameSpace.get(i))) {
				UserNameSpace.remove(i);
				textarea.appendText(name + "���� �����ϼ̽��ϴ� \n");
			}
		}
		updateUserNameBoard();
		
		
	} 
	
	public synchronized void addUserName(String name) {
		if(name.equals(Username)) {
			send(Username + "���� �����ϼ̽��ϴ� \n");
		}
		UserNameSpace.add(name);
		updateUserNameBoard();
	}
	
	public void updateUserNameBoard() {
		UserArea.clear();
		for(int i = 0; i < UserNameSpace.size(); i++) {
			UserArea.appendText(UserNameSpace.get(i) + "\n");
		}
	}
	
	public void stopClient(){
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isOverlap(String name) {
		for(int i = 0; i < UserNameSpace.size(); i++) {
			if(UserNameSpace.get(i).equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void receive() {
		while(true) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String msg = in.readLine();
				String[] test = msg.split("_");
				System.out.println("Client Receive: " + msg);
				if(test[0].equals("Draw")) {
					Platform.runLater(()->{
						//System.out.println(test[0] + ", " + test[1] + ", " + test[2] + ", " + test[3]);
						currentColorNum = Integer.parseInt(test[3]);
						gc.setStroke(colorPalette[currentColorNum]);
						gc.beginPath();
						gc.moveTo(Double.parseDouble(test[1]), Double.parseDouble(test[2]));
						gc.lineTo(Double.parseDouble(test[1]), Double.parseDouble(test[2]));
						gc.stroke();
					});
				}
				else if(test[0].equals("Enter")) {
					Platform.runLater(()->{
						if(!isOverlap(test[1])) {
							addUserName(test[1]);
						}
					});
				}
				else if(test[0].equals("Delete")) {
					Platform.runLater(()->{
						deleteUserName(test[1]);
					});
				}
				else if(test[0].equals("DELETEALL")) {
					Platform.runLater(()->{
						gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					});
				}
				else if(test[0].equals("WORD")) {
					currentWord = test[2];
					if(test[1].equals("Examiner")) {
						isExaminer = true;
						Platform.runLater(()->{
							input.setDisable(true);
							wordInfo.setText("���þ� : " + currentWord);
						});
					}
					else {
						isExaminer = false;
						Platform.runLater(()->{
							input.setDisable(false);
							wordInfo.setText("");
						});
					}
				}
				else if(test[0].equals("Correct")) {
					currentWord = test[2];
					currentRound++;
					if(test[1].equals("Examiner")) {
						isExaminer = true;
						Platform.runLater(()->{
							input.setDisable(true);
							wordInfo.setText("���þ� : " + currentWord);
							roundInfo.setText("Round " + Integer.toString(currentRound));
						});
					}
					else {
						isExaminer = false;
						Platform.runLater(()->{
							input.setDisable(false);
							wordInfo.setText("");
							roundInfo.setText("Round " + Integer.toString(currentRound));
						});
					}
				}
				else {
					Platform.runLater(()->{
						textarea.appendText(msg + "\n");
					});
				}
				
			} catch(Exception e) {
				startDeleteUser(Username);
				break;
			}
		}
	}
	
	
	public void send(String msg) {
		Thread thread = new Thread() {
			public void run() {
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					System.out.println("Client Send: " + msg);
					out.write(msg);
					out.flush();
				}catch(Exception e) {
					startDeleteUser(Username);
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void start(Stage primaryStage) {

		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		root.setStyle("-fx-font-family: \"NanumGothic\"");
		root.setStyle("-fx-background-color:#4374D9");
		
		BorderPane ChatArea = new BorderPane();
		
		BorderPane gamePane = new BorderPane();
		
		StackPane stackpane = new StackPane();
		stackpane.setPrefSize(790, 490);
		
		BorderPane GameInfo = new BorderPane();
		GameInfo.setPrefSize(790, 30);
		
		
		GameInfo.setAlignment(roundInfo, Pos.CENTER);
		
		GameInfo.setLeft(roundInfo);
		GameInfo.setCenter(wordInfo);
		
		canvas = new Canvas(790, 490);
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);
		
		stackpane.getChildren().add(canvas);
		stackpane.setStyle("-fx-background-color:white");
		
		gamePane.setTop(GameInfo);
		gamePane.setBottom(stackpane);
		
		root.setOnMousePressed(e->{
			if(isExaminer) {
				gc.beginPath();
				gc.lineTo(e.getSceneX(), e.getSceneY());
				gc.stroke();
				send("Draw_" + e.getSceneX() + "_" + e.getSceneY() + "_" + currentColorNum + "_" + Username + "\n");
			}
		});
		
		root.setOnMouseDragged(e->{
			if(isExaminer) {
				gc.lineTo(e.getSceneX(), e.getSceneY());
				gc.stroke();
				send("Draw_" + e.getSceneX() + "_" + e.getSceneY() + "_" + currentColorNum + "_" + Username +"\n");
			}
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
			currentColorNum = 0;
		});
		
		ColorSelectArea[1].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(255, 0, 0));
			currentColorNum = 1;
		});
		
		ColorSelectArea[2].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(0, 0, 255));
			currentColorNum = 2;
		});
		
		ColorSelectArea[3].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(54, 82, 35));
			currentColorNum = 3;
		});
		
		ColorSelectArea[4].setOnMousePressed(e->{
			gc.setStroke(Color.rgb(255, 192, 0));
			currentColorNum = 4;
		});
		
		HBox drawTools = new HBox();
		drawTools.setSpacing(5);
		drawTools.getChildren().addAll(ColorSelectArea[0], ColorSelectArea[1],
				ColorSelectArea[2], ColorSelectArea[3],ColorSelectArea[4]);
		
		Button DeleteAllDraw = new Button("��� �����");
		DeleteAllDraw.setPrefSize(120, 40);
		DeleteAllDraw.setOnMouseClicked(e->{
			send("DELETEALL\n");
		});
		DrawArea.setPadding(new Insets(5, 0, 5, 5));
		DrawArea.setLeft(drawTools);
		DrawArea.setRight(DeleteAllDraw);
		
		
		textarea = new TextArea();
		textarea.setEditable(false);
		ChatArea.setCenter(textarea);
		
		
		input.setPrefWidth(Double.MAX_VALUE);
		
		input.setOnAction(event -> {
			send("MSG_" + Username + "_" + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		Button sendbutton = new Button("������");
		
		sendbutton.setOnAction(event -> {
			send("MSG_" + Username + "_" + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		
		
		UserArea.setEditable(false);
		UserArea.setPrefSize(190, 200);
		
		Button exitButton = new Button("�����ϱ�");
		exitButton.setOnMouseClicked(e->{
			primaryStage.close();
			startDeleteUser(Username);
			try {
				WaitingRoomConnect.c_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
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
		
		root.setTop(gamePane);
		root.setCenter(DrawArea);
		root.setBottom(ChatandUser);
		
		
		
		Scene scene = new Scene(root, 800, 780);
		primaryStage.setTitle("[���� ��]");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			startDeleteUser(Username);
		});
		startClient();
		input.requestFocus();
		exitButton.requestFocus();

		
		
	}
	
	public static void main(String[] args) {
		//System.out.printf("%s", args);
		//launch(args);		
		Main M = new Main();
		M.launch(null);
	}
}
