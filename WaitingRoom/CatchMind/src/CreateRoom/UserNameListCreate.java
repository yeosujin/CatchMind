package CreateRoom;

import java.util.ArrayList;
import java.util.Arrays;

public class UserNameListCreate {

	public static void UserNameListCreate() {
		
		String tempString = ReceiveThread.CurUserNameList;
	//	System.out.printf("String : %s\n", ReceiveThread.CurUserNameList);
	//	System.out.printf("tempString : %s\n", tempString);
		
		tempString = tempString.replace("[","");
		tempString = tempString.replace("]","");
		
		String[] temparr = tempString.split(", ");
		
		
		
		
		
		ArrayList<String> TempArrList = new ArrayList<String>(Arrays.asList(temparr));
		WaitingRoom.CurUserNameList = TempArrList;
		
	//for(int i=0; i<temparr.length; i++)
	//System.out.printf("String Cut : %s\n", temparr[i]);
	
	//}
	
		
	}
}
