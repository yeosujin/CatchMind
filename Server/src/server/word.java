package server;

import java.util.ArrayList;

public class word {
	public static ArrayList<String> wordList = new ArrayList<String>(); 
	
	public word(){
		wordList.add("����");
		wordList.add("����");
		wordList.add("�հ�");
		wordList.add("Ǫ��");
		wordList.add("�ᳪ��");
		wordList.add("Ŭ�ι�");
		wordList.add("�䳢");
		wordList.add("��������");
		wordList.add("����");
		wordList.add("������");
		wordList.add("�����");
		wordList.add("���Ƹ�");
		wordList.add("���");
		wordList.add("����(���)");
		wordList.add("�¾�");
		wordList.add("����");
		wordList.add("�����");
		wordList.add("����");
		wordList.add("ƫ��");
		wordList.add("����");
		wordList.add("�ð�");
		wordList.add("����");
		wordList.add("����");
		wordList.add("�ξ�Į");
	}
	
	static String getRandWord() {
		int randNum = (int)((Math.random()*10000) % wordList.size());
		return wordList.get(randNum);
	}
	
}
