package CreateRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class WaitingRoom extends JFrame{
	
	private JTextField ChatField;
	static JTextArea ChatWindow = new JTextArea();
	public static String message;
	
	public WaitingRoom() {
			
		JFrame frame = new JFrame();
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setBounds(100, 100, 1600, 1200);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);
		
		
		ChatField = new JTextField();
		
		ChatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				JTextField t = (JTextField)e.getSource();
				message = t.getText();
				//ChatWindow.append(t.getText()+"\n");
				System.out.println(message);
			}
		});
		
		ChatField.setFont(new Font("Default_SC", Font.PLAIN, 20));
		ChatField.setBackground(UIManager.getColor("Button.background"));
		ChatField.setBounds(80, 1062, 910, 69);
		getContentPane().add(ChatField);
		ChatField.setColumns(10);
		ChatWindow.setFont(new Font("Default_SC", Font.PLAIN, 20));
		
		
		ChatWindow.setBackground(UIManager.getColor("Button.background"));
		ChatWindow.setBounds(93, 427, 897, 614);
		getContentPane().add(ChatWindow);
		ChatWindow.setEditable(false);
			
		try {
			Socket socket = null;
			socket = new Socket("121.151.53.117", 8000);
			
			ChatWindow.append("�濡 �����ϼ̽��ϴ�\n.");	
			System.out.println("������ ���� ����!");
			
			ChatListening t1 = new ChatListening(socket);
			ChatWriting t2 = new ChatWriting(socket);

			t1.start();
			t2.start();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			
			
							
	}		
}
