package server;

import java.util.ArrayList;

public class word {
	public static ArrayList<String> wordList = new ArrayList<String>(); 
	
	public word(){
		wordList.add("µþ±â");
		wordList.add("·¹¸ó");
		wordList.add("¿Õ°ü");
		wordList.add("Çªµù");
		wordList.add("Äá³ª¹°");
		wordList.add("Å¬·Î¹ö");
		wordList.add("Åä³¢");
		wordList.add("½ºÆÝÁö¹ä");
		wordList.add("¶×ÀÌ");
		wordList.add("°­¾ÆÁö");
		wordList.add("°í¾çÀÌ");
		wordList.add("º´¾Æ¸®");
		wordList.add("Æë±Ï");
		wordList.add("¾ÖÇÃ(±â¾÷)");
		wordList.add("ÅÂ¾ç");
		wordList.add("±¸¸§");
		wordList.add("¹°¹æ¿ï");
		wordList.add("»õ½Ï");
		wordList.add("Æ«¸³");
		wordList.add("³ª¹«");
		wordList.add("½Ã°è");
		wordList.add("¿¬ÇÊ");
		wordList.add("°¡À§");
		wordList.add("ºÎ¾ýÄ®");
	}
	
	static String getRandWord() {
		int randNum = (int)((Math.random()*10000) % wordList.size());
		return wordList.get(randNum);
	}
	
}
