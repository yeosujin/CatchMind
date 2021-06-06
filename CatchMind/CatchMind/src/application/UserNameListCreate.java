package application;

import java.util.ArrayList;
import java.util.Arrays;

public class UserNameListCreate {

	public static void UserNameListCreate() {
		
		String tempString = ReceiveThread.CurUserNameList;
		
		tempString = tempString.replace("[","");
		tempString = tempString.replace("]","");
		
		String[] temparr = tempString.split(", ");
			
		ArrayList<String> TempArrList = new ArrayList<String>(Arrays.asList(temparr));
		WaitingRoom.CurUserNameList = TempArrList;
			
	}
}
