package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WaitingRoomConnect {

	static PrintWriter out;
	static Socket c_socket;
	static int isRighttoJoinFlag;
	
	public WaitingRoomConnect(){
		
		int temp;
		
		try {
			c_socket = new Socket();
	
			c_socket.connect(new InetSocketAddress("121.182.158.72", 8000));			
			ReceiveThread rec_thread = new ReceiveThread();
			rec_thread.setSocket(c_socket);
							
			out = new PrintWriter(c_socket.getOutputStream(),true);
			BufferedReader tmpbuf2 = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
			
			out.write(ChooseType.GameType);
					
			//방에 참가할때는 룸넘버 확인
			if(ChooseType.GameType == 2) {
				out.println(ChooseType.IRID);	
				temp = tmpbuf2.read();
			
				if(temp==48) {
					ChooseType CR = new ChooseType(null);
					isRighttoJoinFlag =0;
					System.out.println("방이없음");
					
					Restart RE = new Restart(null);
					RE.setVisible(true);		
					
					CreateRoom.main(null);		
					
				}
				else {
					isRighttoJoinFlag=1;
					System.out.println("방있음");
					
					WaitingRoomManageThread WMThread = new WaitingRoomManageThread();
					Thread thread1 = new Thread(WMThread, "A");			
					thread1.start();
					
					rec_thread.start();
					
					out.flush();
					out.println("IDU" + ChooseType.Nickname);
					//out.flush();
					System.out.printf("ThisRoomnumber : %d\n", WaitingRoom.ThisRoomNumber);	
					new WaitingRoom();	
				}
					
			}
			else { //방만들기면
				
				WaitingRoomManageThread WMThread = new WaitingRoomManageThread();
				Thread thread1 = new Thread(WMThread, "A");			
				thread1.start();
				
				rec_thread.start();
				
				out.flush();
				out.println("IDU" + ChooseType.Nickname);
				System.out.printf("ThisRoomnumber : %d\n", WaitingRoom.ThisRoomNumber);	
				new WaitingRoom();	
				
			}

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
}
