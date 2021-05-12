package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Client {
	Socket socket;
	Socket objsocket;
	public Client(Socket socket, Socket objsocket) {
		this.socket = socket;
		this.objsocket = objsocket;
		receive();
		receiveDrawing();
	}
	
	public void receive() {
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
						System.out.println("[메세지 수신 오류] +"
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
	
	public void receiveDrawing() {
		Runnable thread = new Runnable() {
			@Override
			public void run() { //run함수가 무조건 있어야 한다.
				// TODO Auto-generated method stub
				try {
					while(true) {
						ObjectInputStream in = new ObjectInputStream(objsocket.getInputStream());
						point p = (point)in.readObject(); 
						System.out.println("[그림 수신 성공]" + 
								objsocket.getRemoteSocketAddress() + 
								": " + Thread.currentThread().getName());
						for(Client client : Main.clients) {
							client.sendDrawing(p);
						}
					}
				}catch(Exception e) {
					try {
						System.out.println("[그림 송신 오류]"
								+ objsocket.getRemoteSocketAddress() +
								": " + Thread.currentThread().getName());
					}catch(Exception Ie) {
						Ie.printStackTrace();
					}
				}
			}
		};
		Main.threadpool.submit(thread);
	}
	
	protected void sendDrawing(point p) {
		// TODO Auto-generated method stub
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ObjectOutputStream out = new ObjectOutputStream(objsocket.getOutputStream());
					out.writeObject(p);
					out.flush();
				}catch(Exception e) {
					try {
						System.out.println("[그림 송신 오류] "
								+ objsocket.getRemoteSocketAddress() +
								": " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						objsocket.close();
					}catch(Exception Ie) {
						Ie.printStackTrace();
					}
				}
			};
			
		};
		Main.threadpool.submit(thread);
	}

	public void send(String msg) {
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
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
