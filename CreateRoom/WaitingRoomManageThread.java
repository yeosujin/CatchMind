package CreateRoom;

public class WaitingRoomManageThread extends Thread{

	public void run(){		
		while(true)	{
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(WaitingRoom.CurUserCountFlag != WaitingRoom.CurUserCount) {
				if(WaitingRoom.CurUserCount == 1) {
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(false);
					WaitingRoom.P3_Label.setVisible(false);
					WaitingRoom.P4_Label.setVisible(false);					
				}
				else if(WaitingRoom.CurUserCount == 2) {
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(true);
					WaitingRoom.P3_Label.setVisible(false);
					WaitingRoom.P4_Label.setVisible(false);			
				}
				else if(WaitingRoom.CurUserCount == 3) {
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(true);
					WaitingRoom.P3_Label.setVisible(true);
					WaitingRoom.P4_Label.setVisible(false);		
				}
				else if(WaitingRoom.CurUserCount == 4) {
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(true);
					WaitingRoom.P3_Label.setVisible(true);
					WaitingRoom.P4_Label.setVisible(true);
				}		
				WaitingRoom.CurUserCountFlag = WaitingRoom.CurUserCount;
			}
			
		}	
	}	
}
	
