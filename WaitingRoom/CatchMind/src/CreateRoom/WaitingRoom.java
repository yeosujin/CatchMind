package CreateRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

	
public class WaitingRoom extends JFrame{
	
	public static JTextField ChatField;
	static JTextArea ChatWindow = new JTextArea();
	public static String message = "채팅시작!";
	public static int isMessageListened =0;
	public static int isMessageTyped =0;
	public static int CurUserCount =0;
	public static int CurUserCountFlag = CurUserCount;
	private PrintWriter out;
	static JLabel P1_Label, P2_Label,P3_Label,P4_Label;
	
	public WaitingRoom() {
			
		JFrame frame = new JFrame();
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setBounds(100, 100, 1600, 1200);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);
		
		P1_Label = new JLabel("1P");
		P1_Label.setBackground(UIManager.getColor("Button.background"));
		P1_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		P1_Label.setBounds(100, 50, 250, 70);
		getContentPane().add(P1_Label);
		P1_Label.setVisible(false);
			
		P2_Label = new JLabel("2P");
		P2_Label.setBackground(UIManager.getColor("Button.background"));
		P2_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		P2_Label.setBounds(500, 50, 250, 70);
		getContentPane().add(P2_Label);
		P2_Label.setVisible(false);
		
		P3_Label = new JLabel("3P");
		P3_Label.setBackground(UIManager.getColor("Button.background"));
		P3_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		P3_Label.setBounds(100, 150, 250, 70);
		getContentPane().add(P3_Label);
		P3_Label.setVisible(false);
		
		P4_Label = new JLabel("3P");
		P4_Label.setBackground(UIManager.getColor("Button.background"));
		P4_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		P4_Label.setBounds(500, 150, 250, 70);
		getContentPane().add(P4_Label);
		P4_Label.setVisible(false);
		
		
		try {
			Socket c_socket = new Socket("121.151.53.117", 9000);
			
			ReceiveThread rec_thread = new ReceiveThread();
			rec_thread.setSocket(c_socket);
			
			out = new PrintWriter(c_socket.getOutputStream(),true);
			
			WaitingRoomManageThread WMThread = new WaitingRoomManageThread();
			Thread thread1 = new Thread(WMThread, "A");			
			thread1.start();
			
			//SendThread send_thread = new SendThread();
			//send_thread.setSocket(c_socket);
			
			//send_thread.start();
			rec_thread.start();
			
			out.println("IDU" + ChooseType.Nickname);
			out.flush();
					
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ChatField = new JTextField();
	
		
		
		
		ChatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.printf("CurUserCount: %d, CurUserCountFlag: %d\n",CurUserCount,CurUserCountFlag );
				/*
				isMessageTyped = 1;
				System.out.println("엔터 입력됨!");				
				JTextField t = (JTextField)e.getSource();
				message = t.getText();				
				//ChatWindow.append(t.getText()+"\n");
				System.out.printf(message + " :Waiting Room, isMessageListened : %d\n", isMessageListened);			
				*/
				String message = ChatField.getText().trim();
				if(!message.equals("")) {
					out.println(message);
					ChatField.setText("");
								
				}				
			}			
		});
		
		//isMessageTyped =0;
		ChatField.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		ChatField.setBackground(UIManager.getColor("Button.background"));
		ChatField.setBounds(80, 1062, 910, 69);
		getContentPane().add(ChatField);
		ChatField.setColumns(10);
		ChatWindow.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		
		JLabel Quit_Button = new JLabel("New label");
		Quit_Button.setBackground(Color.WHITE);
		Quit_Button.setForeground(Color.BLACK);
		Quit_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				out.flush();
				out.close();			
				System.exit(0);
			}
		});
		Quit_Button.setIcon(new ImageIcon("image\\quit_resize.png"));
		Quit_Button.setBounds(1479, 1075, 50, 50);
		getContentPane().add(Quit_Button);
		
				
		ChatWindow.setBackground(UIManager.getColor("Button.background"));
		ChatWindow.setBounds(80, 427, 910, 614);
		getContentPane().add(ChatWindow);
		ChatWindow.setEditable(false);
		

		
	
								
	}		
}

