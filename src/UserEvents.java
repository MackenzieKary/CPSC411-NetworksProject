import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowListener;
import java.util.Arrays;

import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class UserEvents {

	private JFrame userEvents;
	private JTable table;
	private JButton btnEdit;
	private JButton btnDelete;
	private String[][] tableData;
	private String[] columnNames;
	/**
	 * Launch the application.
	 */
	
	public void run() {
		try {
			this.userEvents.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the application.
	 */
	public UserEvents() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		userEvents = new JFrame();
		userEvents.setResizable(false);
		userEvents.setTitle("Your Events");
		userEvents.setBounds(100, 100, 482, 286);
		userEvents.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		userEvents.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 470, 190);
		userEvents.getContentPane().add(scrollPane);
		//String[][] tableData = new String[0][0];
		tableData = new String[][]{{"Event One", "Date1", "EventStartTime", "EventEndTime"},{"Event Two", "Date2", "Event3StartTime", "Event3EndTime"},{"Event Three", "Date3", "Event3StartTime", "Event3EndTime"}};
		columnNames = new String[]{"Event Name", "Date", "StartTime","EndTime"};
		table = new JTable(tableData,columnNames);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(27, 208, 117, 29);
		userEvents.getContentPane().add(btnDelete);
		
		btnEdit = new JButton("Edit");
		btnEdit.setBounds(340, 208, 117, 29);
		userEvents.getContentPane().add(btnEdit);
	}
	public void addWindowListener(WindowListener closeEventListWindowEvent) {
		userEvents.addWindowListener(closeEventListWindowEvent);
	}
	public void requestFocus(){
		userEvents.requestFocus();
	}
	
	
	public void addEditButtonListener(ActionListener l){
		btnEdit.addActionListener(l);
	}
	public void addDeleteButtonListener(ActionListener l){
		btnDelete.addActionListener(l);
	}
	public int confirmDeletion(){
		Object[] options = {"No!", "Yes, Please!"};
		int n = JOptionPane.showOptionDialog(userEvents,
		"Are you sure you want to delete this event?",
		"Delete Request",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,     //do not use a custom Icon
		options,  //the titles of buttons
		options[0]); //default button title
		return n;		//This is the result of the dialog box, 0 = yes, 1 = no
	}
	
	public int getHighlightedRow(){
		return table.getSelectedRow();
	}
	public void closeWindow(){
		userEvents.dispose();
	}
}
