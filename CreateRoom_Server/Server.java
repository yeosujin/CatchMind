import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// 소켓통신용 서버 코드
public class Server extends Thread {
	static ArrayList<Socket> list = new ArrayList<Socket>();
	static Socket socket = null;
	
	public Server(Socket socket) {
		this.socket = socket; // 유저 socket을 할당
		list.add(socket); // 유저를 list에 추가
	}
    	public void run() {
		try {
			System.out.println("서버 : " + socket.getInetAddress() 
            						+ " IP의 클라이언트와 연결되었습니다");
			
			// InputStream - 클라이언트에서 보낸 메세지 읽기
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			// OutputStream - 서버에서 클라이언트로 메세지 보내기
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			// 클라이언트에게 연결되었다는 메세지 보내기
			writer.println("서버에 연결되었습니다! ID를 입력해 주세요!");
			
			String readValue; // Client에서 보낸 값 저장
			String name = null; // 클라이언트 이름 설정용
			boolean identify = false;
			
			while((readValue = reader.readLine()) != null ) {
				if(!identify) { // 연결 후 한번만 노출
					name = readValue; // 이름 할당
					identify = true;
					writer.println(name + "님이 접속하셨습니다.");
					continue;
				}
				
				for(int i = 0; i<list.size(); i++) { 
					out = list.get(i).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(name + " : " + readValue); 
				}
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}    		
    	}	
	
	public static void main(String[] args) {
    		try {
                      int socketPort = 8000; 
                      ServerSocket serverSocket = new ServerSocket(socketPort);
                      System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다");
			
                      while(true) {
                          Socket socketUser = serverSocket.accept(); // 서버에 클라이언트 접속 시
                          // Thread 안에 클라이언트 정보를 담아줌
                          Thread thd = new Server(socketUser);
                          thd.start();
                      }                 
            
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
