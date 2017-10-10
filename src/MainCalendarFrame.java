import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Component;

public class MainCalendarFrame {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnDone;
	private JLabel label;
	private JTextField textField_2;
	private JTextField textField_3;
	private LocalDate shownMonth;
	private JPanel currentCalendar;
	private JPanel panel_right;
	private JLabel monthName;
	private JLabel yearName;
	private JButton addButton;
	private JButton btnListUser;
	private JButton btnUserEvents; 
	private JButton btnNotifications;
	private MouseListener panelListener;
	private boolean addButtonEnabled = false;
	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the application.
	 */
	public MainCalendarFrame(MouseListener listener) {
		this.panelListener = listener;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 626);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		
		panel_right = new JPanel();
		splitPane.setRightComponent(panel_right);
		
		panel_right.setBorder(
	            BorderFactory.createTitledBorder(
	            BorderFactory.createEtchedBorder(
	                    EtchedBorder.RAISED, Color.GRAY
	                    , Color.DARK_GRAY), "Calendar"));
		panel_right.setLayout(null);
		


		JButton btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousMonth();
			}
		});
		btnPrevious.setBounds(43, 43, 117, 29);
		panel_right.add(btnPrevious);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextMonth();
			}
		});
		
		
		shownMonth = LocalDate.now();
		currentCalendar = constructCalendar(shownMonth);
		panel_right.add(currentCalendar);
		
		btnNext.setBounds(583, 43, 117, 29);
		panel_right.add(btnNext);
		
		
		JPanel panel_left = new JPanel();
		splitPane.setLeftComponent(panel_left);
		
		panel_left.setBorder(
	            BorderFactory.createTitledBorder(
	            BorderFactory.createEtchedBorder(
	                    EtchedBorder.RAISED, Color.GRAY
	                    , Color.DARK_GRAY), "Events"));
		
		Dimension buttonSize = new Dimension(120, 30);
		GridBagLayout gbl_panel_left = new GridBagLayout();
		gbl_panel_left.columnWidths = new int[]{116, 0};
		gbl_panel_left.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0};
		gbl_panel_left.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_left.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_left.setLayout(gbl_panel_left);
		
		btnUserEvents = new JButton("Your Events");
		btnUserEvents.setPreferredSize(buttonSize);
		GridBagConstraints gbc_btnUserEvents = new GridBagConstraints();
		gbc_btnUserEvents.insets = new Insets(0, 0, 5, 0);
		gbc_btnUserEvents.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnUserEvents.gridx = 0;
		gbc_btnUserEvents.gridy = 0;
		panel_left.add(btnUserEvents, gbc_btnUserEvents);
		
		addButton = new JButton("Add Event");
		addButton.setEnabled(addButtonEnabled);
		addButton.setPreferredSize(buttonSize);
		GridBagConstraints gbc_addButton = new GridBagConstraints();
		gbc_addButton.insets = new Insets(0, 0, 5, 0);
		gbc_addButton.gridx = 0;
		gbc_addButton.gridy = 1;
		panel_left.add(addButton, gbc_addButton);btnUserEvents.setPreferredSize(buttonSize);
		
		btnListUser = new JButton("List Users");	
		btnListUser.setPreferredSize(buttonSize);
		GridBagConstraints gbc_btnListUser = new GridBagConstraints();
		gbc_btnListUser.insets = new Insets(0, 0, 5, 0);
		gbc_btnListUser.gridx = 0;
		gbc_btnListUser.gridy = 2;
		panel_left.add(btnListUser, gbc_btnListUser);
		
		btnNotifications = new JButton("Notifications");
		btnNotifications.setPreferredSize(buttonSize);
		GridBagConstraints gbc_btnNotifications = new GridBagConstraints();
		gbc_btnNotifications.insets = new Insets(0, 0, 5, 0);
		gbc_btnNotifications.gridx = 0;
		gbc_btnNotifications.gridy = 3;
		panel_left.add(btnNotifications, gbc_btnNotifications);
		
		JButton btnLogOut = new JButton("Log Out");
		GridBagConstraints gbc_btnLogOut = new GridBagConstraints();
		gbc_btnLogOut.insets = new Insets(0, 0, 5, 0);
		
		gbc_btnLogOut.anchor = GridBagConstraints.LAST_LINE_START;
		gbc_btnLogOut.weighty = 1.0;   //request any extra vertical space
		gbc_btnLogOut.gridx = 0;
		gbc_btnLogOut.gridy = 4;
		panel_left.add(btnLogOut, gbc_btnLogOut);

	}
	private JPanel constructCalendar(LocalDate toShow){
		JPanel panelCalendar = new JPanel();
		panelCalendar.setBorder(BorderFactory.createLineBorder(Color.black));
		panelCalendar.setBackground(Color.LIGHT_GRAY);
		panelCalendar.setBounds(30, 123, 696, 450);
		
		panelCalendar.setLayout(new GridLayout(0, 7, 0, 0));
		JLabel label_1 = new JLabel("Sunday");
		label_1.setBackground(new Color(51, 255, 153));
		label_1.setOpaque(true);
		label_1.setForeground(Color.BLACK);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_1);
		JLabel label_3 = new JLabel("Monday");
		label_3.setBackground(new Color(51, 255, 153));
		label_3.setOpaque(true);
		label_3.setForeground(Color.BLACK);
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_3);
		JLabel label_4 = new JLabel("Tuesday");
		label_4.setBackground(new Color(51, 255, 153));
		label_4.setOpaque(true);
		label_4.setForeground(Color.BLACK);
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_4);
		JLabel label_2 = new JLabel("Wednesday");
		label_2.setBackground(new Color(51, 255, 153));
		label_2.setOpaque(true);
		label_2.setForeground(Color.BLACK);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_2);
		JLabel label_5 = new JLabel("Thursday");
		label_5.setBackground(new Color(51, 255, 153));
		label_5.setOpaque(true);
		label_5.setForeground(Color.BLACK);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_5);
		JLabel label_6 = new JLabel("Friday");
		label_6.setBackground(new Color(51, 255, 153));
		label_6.setOpaque(true);
		label_6.setForeground(Color.BLACK);
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_6);
		JLabel label_7 = new JLabel("Saturday");
		label_7.setBackground(new Color(51, 255, 153));
		label_7.setOpaque(true);
		label_7.setForeground(Color.BLACK);
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		panelCalendar.add(label_7);
	
		
		LocalDate firstOfMonth = LocalDate.of(shownMonth.getYear(),shownMonth.getMonth(),1);
		for (int i = 0; i < getDayOffsetOfMonth(firstOfMonth); i++){
			JPanel toAdd = new JPanel();
			toAdd.setVisible(false);
			panelCalendar.add(toAdd);
		}
		for (int i = 1; i <= shownMonth.getMonth().maxLength(); i++){
			DayPanel day = new DayPanel(i, 0);
			day.addMouseListener(panelListener);
			panelCalendar.add(day);
		}
		
		monthName = new JLabel(shownMonth.getMonth().toString().toUpperCase());
		monthName.setHorizontalAlignment(SwingConstants.CENTER);
		monthName.setFont(new Font("Monospaced", Font.PLAIN, 28));
		monthName.setBounds(165, 33, 406, 29);
		panel_right.add(monthName);
		
		yearName = new JLabel(String.valueOf(shownMonth.getYear()));
		yearName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		yearName.setHorizontalAlignment(SwingConstants.CENTER);
		yearName.setBounds(332, 61, 70, 34);
		panel_right.add(yearName);
		
		return panelCalendar;
	}
	private int getDayOffsetOfMonth(LocalDate d) {
		DayOfWeek dayName = d.getDayOfWeek();
		if(dayName.toString().equals("SUNDAY")){
			return 0;
		}
		else if(dayName.toString().equals("MONDAY")){
			return 1;
		}
		else if(dayName.toString().equals("TUESDAY")){
			return 2;
		}
		else if(dayName.toString().equals("WEDNESDAY")){
			return 3;
		}
		else if(dayName.toString().equals("THURSDAY")){
			return 4;
		}
		else if(dayName.toString().equals("FRIDAY")){
			return 5;
		}
		else if(dayName.toString().equals("SATURDAY")){
			return 6;
		}
		return -1;
	}
	
	public void nextMonth(){
		shownMonth = shownMonth.plusMonths(1);
		panel_right.remove(currentCalendar);
		panel_right.remove(monthName);
		panel_right.remove(yearName);
		currentCalendar = constructCalendar(shownMonth);
		panel_right.add(currentCalendar);
		panel_right.revalidate();
		panel_right.repaint();

	}
	
	public void previousMonth(){
		shownMonth = shownMonth.minusMonths(1);
		panel_right.remove(currentCalendar);
		panel_right.remove(monthName);
		panel_right.remove(yearName);
		currentCalendar = constructCalendar(shownMonth);
		panel_right.add(currentCalendar);
		panel_right.revalidate();
		panel_right.repaint();

	}
	public LocalDate getDate(){	
		return shownMonth;
	}
	public void toggleAddButtonOn(){
		addButton.setEnabled(true);
	}
	public void toggleAddButtonOff(){
		addButton.setEnabled(false);
	}
	public void toggleAddButton(){
		addButton.setEnabled(!addButtonEnabled);
	}
	
	public void addAddOrEditListener(ActionListener l){
		System.out.println("Listener added");
		addButton.addActionListener(l);
	}
	
	public void addShowUserListener(ActionListener l){
		System.out.println("Listener added");
		btnListUser.addActionListener(l);
	}
	public void addShowUserEventsListener(ActionListener l){
		System.out.println("Listener added");
		btnUserEvents.addActionListener(l);
	}
	public void addNotificationsListener(ActionListener l){
		System.out.println("Notification added");
		btnNotifications.addActionListener(l);
	}
	public String getMonthName(){
		return shownMonth.getMonth().toString();
	}

}

