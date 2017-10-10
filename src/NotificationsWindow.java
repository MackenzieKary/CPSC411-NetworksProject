import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;

public class NotificationsWindow {

	private JFrame frmNotifications;
	private JTable table;
	private JButton btnAccept;
	private JButton btnDecline;
	private String[][] tableData;
	private String[] columnNames;

	/**
	 * Launch the application.
	 */

	public void run() {
		try {
			this.frmNotifications.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public NotificationsWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNotifications = new JFrame();
		frmNotifications.setResizable(false);
		frmNotifications.setTitle("Notifications");
		frmNotifications.setBounds(100, 100, 450, 300);
		frmNotifications.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNotifications.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 438, 220);
		frmNotifications.getContentPane().add(scrollPane);
		
		tableData = new String[][]{{"EventName", "EventDate", "EventStartTime", "EventEndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"},{"Event3Name", "Event3Date", "Event3StartTime", "Event3EndTime"}};
		columnNames = new String[]{"Name", "Date", "StartTime","EndTime"};
		table = new JTable(tableData,columnNames);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		btnAccept = new JButton("Accept");
		btnAccept.setBounds(278, 238, 117, 29);
		frmNotifications.getContentPane().add(btnAccept);
		
		btnDecline = new JButton("Decline");
		btnDecline.setBounds(53, 238, 117, 29);
		frmNotifications.getContentPane().add(btnDecline);
	}
	public void addWindowListener(WindowListener closeNotificationWindowEvent) {
		frmNotifications.addWindowListener(closeNotificationWindowEvent);
	}
	public void requestFocus(){
		frmNotifications.requestFocus();
	}
	
	public void addAcceptButtonListener(ActionListener l){
		btnAccept.addActionListener(l);
	}
	public void addDeclineButtonListener(ActionListener l){
		btnDecline.addActionListener(l);
	}
	public int getHighlightedRow(){
		return table.getSelectedRow();
	}
	public int confirmDecline(){
		Object[] options = {"No!", "Yes, Please!"};
		int n = JOptionPane.showOptionDialog(frmNotifications,
		"Are you sure you want to decline this event request?",
		"Decline Request",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,     //do not use a custom Icon
		options,  //the titles of buttons
		options[0]); //default button title
		return n;		//This is the result of the dialog box, 0 = yes, 1 = no
	}
	public void closeWindow(){
		frmNotifications.dispose();
	}

}
