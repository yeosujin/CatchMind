package CreateRoom;

import java.awt.Color;

public class WaitingRoomManageThread extends Thread{

	public static int NumToChange;
	
	public void run(){		
		
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String text = String.format("RoomID : %d", WaitingRoom.ThisRoomNumber);
		WaitingRoom.RID_Label.setText(text);
		
		while(true)	{
						
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(NumToChange==1) {
				WaitingRoom.P2_Label.setForeground(Color.LIGHT_GRAY);
				NumToChange =0;
			}
			else if(NumToChange==2) {
				WaitingRoom.P3_Label.setForeground(Color.LIGHT_GRAY);
				NumToChange =0;
			}
			else if(NumToChange==3) {
				WaitingRoom.P4_Label.setForeground(Color.LIGHT_GRAY);
				NumToChange =0;
			}
			else if(NumToChange==-1) {
				WaitingRoom.P2_Label.setForeground(Color.BLACK);
				NumToChange =0;
			}
			else if(NumToChange==-2) {
				WaitingRoom.P3_Label.setForeground(Color.BLACK);
				NumToChange =0;
			}
			else if(NumToChange==-3) {
				WaitingRoom.P4_Label.setForeground(Color.BLACK);
				NumToChange =0;
			}
	
			
			if(WaitingRoom.CurUserCountFlag != WaitingRoom.CurUserCount) {
				if(WaitingRoom.CurUserCount == 1) {
					
					String P1text = String.format("P1  <%s>", WaitingRoom.CurUserNameList.get(0));
					
					WaitingRoom.P1_Label.setText(P1text);					
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(false);
					WaitingRoom.P3_Label.setVisible(false);
					WaitingRoom.P4_Label.setVisible(false);					
				}
				else if(WaitingRoom.CurUserCount == 2) {
					
					String P1text = String.format("P1  <%s>", WaitingRoom.CurUserNameList.get(0));
					WaitingRoom.P1_Label.setText(P1text);						
					String P2text = String.format("P2  <%s>", WaitingRoom.CurUserNameList.get(1));
					WaitingRoom.P2_Label.setText(P2text);

					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(true);
					WaitingRoom.P3_Label.setVisible(false);
					WaitingRoom.P4_Label.setVisible(false);			
				}
				else if(WaitingRoom.CurUserCount == 3) {
					String P1text = String.format("P1  <%s>", WaitingRoom.CurUserNameList.get(0));
					WaitingRoom.P1_Label.setText(P1text);
					
					String P2text = String.format("P2  <%s>", WaitingRoom.CurUserNameList.get(1));
					WaitingRoom.P2_Label.setText(P2text);
					
					String P3text = String.format("P3  <%s>", WaitingRoom.CurUserNameList.get(2));
					WaitingRoom.P3_Label.setText(P3text);
					
					WaitingRoom.P1_Label.setVisible(true);
					WaitingRoom.P2_Label.setVisible(true);
					WaitingRoom.P3_Label.setVisible(true);
					WaitingRoom.P4_Label.setVisible(false);		
				}
				else if(WaitingRoom.CurUserCount == 4) {
					String P1text = String.format("P1  <%s>", WaitingRoom.CurUserNameList.get(0));
					WaitingRoom.P1_Label.setText(P1text);
					
					String P2text = String.format("P2  <%s>", WaitingRoom.CurUserNameList.get(1));
					WaitingRoom.P2_Label.setText(P2text);
					
					String P3text = String.format("P3  <%s>", WaitingRoom.CurUserNameList.get(2));
					WaitingRoom.P3_Label.setText(P3text);
					
					String P4text = String.format("P4  <%s>", WaitingRoom.CurUserNameList.get(3));
					WaitingRoom.P4_Label.setText(P4text);
					
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

