import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JList;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.time.LocalDate;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;


public class ModifyEvent {

	private JFrame addmodifyWindow;
	private JTextField eventName;
	private JButton submitBtn;
	private JComboBox startHourBox;
	private JComboBox startMinutesBox;
	private JLabel endTime;
	private JComboBox endHourBox;
	private JComboBox endMinuteBox;
	
	

	/**
	 * Launch the application.
	 */
	
	public void run() {
		try {
			this.addmodifyWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the application.
	 */
	public ModifyEvent() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		addmodifyWindow = new JFrame();
		addmodifyWindow.setTitle("Event Details");
		addmodifyWindow.setBounds(100, 100, 375, 457);
		addmodifyWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addmodifyWindow.setResizable(false);
		addmodifyWindow.getContentPane().setLayout(null);
		
		
		JLabel eventNameLabel = new JLabel("Event Name: ");
		eventNameLabel.setBounds(21, 6, 89, 50);
		addmodifyWindow.getContentPane().add(eventNameLabel);
		
		eventName = new JTextField();
		eventName.setToolTipText("Enter name for your event");
		eventName.setBounds(122, 17, 224, 28);
		addmodifyWindow.getContentPane().add(eventName);
		eventName.setColumns(10);
		
		JLabel startTime = new JLabel("Start Time: ");
		startTime.setBounds(21, 94, 77, 42);
		addmodifyWindow.getContentPane().add(startTime);
		
		startHourBox = new JComboBox();
		startHourBox.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		startHourBox.setBounds(122, 103, 70, 27);
		addmodifyWindow.getContentPane().add(startHourBox);
		
		
		startMinutesBox = new JComboBox();
		startMinutesBox.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));
		startMinutesBox.setBounds(204, 91, 70, 50);
		addmodifyWindow.getContentPane().add(startMinutesBox);
		
		JLabel lblShareEventWith = new JLabel("Share Event With:");
		lblShareEventWith.setBounds(21, 184, 116, 16);
		addmodifyWindow.getContentPane().add(lblShareEventWith);
		
		JPanel panel = new JPanel();
		panel.setBounds(21, 212, 166, 205);
		addmodifyWindow.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JList listUserToShare = new JList();
		panel.add(listUserToShare, BorderLayout.CENTER);
		
		endTime = new JLabel("End Time:");
		endTime.setBounds(21, 149, 70, 16);
		addmodifyWindow.getContentPane().add(endTime);
		
		endHourBox = new JComboBox();
		endHourBox.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		endHourBox.setBounds(122, 145, 70, 27);
		addmodifyWindow.getContentPane().add(endHourBox);
		
		endMinuteBox = new JComboBox();
		endMinuteBox.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));
		endMinuteBox.setBounds(204, 145, 70, 27);
		addmodifyWindow.getContentPane().add(endMinuteBox);
		
		submitBtn = new JButton("Submit");
		submitBtn.setToolTipText("Commit changes");
		submitBtn.setBounds(256, 373, 100, 36);
		addmodifyWindow.getContentPane().add(submitBtn);

	}
	public void setStartHour(String sH){
		this.startHourBox.setSelectedItem(sH);
	}
	public void setStartMin(String sM){
		this.startMinutesBox.setSelectedItem(sM);
	}
	public void setEndHour(String eH){
		this.endHourBox.setSelectedItem(eH);
	}
	public void setEndMin(String eM){
		this.endMinuteBox.setSelectedItem(eM);
	}
	public void addWindowListener(WindowListener closeEditWindowEvent) {
		addmodifyWindow.addWindowListener(closeEditWindowEvent);
	}
	public void requestFocus(){
		addmodifyWindow.requestFocus();
	}
	public void addSubmitButtonListener(ActionListener l){
		submitBtn.addActionListener(l);
	}
	public void changedTitle(String date){
		addmodifyWindow.setTitle("Event Details " + date );
	}
	public void closeAddModifyEventWindow(){
		addmodifyWindow.dispose();
	}
	public String[] getSelectedInfo(){
		String[] toReturn = new String[]{eventName.getText(), startHourBox.getSelectedItem().toString(),startMinutesBox.getSelectedItem().toString(),
				endHourBox.getSelectedItem().toString(),endMinuteBox.getSelectedItem().toString()};
		return toReturn;
	}
	public void setEventName(String eName){
		this.eventName.setText(eName);
	}
}
