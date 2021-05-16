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
		this.socket = socket;
		this.bufferedReader = BR;
		this.Place = 0;
		this.WriterList = WL;
	}
	
	public void Run() {
		try {
			Writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			String req = bufferedReader.readLine();
			
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
					System.out.println("No requst");
					Quit(Writer, this.Place);
					break;
				}
			}
		}
	}
	
	private void Quit(PrintWriter writer, int num) {
		RemoveWriter(writer, num);
		
	}
	
	private void RemoveWriter(PrintWriter writer, int num) {
		
	}
	
	private void Message(String data, int num) {
		
	}
	
	private void  Join(PrintWriter writer, int num) {
		
	}
	
	private void Draw(String points, int roomID) {
		
	}
	
}


 