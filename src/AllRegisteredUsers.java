import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowListener;

import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;


public class AllRegisteredUsers {

	private JFrame frmViewAllRegistered;

	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			
			this.frmViewAllRegistered.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the application.
	 */
	public AllRegisteredUsers() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmViewAllRegistered = new JFrame();
		frmViewAllRegistered.setResizable(false);
		frmViewAllRegistered.setTitle("View All Registered Users");
		frmViewAllRegistered.setBounds(100, 100, 297, 269);
		frmViewAllRegistered.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmViewAllRegistered.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(null, "Username", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frmViewAllRegistered.getContentPane().add(scrollPane, gbc_scrollPane);
	}

	public void addWindowListener(WindowListener closeViewUserWindowEvent) {
		frmViewAllRegistered.addWindowListener(closeViewUserWindowEvent);
	}
	public void requestFocus(){
		frmViewAllRegistered.requestFocus();
	}
}
