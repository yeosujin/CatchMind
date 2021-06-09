package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ChooseType extends JDialog {

	public static int GameType = -1;
	public static String UserID = "Nickname";
	public static String Nickname = "Nickname";
	public static String SRID;
	public static int IRID =0;
	
	JTextField setNickname = new JTextField();
	static JTextField RoomID = new JTextField();
	public ChooseType(Window parent) {
		super(parent, "Catch Mind", ModalityType.APPLICATION_MODAL);
		
		
		setSize(1000,600);
		setLayout(null);
		getContentPane().setBackground(Color.white);
		setLocationRelativeTo(parent);
		setResizable(false);
		JLabel lb = new JLabel("Enter Text");
		JLabel CreateRoomButton = new JLabel("Make New Room");
		JLabel CreateRoomButtonLang = new JLabel(":새로운 게임을 위한 방을 생성합니다.");
		JLabel JoinRoomButton = new JLabel("Join Game");
		JLabel JoinRoomButtonLang = new JLabel(":현재 생성되어 있는 방에 참가합니다.");
		JLabel JoinRoomButtonLang2 = new JLabel(":참가하려는 방의 ID가 필요합니다.");
		
		JLabel setYourNickname = new JLabel("Type Your Nickname");
		JLabel setYourNickname2 = new JLabel("게임에서 사용할 별명을 입력하세요.");
		
		JLabel TypeRoomID = new JLabel("Type Room ID");
		JLabel TypeRoomID2 = new JLabel("참가하려는 방의 ID를 입력하세요");
		
		

		
		setNickname.setBackground(UIManager.getColor("Button.background"));
		setNickname.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		setNickname.setBounds(250,250,500,80);
		getContentPane().add(setNickname);
		setNickname.setColumns(10);
		setNickname.setVisible(false);
		
		RoomID.setBackground(UIManager.getColor("Button.background"));
		RoomID.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		RoomID.setBounds(250,250,500,80);
		getContentPane().add(RoomID);
		RoomID.setColumns(10);
		RoomID.setVisible(false);
		
		
		
		setYourNickname.setVisible(false);
		setYourNickname2.setVisible(false);
		
		TypeRoomID.setVisible(false);
		TypeRoomID2.setVisible(false);
				
		lb.setFont(new Font("TT_Skip-E 85W", Font.PLAIN,25));
		lb.setBounds(200,200,100,50);
						
		CreateRoomButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		JoinRoomButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 40));
		CreateRoomButtonLang.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		JoinRoomButtonLang.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		JoinRoomButtonLang2.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 20));
		setYourNickname.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		setYourNickname2.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 25));
		TypeRoomID.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));
		TypeRoomID2.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 25));
		
		
		CreateRoomButton.setBounds(100, 120, 400, 50);
		JoinRoomButton.setBounds(580, 120, 400, 50);
		
		CreateRoomButtonLang.setBounds(100, 170, 400, 50);
		JoinRoomButtonLang.setBounds(580, 170, 400, 50);
		JoinRoomButtonLang2.setBounds(580, 200, 400, 50);
		
		setYourNickname.setBounds(250,150,500,50);
		setYourNickname2.setBounds(250,200,500,50);
		TypeRoomID.setBounds(250,150,500,50);
		TypeRoomID2.setBounds(250,200,500,50);

		
			
		CreateRoomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				CreateRoomButton.setVisible(false);
				JoinRoomButton.setVisible(false);
				CreateRoomButtonLang.setVisible(false);
				JoinRoomButtonLang.setVisible(false);
				JoinRoomButtonLang2.setVisible(false);
				
				setYourNickname.setVisible(true);
				setYourNickname2.setVisible(true);
				
				setNickname.setVisible(true);
				
				
				setNickname.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						Nickname = setNickname.getText().trim();
						GameType = 1;
										
						dispose();	
					}	
				});			
			}
			@Override
			public void mousePressed(MouseEvent e) {		
				CreateRoomButton.setForeground(Color.GRAY);
				CreateRoomButtonLang.setForeground(Color.GRAY);	
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				CreateRoomButton.setForeground(Color.BLACK);
				CreateRoomButtonLang.setForeground(Color.BLACK);	
			}		
		});
				
		JoinRoomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				CreateRoomButton.setVisible(false);
				JoinRoomButton.setVisible(false);
				CreateRoomButtonLang.setVisible(false);
				JoinRoomButtonLang.setVisible(false);
				JoinRoomButtonLang2.setVisible(false);
				
				setYourNickname.setVisible(true);
				setYourNickname2.setVisible(true);
				
				setNickname.setVisible(true);
								
				setNickname.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						Nickname = setNickname.getText().trim();
						
						setYourNickname.setVisible(false);
						setYourNickname2.setVisible(false);					
						setNickname.setVisible(false);

						RoomID.setVisible(true);
						TypeRoomID2.setVisible(true);					
						TypeRoomID.setVisible(true);
						
						
						RoomID.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								SRID = RoomID.getText().trim();
								
								IRID = Integer.parseInt(SRID);
						
								System.out.printf("IRID: %d, Nickname : %s\n", IRID, Nickname);
								GameType = 2;
								dispose();																							
							}																		
						});
					}	
				});									
			}
			@Override
			public void mousePressed(MouseEvent e) {		
				JoinRoomButton.setForeground(Color.GRAY);
				JoinRoomButtonLang.setForeground(Color.GRAY);	
				JoinRoomButtonLang2.setForeground(Color.GRAY);	
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				JoinRoomButton.setForeground(Color.BLACK);
				JoinRoomButtonLang.setForeground(Color.BLACK);	
				JoinRoomButtonLang2.setForeground(Color.BLACK);	
			}
		});
				
		add(CreateRoomButton);
		add(JoinRoomButton);
		add(CreateRoomButtonLang);
		add(JoinRoomButtonLang);
		add(JoinRoomButtonLang2);
		add(setYourNickname);
		add(setYourNickname2);
		add(TypeRoomID);
		add(TypeRoomID2);
		
	}
	
	
	

}


