package CatchMind_Server;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class ServerManage extends Thread{
	private String name;
	private Socket socket = null;
	private BufferedReader  bufferedReader = null;
	private PrintWriter Writer = null;
	private int Place;
	private ArrayList<PrintWriter> WriterList[] = null;
	
	
	public ServerManage(Socket socket, BufferedReader BR, ArrayList<PrintWriter> WL[]) {
		this.socket = socket; //������ �����ϴ� ����??
		this.bufferedReader = BR; //�о����
		this.Place = 0; //�� ��ȣ (���� �ִ� ��ġ?)
		this.WriterList = WL; //PrintWriter�迭�� �÷��̾� ���� �ø��� ��� ���� �����Ǵ°ǰ�?? 
	}
	
	public void Run() {
		try {
			Writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			String req = bufferedReader.readLine();
			System.out.println("Your name is "+req);
			this.name = req;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		InChat();
	}
	
	
	private void InChat() {
		try {
			while(true) {
				String req = bufferedReader.readLine();
				if(req == null) {
					//System.out.println("Someone is getting out");
					Quit(Writer, this.Place);
					break;
				}
				String[] token=req.split(":");
				if("join".equals(token[0])) {
					Join(Writer,Integer.parseInt(token[1]));
					if(Integer.parseInt(token[1])==0) {
						System.out.println("MAIN CHATROOM ENTRY");
					
					}
					else
						System.out.println("ROOM "+token[1]+" ENTRY");
					
				}
				else if("message".equals(token[0])) { //�� ��ū���� ��ü �� ���ϴ���?? �𸣰ڳ�
					Message(token[1],Integer.parseInt(token[2]));
				}
				else if("quit".equals(token[0])) {
					Quit(Writer,Integer.parseInt(token[1]));
				}
				else if("draw".equals(token[0])) {
					if("erase".equals(token[1]))
						Erase(Integer.parseInt(token[2]));
					else
						Draw(req,Integer.parseInt(token[4]));
				}
				
			}
		}
		catch(IOException e) {
			consoleLog(this.name +"���� ���ӿ��� �����̽��ϴ�."); //??
		}
	}
	
	private void Quit(PrintWriter writer, int num) { //PrintWriter�� �÷��̾ ���ϴ°ǰ�? 
		RemoveWriter(writer, num);
		
		String data = "["+this.name+"]���� �����ϼ̽��ϴ�.";
		synchronized(WriterList) {
			System.out.println(WriterList[0]);
		}
		Broadcast(data,num); //Broaecast �Լ��� ���� ����� �ϴ� �Լ�����?? 
	}
	
	private void RemoveWriter(PrintWriter writer, int num) {
		synchronized(WriterList) {
			WriterList[num].remove(writer);
		}
	}
	
	private void Message(String data, int num) {
		Broadcast("["+this.name+"]: "+data,num);
	}
	
	private void Join(PrintWriter writer, int num) {
		String data="["+name+"]���� �����ϼ̽��ϴ�.";
		Broadcast(data,num);
		Place=num;
		
		addWriter(writer,num);
		synchronized(WriterList) {
			System.out.println(WriterList[0]);
			
		}
		System.out.println(this.name+"���� �����ϼ̽��ϴ�."); //�̰� PrintWriter�� ����ϴ� �Ͱ��� ������ ����
	}
	
	private void Draw(String points, int roomID) {
		synchronized(WriterList) { //synchronized = ��ü �Ǵ� �޼ҵ� ����ȭ �� ���. WriterList�� �ϳ��� �����常 ���� �����ϵ��� �����.
			for(PrintWriter writer : WriterList[roomID]) { //WriterList���� writer�鿡��
				writer.println(points); //points�� ���� (�׸��� �׸� ��ǥ)
				writer.flush();
			}
		}
	}
	
	private void Erase(int roomID){ //�����
		synchronized(WriterList) {
			for(PrintWriter writer:WriterList[roomID]) {
				writer.println("draw:erase"); 
				writer.flush();
			}
		}
	}
	
	private void addWriter(PrintWriter writer, int num) {
		synchronized(WriterList) {
			WriterList[num].add(writer); 
		}
	}
	
	private void Broadcast(String data, int num) {
		synchronized (WriterList) {
			for(PrintWriter writer:WriterList[num]) {
				writer.println(data); //Broadcast�� �޼����� �����ϵ��� �����ִ� �Լ�!
				writer.flush();
			}
		}
	}
	
	private void consoleLog(String log) {
		System.out.println(log);
	}
}


 