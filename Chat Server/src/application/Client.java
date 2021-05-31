package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//MainŬ�������� ����� ClientŬ�����̴�. (Ŭ���̾�Ʈ �迭 ���� �� ���) 
//ClientŬ������ ������ ���� ����� �����Ѵ�.
public class Client {
	Socket socket; //�ؽ�Ʈ �ְ�޴� ����
	String name; //�̿��� �г���
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
		//1. inputstream���� Ŭ���̾�Ʈ�� ������ �����͸� byte���·� �д´�
		//2. for������ Ŭ���̾�Ʈ �������� �޼����� �����Ѵ� (send �Լ� ���)
		Runnable thread = new Runnable() {
			@Override
			public void run() { //run�Լ��� ������ �־�� �Ѵ�.
				// TODO Auto-generated method stub
				try {
					while(true) {
						InputStream in = socket.getInputStream(); 
						byte[] buffer = new byte[512];
						int length = in.read(buffer); 
						while(length == -1) {
							throw new IOException();
						}
						System.out.println("[�޼��� ���� ����]" + 
											socket.getRemoteSocketAddress() + 
											": " + Thread.currentThread().getName());
						String msg = new String(buffer, 0, length, "UTF-8");
						
						for(Client client : Main.clients) {
							client.send(msg);
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
