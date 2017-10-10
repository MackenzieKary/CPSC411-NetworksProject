import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;

import javax.swing.SwingConstants;

public class DayPanel extends JPanel {
	private JLabel lblDay;
	private JLabel lblNumEvents;
	private int day;
	private boolean hasEvent = false;
	//private LocalDate dateClickedOn;
	
	/**
	 * Create the panel.
	 */
	public DayPanel() {
		setMaximumSize(new Dimension(95, 55));
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setLayout(null);
		
		JLabel lblDay = new JLabel("Day");
		lblDay.setFont(new Font("Lucida Console", Font.PLAIN, 13));
		lblDay.setForeground(Color.BLACK);
		lblDay.setHorizontalAlignment(SwingConstants.CENTER);
		lblDay.setBounds(6, 6, 61, 16);
		add(lblDay);
		
		JLabel lblNumEvents = new JLabel("Num Events");
		lblNumEvents.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		lblNumEvents.setBounds(12, 33, 89, 16);
		add(lblNumEvents);

	}
	public DayPanel(int day, int numEvents){
		this.day = day;
		this.setLayout(null);
		this.lblDay = new JLabel(String.valueOf(day));
		lblDay.setForeground(Color.BLACK);
		lblDay.setBounds(6, 6, 24, 16);
		lblDay.setHorizontalAlignment(SwingConstants.CENTER);
		lblDay.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0))); 
		this.add(lblDay);
		this.lblNumEvents = new JLabel("Number of Events: " + String.valueOf(numEvents));
		lblNumEvents.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		lblNumEvents.setBounds(12, 33, 79, 16);
		this.add(lblNumEvents);
		this.setBorder(new LineBorder(Color.BLACK));
		
	}
	
	public void addClickedOnListener(MouseListener l){
		this.addMouseListener(l);
	}
	public int getDay(){
		return day;
	}
}
