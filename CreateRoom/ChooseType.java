package CreateRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChooseType extends JDialog {

	int GameType = -1;
	
	public ChooseType(Window parent) {
		super(parent, "Catch Mind", ModalityType.APPLICATION_MODAL);
		
		 		
		setSize(1000,600);
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setLocationRelativeTo(parent);
		setResizable(false);
		JLabel lb = new JLabel("Enter Text");
		JLabel CreateRoomButton = new JLabel("Make New Room");
		JLabel CreateRoomButtonLang = new JLabel(":���ο� ������ ���� ���� �����մϴ�.");
		JLabel JointRoomButton = new JLabel("Join Game");
		JLabel JointRoomButtonLang = new JLabel(":���� �����Ǿ� �ִ� �濡 �����մϴ�.");
		JLabel JointRoomButtonLang2 = new JLabel(":�����Ϸ��� ���� ID�� �ʿ��մϴ�.");
				
		lb.setFont(new Font("Default_SC", Font.PLAIN,25));
		lb.setBounds(200,200,100,50);
						
		CreateRoomButton.setFont(new Font("Default_SC", Font.PLAIN, 40));
		JointRoomButton.setFont(new Font("Default_SC", Font.PLAIN, 40));
		CreateRoomButtonLang.setFont(new Font("Default_SC", Font.PLAIN, 20));
		JointRoomButtonLang.setFont(new Font("Default_SC", Font.PLAIN, 20));
		JointRoomButtonLang2.setFont(new Font("Default_SC", Font.PLAIN, 20));
		
		CreateRoomButton.setBounds(100, 120, 400, 50);
		JointRoomButton.setBounds(580, 120, 400, 50);
		
		CreateRoomButtonLang.setBounds(100, 170, 400, 50);
		JointRoomButtonLang.setBounds(580, 170, 400, 50);
		JointRoomButtonLang2.setBounds(580, 200, 400, 50);
		
			
		CreateRoomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
			GameType = 1;
			dispose();
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
				
		JointRoomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				GameType = 2;
				dispose();									
			}
			@Override
			public void mousePressed(MouseEvent e) {		
				JointRoomButton.setForeground(Color.GRAY);
				JointRoomButtonLang.setForeground(Color.GRAY);	
				JointRoomButtonLang2.setForeground(Color.GRAY);	
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				JointRoomButton.setForeground(Color.BLACK);
				JointRoomButtonLang.setForeground(Color.BLACK);	
				JointRoomButtonLang2.setForeground(Color.BLACK);	
			}
		});
				
		add(CreateRoomButton);
		add(JointRoomButton);
		add(CreateRoomButtonLang);
		add(JointRoomButtonLang);
		add(JointRoomButtonLang2);
		
	}
	
}