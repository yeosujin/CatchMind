package CatchMind_Server;

import java.io.PrintWriter;
import java.util.ArrayList;

public class ServerInfoSender extends Thread{
	private ArrayList<PrintWriter> WriterList[] = null;
	ServerInfoSender(ArrayList<PrintWriter> WriterList[]){
		this.WriterList = WriterList;
	}
	public void run() {
		while(true) {
			try {
				sleep(10);
				synchronized(WriterList) {
					String[] number = new String[10];
					for(int i = 0; i < 4; i++) {
						number[i] = Integer.toString(WriterList[i].size());
					}
					for(int i = 0; i< 4;i++) {
						for(PrintWriter a:WriterList[i]) {
							a.println("serverinfo: " + number[0]+":"+number[1]+":"+number[2]+":"+number[3]);
							//각 number가 뜻하는게 대체 무엇인지? 플레이어의 번호? 아이피 주소? 포트 번호?
							a.flush();
						}
					}
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
