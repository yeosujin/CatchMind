package CatchMind_Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.InputStreamReader;

public class CatchMind {
	ServerSocket serversocket = null;
	ArrayList<PrintWriter> WriterList[] = new ArrayList[10];
	
	public CatchMind() {
		for (int i = 0; i < 4; i++) {
			WriterList[i] = new ArrayList<PrintWriter>();
		}
		new Chat(WriterList).start();
		new ServerInfoSender(WriterList).start();
		access();
	}

	public void access(){
		try {
			serversocket = new ServerSocket();
			String hostAd = InetAddress.getLocalHost().getHostAddress();
			serversocket.bind(new InetSocketAddress(hostAd, Main.Port));
			
			
			while(true) {
				Socket socket = serversocket.accept();
				System.out.println("누군가가 연결을 시도했습니다.");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				String req = bufferedReader.readLine();
				if(req.equals("로그인")) {
					login(socket, bufferedReader);
					System.out.println("누군가가 로그인을 시도하였습니다.");
					
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	public void login(Socket socket, BufferedReader bufferedReader) {
		System.out.println(" ");
		new ServerManage(socket, bufferedReader, WriterList).start();
	
	}
}
