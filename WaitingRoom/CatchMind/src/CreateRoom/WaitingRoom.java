package CreateRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.JPanel;

	
public class WaitingRoom extends JFrame{
	
	public static JTextField ChatField;
	static JTextArea ChatWindow = new JTextArea();
	public static String message = "채팅시작!";
	public static int isMessageListened =0;
	public static int isMessageTyped =0;
	public static int CurUserCount =0;
	public static int CurUserCountFlag = CurUserCount;
	public static int ThisRoomNumber = 0;
	public static String ThisRoomNumberString;
	
	static JLabel P1_Label, P2_Label,P3_Label,P4_Label, RID_Label;
	private JLabel SettingLabel;
	
	public static ArrayList<String> CurUserNameList = new ArrayList<>();
	
	public WaitingRoom() {
			
		JFrame frame = new JFrame();
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setBounds(100, 100, 1600, 1200);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);
		frame.setResizable(false);
			
		P1_Label = new JLabel("1P");
		P1_Label.setBackground(UIManager.getColor("Button.background"));
		P1_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P1_Label.setBounds(100, 50, 400, 70);
		getContentPane().add(P1_Label);
		P1_Label.setVisible(false);
			
		P2_Label = new JLabel("2P");
		P2_Label.setBackground(UIManager.getColor("Button.background"));
		P2_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P2_Label.setBounds(500, 50, 400, 70);
		getContentPane().add(P2_Label);
		P2_Label.setVisible(false);
		
		P3_Label = new JLabel("3P");
		P3_Label.setBackground(UIManager.getColor("Button.background"));
		P3_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P3_Label.setBounds(100, 150, 400, 70);
		getContentPane().add(P3_Label);
		P3_Label.setVisible(false);
		
		P4_Label = new JLabel("3P");
		P4_Label.setBackground(UIManager.getColor("Button.background"));
		P4_Label.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		P4_Label.setBounds(500, 150, 400, 70);
		getContentPane().add(P4_Label);
		P4_Label.setVisible(false);

		
		RID_Label = new JLabel(" ");
		RID_Label.setBackground(UIManager.getColor("Button.background"));
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
			GameStartButton.setBounds(1200, 1000, 200, 100);
			getContentPane().add(GameStartButton);
			
			
			JPanel panel = new JPanel();
			panel.setBounds(1000, 300, 500, 600);
			getContentPane().add(panel);
			panel.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("\uAC8C\uC784\uC2DC\uAC04");
			lblNewLabel.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
			lblNewLabel.setBounds(30, 100, 150, 50);
			panel.add(lblNewLabel);
			
			
			JRadioButton GameTimeSet10 = new JRadioButton("10분");
			JRadioButton GameTimeSet15 = new JRadioButton("15분");
			JRadioButton GameTimeSet20 = new JRadioButton("20분");
			
			GameTimeSet10.setSelected(true);
			
			ButtonGroup GameTimeSetGroup = new ButtonGroup();
			
			GameTimeSetGroup.add(GameTimeSet10);
			GameTimeSetGroup.add(GameTimeSet15);
			GameTimeSetGroup.add(GameTimeSet20);
			
			GameTimeSet10.setBounds(180, 100, 100, 50);
			GameTimeSet10.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
			GameTimeSet15.setBounds(280, 100, 100, 50);
			GameTimeSet15.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
			GameTimeSet20.setBounds(380, 100, 100, 50);
			GameTimeSet20.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
			

			panel.add(GameTimeSet10);
			panel.add(GameTimeSet15);
			panel.add(GameTimeSet20);
		
		}
		
		if(ChooseType.GameType == 2) {
			
			JLabel GameReadyButton = new JLabel("Ready");
			//게임 스타트 버튼
			GameReadyButton.setForeground(Color.BLACK);
			GameReadyButton.setBackground(Color.WHITE);
			GameReadyButton.setVisible(true);
			GameReadyButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					GameReadyButton.setForeground(Color.GRAY);					
				}
				@Override
				public void mousePressed(MouseEvent e) {		
					GameReadyButton.setForeground(Color.GRAY);					
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					GameReadyButton.setForeground(Color.BLACK);
				}
			});
			
			GameReadyButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 60));
			GameReadyButton.setBounds(1300, 900, 300, 100);
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
				System.out.printf("ThisRoomnumber : %d\n", ThisRoomNumber);	
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
				
				WaitingRoomConnect.out.flush();
				WaitingRoomConnect.out.close();			
				System.exit(0);
			}
		});
		Quit_Button.setIcon(new ImageIcon("image\\quit_resize.png"));
		Quit_Button.setBounds(1479, 1075, 50, 50);
		getContentPane().add(Quit_Button);
		
				
		ChatWindow.setBackground(UIManager.getColor("Button.background"));
		ChatWindow.setBounds(80, 427, 910, 614);
		getContentPane().add(ChatWindow);
		
		SettingLabel = new JLabel("Settings");
		SettingLabel.setBounds(1175, 250, 200, 50);
		SettingLabel.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		getContentPane().add(SettingLabel);
		if(ChooseType.GameType == 1)
			SettingLabel.setVisible(true);
		else
			SettingLabel.setVisible(false);

								
	}		
}

