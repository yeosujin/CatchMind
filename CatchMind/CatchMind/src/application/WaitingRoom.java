package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;


	
@SuppressWarnings("serial")
public class WaitingRoom extends JFrame{
	
	public static JTextField ChatField;
	public static JTextArea ChatWindow = new JTextArea();
	
	public static String message = "채팅시작!";
	public static int isMessageListened =0;
	public static int isMessageTyped =0;
	public static int CurUserCount =0;
	public static int CurUserCountFlag = CurUserCount;
	public static int ThisRoomNumber = 0;
	public static String ThisRoomNumberString;
	static int jflag =0;
	static int isDispose = 0;
	static JLabel P1_Label, P2_Label,P3_Label,P4_Label, RID_Label;
	public static JFrame frame;
	private JScrollPane scrollPane;
	
	public static ArrayList<String> CurUserNameList = new ArrayList<>();
	
	public WaitingRoom() {
			
		frame = new JFrame();
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setBounds(100, 100, 1600, 900);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);
		frame.setResizable(true);
		
		
		
		//frame.add(scrollPane);
			
		P1_Label = new JLabel("1P");
		P1_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P1_Label.setBounds(100, 50, 400, 70);
		getContentPane().add(P1_Label);
		P1_Label.setVisible(false);
			
		P2_Label = new JLabel("2P");
		P2_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P2_Label.setBounds(500, 50, 400, 70);
		getContentPane().add(P2_Label);
		P2_Label.setVisible(false);
		
		P3_Label = new JLabel("3P");
		P3_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P3_Label.setBounds(100, 150, 400, 70);
		getContentPane().add(P3_Label);
		P3_Label.setVisible(false);
		
		P4_Label = new JLabel("3P");
		P4_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P4_Label.setBounds(500, 150, 400, 70);
		getContentPane().add(P4_Label);
		P4_Label.setVisible(false);

		
		RID_Label = new JLabel(" ");
		RID_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		RID_Label.setBounds(1200, 50, 250, 70);
		getContentPane().add(RID_Label);
		RID_Label.setVisible(true);
		
		
		//방장
		if(ChooseType.GameType == 1) {
					
			JLabel GameStartButton = new JLabel("Start");
			//게임 스타트 버튼
			GameStartButton.setForeground(Color.BLACK);
			GameStartButton.setBackground(Color.WHITE);
			GameStartButton.setVisible(true);
			GameStartButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					GameStartButton.setForeground(Color.GRAY);	
					
					
					String message = "GAME_START";
					if(!message.equals("")) {
						WaitingRoomConnect.out.println(message);
						ChatField.setText("");									
					}			
					
				}
				@Override
				public void mousePressed(MouseEvent e) {		
					GameStartButton.setForeground(Color.GRAY);					
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					GameStartButton.setForeground(Color.BLACK);
				}
			});
			
			GameStartButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 60));
			GameStartButton.setBounds(1200, 700, 200, 100);
			getContentPane().add(GameStartButton);
		
		}
		
		if(ChooseType.GameType == 2) {
			
			JLabel GameReadyButton = new JLabel("Ready");
			//게임 레디버튼
			GameReadyButton.setForeground(Color.BLACK);
			GameReadyButton.setBackground(Color.WHITE);
			GameReadyButton.setVisible(true);
			GameReadyButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {				
					if(jflag==0) { //READY
						GameReadyButton.setForeground(Color.GRAY);
						
						String message = "PLAYER_SET_READY";
						if(!message.equals("")) {
							WaitingRoomConnect.out.println(message);
							ChatField.setText("");									
						}								
						jflag =1;
					}
					else { //READY 취소
						GameReadyButton.setForeground(Color.BLACK);
						
						String message = "PLAYER_SET_READY_CANCELED";
						if(!message.equals("")) {
							WaitingRoomConnect.out.println(message);
							ChatField.setText("");									
						}								
						jflag =0;
					}
												
				}
				@Override
				public void mousePressed(MouseEvent e) {		
				
						
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					GameReadyButton.setForeground(Color.BLACK);
				}
			});
			
			GameReadyButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 60));
			GameReadyButton.setBounds(1200, 700, 300, 100);
			getContentPane().add(GameReadyButton);
						
		}
			
		ChatField = new JTextField();

		
		
		ChatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.printf("CurUserCount: %d, CurUserCountFlag: %d\n",CurUserCount,CurUserCountFlag );
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
					WaitingRoomConnect.out.println(message);
					ChatField.setText("");
								
				}				
			}			
		});
		
		//isMessageTyped =0;
		ChatField.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		ChatField.setBackground(UIManager.getColor("Button.background"));
		ChatField.setBounds(80, 762, 1000, 69);
		getContentPane().add(ChatField);
		ChatField.setColumns(10);
		ChatWindow.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		
		JLabel Quit_Button = new JLabel("New label");
		Quit_Button.setBackground(Color.WHITE);
		Quit_Button.setForeground(Color.BLACK);
		Quit_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					WaitingRoomConnect.c_socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				WaitingRoomConnect.out.flush();
				WaitingRoomConnect.out.close();			
				System.exit(0);
			}
		});
		Quit_Button.setIcon(new ImageIcon("image\\quit_resize.png"));
		Quit_Button.setBounds(1479, 750, 50, 50);
		getContentPane().add(Quit_Button);
		
		
	
		ChatWindow.setCaretPosition(WaitingRoom.ChatWindow.getDocument().getLength());
		ChatWindow.setBackground(UIManager.getColor("Button.background"));
		ChatWindow.setBounds(80, 327, 1000, 414);
		ChatWindow.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(ChatWindow); 
		scrollPane = new JScrollPane(ChatWindow, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		ChatWindow.setLineWrap(true);
		ChatWindow.setWrapStyleWord(true);
		
		getContentPane().add(ChatWindow);
		this.getContentPane().add(scrollPane);
		
								
	}		
}

