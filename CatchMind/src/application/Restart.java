package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;


public class Restart extends JDialog {

	public Restart(Window parent) {
		super(parent, "Restart", ModalityType.APPLICATION_MODAL);
				
		setSize(750,200);
		setLayout(null);
		getContentPane().setBackground(Color.white);
		setLocationRelativeTo(parent);
		setResizable(false);
		JLabel CreateRoomButton = new JLabel("해당 ID의 방이 존재하지 않습니다.");
						
		CreateRoomButton.setFont(new Font("TT_Skip-E 85W", Font.PLAIN, 30));	
		CreateRoomButton.setBounds(150, 50, 600, 50);
		
		CreateRoomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				CreateRoomButton.setVisible(false);
				dispose();	
			}
			@Override
			public void mousePressed(MouseEvent e) {		
				CreateRoomButton.setForeground(Color.GRAY);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				CreateRoomButton.setForeground(Color.BLACK);
			}		
		});								
		add(CreateRoomButton);		
	}
	
}


