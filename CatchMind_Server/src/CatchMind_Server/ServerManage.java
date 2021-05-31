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
		this.socket = socket; //서버가 관리하는 소켓??
		this.bufferedReader = BR; //읽어들임
		this.Place = 0; //방 번호 (현재 있는 위치?)
		this.WriterList = WL; //PrintWriter배열은 플레이어 생성 시마다 계속 새로 생성되는건가?? 
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
				else if("message".equals(token[0])) { //각 토큰들이 대체 뭘 뜻하는지?? 모르겠네
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
			consoleLog(this.name +"님이 게임에서 나가셨습니다."); //??
		}
	}
	
	private void Quit(PrintWriter writer, int num) { //PrintWriter는 플레이어를 뜻하는건가? 
		RemoveWriter(writer, num);
		
		String data = "["+this.name+"]님이 입장하셨습니다.";
		synchronized(WriterList) {
			System.out.println(WriterList[0]);
		}
		Broadcast(data,num); //Broaecast 함수는 무슨 기능을 하는 함수인지?? 
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
		String data="["+name+"]님이 입장하셨습니다.";
		Broadcast(data,num);
		Place=num;
		
		addWriter(writer,num);
		synchronized(WriterList) {
			System.out.println(WriterList[0]);
			
		}
		System.out.println(this.name+"님이 입장하셨습니다."); //이건 PrintWriter로 출력하는 것과는 관련이 없음
	}
	
	private void Draw(String points, int roomID) {
		synchronized(WriterList) { //synchronized = 객체 또는 메소드 동기화 시 사용. WriterList를 하나의 스레드만 접근 가능하도록 만든다.
			for(PrintWriter writer : WriterList[roomID]) { //WriterList에서 writer들에게
				writer.println(points); //points를 전송 (그림을 그린 좌표)
				writer.flush();
			}
		}
	}
	
	private void Erase(int roomID){ //지우기
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
				writer.println(data); //Broadcast는 메세지를 전송하도록 도와주는 함수!
				writer.flush();
			}
		}
	}
	
	private void consoleLog(String log) {
		System.out.println(log);
	}
}


 