package CatchMind_Server;

import java.io.PrintWriter;
import java.util.*;

public class Chat extends Thread{
	ArrayList<PrintWriter>[] WriterList;
	public Chat(ArrayList<PrintWriter>[] WriterList) {
		this.WriterList = WriterList;
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			String str = sc.next(); 
			str = " . " + str; //앞에 .은 왜 붙이는거지? 
			for(int i = 0; i < 4; i++) {
				synchronized (WriterList) { 
					for(PrintWriter writer : WriterList[i]) {
						writer.println(str);
						writer.flush();
					}
				}
			}
		}
	}

}
