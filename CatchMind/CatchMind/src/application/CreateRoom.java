package application;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JPanel;

public class CreateRoom {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateRoom window = new CreateRoom();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreateRoom() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1600, 900);
		frame.setLocationRelativeTo(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
			
		try {
		    //create the font to use. Specify the size!
		    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font\\ja-jp.ttf")).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		
		JLabel GameStartButton = new JLabel("Game Start");
		//게임 스타트 버튼
		GameStartButton.setForeground(Color.WHITE);
		GameStartButton.setBackground(Color.WHITE);
		GameStartButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				ChooseType CT = new ChooseType(frame);
				CT.setVisible(true);				
				//방만들기
				if(ChooseType.GameType ==1) {
					frame.setVisible(false);
					new WaitingRoomConnect();
		
				}				
				//방 참가
				if(ChooseType.GameType ==2) {
					new WaitingRoomConnect();
					if(WaitingRoomConnect.isRighttoJoinFlag==1)
						frame.setVisible(false);
					else if(WaitingRoomConnect.isRighttoJoinFlag!=2) {
						frame.setVisible(false);
							
					}
						
					
				}							
			}
			@Override
			public void mousePressed(MouseEvent e) {		
				GameStartButton.setForeground(Color.GRAY);					
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				GameStartButton.setForeground(Color.WHITE);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				GameStartButton.setForeground(Color.WHITE);
			}
		});
		
		GameStartButton.setFont(new Font("Default_SC", Font.PLAIN, 60));
		GameStartButton.setBounds(620, 383, 373, 138);
		frame.getContentPane().add(GameStartButton);
				
		
		JLabel Quit_Button = new JLabel("New label");
		Quit_Button.setBackground(Color.WHITE);
		Quit_Button.setForeground(Color.BLACK);
		Quit_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		Quit_Button.setIcon(new ImageIcon("image\\quit_resize.png"));
		Quit_Button.setBounds(1400, 700, 50, 50);
		Quit_Button.setVisible(true);
		frame.getContentPane().add(Quit_Button);
		
		JLabel MainImage = new JLabel("main");
		MainImage.setIcon(new ImageIcon("image\\main.png"));
		MainImage.setBounds(0, 0, 1904, 1041);
		frame.getContentPane().add(MainImage);
		
		
		//MainImage.setBounds(0, 0, 1904, 1041);
		//frame.getContentPane().add(MainImage);
				
				
		frame.setResizable(false);
		frame.setTitle("CatchMind");
				
	}
}
