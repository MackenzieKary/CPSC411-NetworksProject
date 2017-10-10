import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;


public class LoginWindow {

	private JFrame frmCalenderLogin;
	private JPasswordField loginPassword;
	private JTextField textField;
	private JButton btnLogin;
	private JButton btnNewUser;

	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			this.frmCalenderLogin.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCalenderLogin = new JFrame();
		frmCalenderLogin.setResizable(false);
		frmCalenderLogin.setTitle("Calender Login");
		frmCalenderLogin.setBounds(100, 100, 330, 411);
		frmCalenderLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCalenderLogin.getContentPane().setLayout(null);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(97, 328, 143, 29);
		frmCalenderLogin.getContentPane().add(btnLogin);
		
		JPanel teamLogo = new JPanel();
		//teamLogo.setBackground(new Color(238, 238, 238));
		ImageIcon image = new ImageIcon(getClass().getResource("Team Image.png"));
		teamLogo.add(new JLabel(image), BorderLayout.NORTH);
		
		teamLogo.setBounds(23, 23, 285, 142);
		frmCalenderLogin.getContentPane().add(teamLogo);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(23, 185, 74, 50);
		frmCalenderLogin.getContentPane().add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(23, 255, 74, 50);
		frmCalenderLogin.getContentPane().add(passwordLabel);
		
		loginPassword = new JPasswordField();
		loginPassword.setBounds(97, 266, 211, 28);
		frmCalenderLogin.getContentPane().add(loginPassword);
		
		textField = new JTextField();
		textField.setBounds(97, 196, 211, 28);
		frmCalenderLogin.getContentPane().add(textField);
		textField.setColumns(10);
		
		btnNewUser = new JButton("New User");
		btnNewUser.setBounds(126, 354, 85, 29);
		frmCalenderLogin.getContentPane().add(btnNewUser);
	}
	
	public void addWindowListener(ActionListener l) {
		btnLogin.addActionListener(l);
	}
	public void addLoginWindowCloseListener(WindowListener closeLoginWindowEvent) {
		frmCalenderLogin.addWindowListener(closeLoginWindowEvent);
	}
	public void requestFocus(){
		frmCalenderLogin.requestFocus();
	}
	public void closeLoginWindow(){
		frmCalenderLogin.dispose();
	}
	public String getTextField(){
		//System.out.println(textField.getText().toString());
		if (textField.getText().equals("")){
			UIManager UI=new UIManager();
			 UI.put("OptionPane.background", Color.CYAN);
			 UI.put("Panel.background", Color.CYAN);
			JOptionPane.showMessageDialog(frmCalenderLogin,
				    "Invalid Login, Please Enter Again",
				    "Login Error",
				    JOptionPane.ERROR_MESSAGE);
			
			 
			 //UI.put("Panel.background",new ColorUIResource(255,0,0));
		}else{
			return textField.getText().toString();
		}
		return "";
	}
}
