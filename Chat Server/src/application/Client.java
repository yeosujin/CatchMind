package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//Main클래스에서 사용한 Client클래스이다. (클라이언트 배열 만들 때 사용) 
//Client클래스를 파일을 따로 만들어 구현한다.
public class Client {
	Socket socket; //텍스트 주고받는 소켓
	String name; //이용자 닉네임
	PrintWriter outmsg;
	int roomNumber;
	public Client(Socket socket, String name, int roomNumber) { 
		this.socket = socket;
		this.name = name;
		this.roomNumber = roomNumber;
		try {
			outmsg = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		receive(); 
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
						InputStream in = socket.getInputStream(); 
						byte[] buffer = new byte[512];
						int length = in.read(buffer); 
						while(length == -1) {
							throw new IOException();
						}
						System.out.println("[메세지 수신 성공]" + 
											socket.getRemoteSocketAddress() + 
											": " + Thread.currentThread().getName());
						String msg = new String(buffer, 0, length, "UTF-8");
						
						for(Client client : Main.clients) {
							client.send(msg);
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

	
	public void send(String msg) {
		Runnable thread = new Runnable() {

			@Override
			// TODO Auto-generated method stub
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = msg.getBytes("UTF-8");
					out.write(buffer);
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
