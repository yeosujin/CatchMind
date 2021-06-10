package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

//MainŬ�������� ����� ClientŬ�����̴�. (Ŭ���̾�Ʈ �迭 ���� �� ���) 
//ClientŬ������ ������ ���� ����� �����Ѵ�.
public class Client {
	Socket socket; //�ؽ�Ʈ �ְ��޴� ����
	String name; //�̿��� �г���
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
		//1. inputstream���� Ŭ���̾�Ʈ�� ������ �����͸� byte���·� �д´�
		//2. for������ Ŭ���̾�Ʈ �������� �޼����� �����Ѵ� (send �Լ� ���)
		Runnable thread = new Runnable() {

			@Override
			public void run() { //run�Լ��� ������ �־�� �Ѵ�.
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
						System.out.println("[�޼��� ���� ����]"
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
						System.out.println("[�޼��� �۽� ����]"
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