package application;

import java.awt.Color;

import javax.swing.JFrame;

public class JoinRoom {

	public JoinRoom(JFrame Jframe){
		
		Jframe = new JFrame();
		Jframe.getContentPane().setBackground(Color.WHITE);
		Jframe.setBackground(Color.WHITE);
		Jframe.setBounds(100, 100, 1600, 1200);
		Jframe.setLocationRelativeTo(Jframe);
		Jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Jframe.getContentPane().setLayout(null);
		Jframe.setVisible(true);
		
		
		
	}
	
	
	
}
