package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

//Main클래스에서 사용한 Client클래스이다. (클라이언트 배열 만들 때 사용) 
//Client클래스를 파일을 따로 만들어 구현한다.
public class Client {
	Socket socket; //텍스트 주고받는 소켓
	String name; //이용자 닉네임
	PrintWriter outmsg;
	int roomNumber;
	boolean isExaminer;
	public Client(Socket socket, String name, int roomNumber) { 
		this.socket = socket;
		this.name = name;
		this.roomNumber = roomNumber;
		this.isExaminer = false;
		try {
			outmsg = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		receive(); 
	}
	
	public void updateUserInfo() {
		for(Client client : Main.clients) {
			client.send("Enter_" + name + "_\n");
		}
		for(Client client : Main.clients) {
			send("Enter_" + client.name + "_\n");
		}
	}
	public void receive() {
		//1. inputstream으로 클라이언트가 보내는 데이터를 byte형태로 읽는다
		//2. for문으로 클라이언트 전원에게 메세지를 전달한다 (send 함수 사용)
		Runnable thread = new Runnable() {

			@Override
			public void run() { //run함수가 무조건 있어야 한다.
				// TODO Auto-generated method stub
				try {
					while(true) {
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String msg = in.readLine();
						String[] splitbuf = msg.split("_");
						System.out.println("Server receive: " + msg);
						if(splitbuf[0].equals("Enter")) {
							if(name.equals("")) {
								name = splitbuf[1];
							}
							updateUserInfo();
						}
						else if(splitbuf[0].equals("Start")) {
							Main.startClickedUser++;
							Main.clients.get(Main.playTurn).isExaminer = true;
						}
						else if(splitbuf[0].equals("Delete")) {
							deleteClient(splitbuf[1]);
						}
						else if(splitbuf[0].equals("Draw")) {
							for(Client client : Main.clients) {
								if(!splitbuf[4].equals(client.name)) {
									client.send(msg + "\n");
								}
							}
						}
						else {
							for(Client client : Main.clients) {
								client.send(msg + "\n");
							}
						}
					}
				}catch(Exception e) {
					try {
						System.out.println("[메세지 수신 오류]"
								+ socket.getRemoteSocketAddress() +
								": " + Thread.currentThread().getName());
					}catch(Exception Ie) {
						Ie.printStackTrace();
					}
				}
			}

			
		};
		Main.threadpool.submit(thread);
	}
	
	private void deleteClient(String Username) {
		// TODO Auto-generated method stub
		Iterator<Client> iterator = Main.clients.iterator();
		System.out.println("Program enter\n");
		while(iterator.hasNext()) {
			Client client = iterator.next();
			System.out.println("Current iterator point to " + name + ", " + Username);
			if(client.name.equals(Username)) {
				iterator.remove();
				for(Client c : Main.clients) {
					c.send("Delete_" + Username + "\n");
				}
				break;
			}
		}
		System.out.println("Program exit\n");
	}
	
	public void send(String msg) {
		Runnable thread = new Runnable() {

			@Override
			// TODO Auto-generated method stub
			public void run() {
				try {
					
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					System.out.println("Server Send: " + msg);
					out.write(msg);
					out.flush();
				}catch(Exception e) {
					try {
						System.out.println("[메세지 송신 오류]"
								+ socket.getRemoteSocketAddress() +
								": " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
					}catch(Exception Ie) {
						Ie.printStackTrace();
					}
				}
			};
			
		};
		Main.threadpool.submit(thread);
	}
}
