import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class CreateUser {

	private JFrame frmNewUser;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateUser window = new CreateUser();
					window.frmNewUser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreateUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNewUser = new JFrame();
		frmNewUser.setTitle("New User");
		frmNewUser.setBounds(100, 100, 214, 300);
		frmNewUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNewUser.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(6, 36, 72, 16);
		frmNewUser.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(6, 92, 72, 16);
		frmNewUser.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(74, 30, 134, 28);
		frmNewUser.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Create New Account!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(25, 212, 165, 29);
		frmNewUser.getContentPane().add(btnNewButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(74, 86, 134, 28);
		frmNewUser.getContentPane().add(passwordField);
	}
}
