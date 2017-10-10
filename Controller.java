import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import logic.*;
import gui.*;

    /**
     * Class Controller links the GUI and the logic calls the various classes and runs them
     *  asks the user to build a schedule
     */
public class Controller {
    //Instance Variables
	private Schedule mainwindow;
	private GridSchedule grid;
	private LectureList allLectures;
	private LogicSchedule usersSchedule = new LogicSchedule();
	private Lecture[] storedMajor;
	private Lecture[] storedElective;
	private String selectedLecToAdd;
	private BufferedImage image;
	private int dayInt = 0;
	private int hourInt = 0;
	private String selectedMajor = "None";
	private String selectedTimeString = " ";
	private String thetab;
	private boolean isCoursePresent;
	private boolean isBreakPresent;
	private Object noTimeBlockSelected = "Error: No time block selected.";
	private Lecture toDrop;
    
	/**
     * Searches all lecs to refine to those that fit time selected, don't overlap a full time, and by major
     * @param major:Currently selected Major
     * @param timeSlot:Currently selected Timeslot
     * @return
     */
	private String[] getLecturesToAddMajor(String major, String timeSlot){
		 Lecture[] fullyRefined = allLectures.fullMajorRefine(timeSlot, usersSchedule.callFullTimeSlots(),usersSchedule.callBreakSlots(), major, usersSchedule.callLecturesTaking());
         storedMajor = fullyRefined;
         String[] fullyRefinedNames = new String[fullyRefined.length];
         for(int indexLec = 0; indexLec < fullyRefined.length; indexLec++){
              fullyRefinedNames[indexLec] = fullyRefined[indexLec].nameCall();
         }
    	
    	return fullyRefinedNames;
    }
    
	 /**
     * Searches all lecs to refine to those that fit time selected, don't overlap a full time, and by major
     * @param major:Currently selected Major
     * @param timeSlot:Currently selected Timeslot
     * @return
     */
    private String[] getLecturesToAddElective(String major, String timeSlot){
 
		Lecture[] fullyRefined = allLectures.fullElectiveRefine(timeSlot, usersSchedule.callFullTimeSlots(),usersSchedule.callBreakSlots(), major, usersSchedule.callLecturesTaking());
        storedElective = fullyRefined;
        String[] fullyRefinedNames = new String[fullyRefined.length];
        for(int indexLec = 0; indexLec < fullyRefined.length; indexLec++){
             fullyRefinedNames[indexLec] = fullyRefined[indexLec].nameCall();
        }
   	
        return fullyRefinedNames;
   }
   	
    /**
     * 
     * @param name: Name of the desired Lec
     * @return nameCheck: The instance of Lec that matches the name
     */
    private Lecture getLecFromName(String name){
    	for(Lecture e: storedElective){
    		
    	}
    	for(Lecture nameCheck: storedElective){
    		if(nameCheck.nameCall().equals(name)){
    			return nameCheck;
    		}
    	}	

    	for(Lecture nameCheck: storedMajor){
    		if(nameCheck.nameCall().equals(name)){return nameCheck;}
    	}
    	
    	return null;
    }

	
    /**
    *updateButtonsForAddLec updates all buttons corresponding to timeslots of an added lec
    *@param: justAdded, the recently added lecture
    */
    private void updateButtonsForAddLec(Lecture justAdded){
    	for(String timeSlot: justAdded.getTimeSlots()){
    		int[] buttonArrayCoord = stringTimeToCoords(timeSlot); 
    		grid.updateButton(justAdded.getLecInfo(), justAdded.nameCall(), timeSlot, justAdded.getLecLocation(), true, false, buttonArrayCoord[0], buttonArrayCoord[1]);    		
    	}
    }
    
	/**
    *   stringTimeToCoords(stringTimeBlock) converts a string into an array of ints
    * To be used as coordinates to find a object in an array of ints
    *
    */
    private int[] stringTimeToCoords(String stringTimeBlock){
    	int[] buttonCoordinates = new int[2];
    	switch (stringTimeBlock.substring(0,3)) {
		case("MON"):
			dayInt = 1;
			break;
		case("TUE"):
			dayInt = 2;
		break;
			case("WED"):
			dayInt = 3;
		break;
			case("THU"):
			dayInt = 4;
			break;
		case("FRI"):
			dayInt = 5;
			break;	
		}
		buttonCoordinates[0]=dayInt;		
		switch (stringTimeBlock.substring(3)){
		case("8"):
			hourInt = 1;
			break;
		case("9"):
			hourInt = 2;
			break;
		case("10"):
			hourInt = 3;
			break;
		case("11"):
			hourInt = 4;
			break;
		case("12"):
			hourInt = 5;
			break;
		case("13"):
			hourInt = 6;
			break;
		case("14"):
			hourInt = 7;
			break;
		case("15"):
			hourInt = 8;
			break;
		case("16"):
			hourInt = 9;
			break;
		case("17"):
			hourInt = 10;
			break;
		case("18"):
			hourInt = 11;
			break;
		case("19"):
			hourInt = 12;
			break;
		case("20"):
			hourInt = 13;
			break;
		}
		buttonCoordinates[1] = hourInt;
		return buttonCoordinates;
    }
    
    /**
    *   updateButtonsForDroppedLec changes all timeslots from a lec to empty slots
    * @param: justDropped, the lecture that was dropped
    *
    *
    */
   private void updateButtonsForDropedLec(Lecture justDropped){
    	for(String timeString: justDropped.getTimeSlots()){
    		int[] buttonArrayCoord = stringTimeToCoords(timeString);
    		grid.updateButton( "EMPTY TIME BLOCK"," ", "        EMPTY", " ", false, false, buttonArrayCoord[0], buttonArrayCoord[1]);
    	}
    }
    
	/**
    *   TabChangeListener class does actions depending on which tab is selected
    *  
    *
    *
    */
	class TabChangeListener implements ChangeListener {
			//When the state of the tab changes
            public void stateChanged(ChangeEvent e) {
				//Set the selected string to null to avoid errors
                selectedTimeString = null;
                
                //Deselect all buttons to avoid confusion
				grid.deselectButtons();
                
                //Sets which pane the tab is on
				if(mainwindow.getTabbedPane().getSelectedIndex() == 0){thetab = "Add Components";}
				if(mainwindow.getTabbedPane().getSelectedIndex() == 1){thetab = "Remove Components";}
				if(mainwindow.getTabbedPane().getSelectedIndex() == 2){thetab = "Review Components";}
                //For every timeslot(day and hour) in the grid, make certain buttons click able based on traits
				for (int dayIndex = 1; dayIndex < 6; dayIndex++){
					for (int hourIndex = 1; hourIndex < 14; hourIndex++){
                        //Check for course or break
						isCoursePresent = grid.checkForCourseUpper(dayIndex, hourIndex);
						isBreakPresent = grid.checkForBreakUpper(dayIndex, hourIndex);
						
						if ((isCoursePresent == false) && ( isBreakPresent == false)) {
							if (thetab == "Add Components") {
								
                                //If the tab is add, update the list of courses presented to the user
					    		mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
					    		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
					    		mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
					    	
								grid.setEnabledTrueUpper(dayIndex, hourIndex);
							}
                            
							else if (thetab == "Remove Components") {
								grid.setEnabledFalseUpper(dayIndex, hourIndex);
							}
							else {grid.setEnabledFalseUpper(dayIndex, hourIndex);}
							}
							
						else if ((isCoursePresent == true) || (isBreakPresent == true)) {
							if (thetab == "Add Components") {
								grid.setEnabledFalseUpper(dayIndex, hourIndex);
								}
							else if (thetab == "Remove Components") {
								grid.setEnabledTrueUpper(dayIndex, hourIndex);
							}
							else {
								grid.setEnabledFalseUpper(dayIndex, hourIndex);
							}
						}
				}
			}
		}
	}
    
    /**
    *   MajorChangeListener listens for a change of the users major from a drop down menu
    */
    class MajorChangeListener implements ItemListener{
    	public void itemStateChanged(ItemEvent e){
    		if(e.getStateChange()==ItemEvent.SELECTED){
    			//When the major changes, update the selected major
                selectedMajor = (String)e.getItem();
                //Update the list of courses for the user
        		mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
        		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
        		mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
    		}
    		
    	}
    	
    }
    
    /**
    *   ClearTimeListener listens for the clear time selected button, and clears values associated with it
    */
    class ClearTimeListener implements ActionListener{
    	public void actionPerformed(ActionEvent clicked){
    		//Deselect all buttons in array
            grid.deselectButtons();
            //Set the selectedTimeString to null
    		selectedTimeString = null;
            //Update the courses presented to the Major
    		mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
    		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));   		
    		mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
    	}
    	
    }
    
    /**
    *   JListSelectingLecListener Listens for a change in the two JScrollPanes that hold possible courses to add
    */
    class JListSelectingLecListener implements ListSelectionListener{
    	public void valueChanged(ListSelectionEvent e){
            //Once the user has stopped updating
    		if(!e.getValueIsAdjusting()){
                //Set the selectedLecToAdd to the selected value
    			String name = ((JList)e.getSource()).getSelectedValue().toString();
    			selectedLecToAdd = name;
    		}
    	}
    }
    
    /**
    *   AddSelectedLecListener fires an event every time add course button is clicked
    */
    class AddSelectedLecListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    	
            try {	
                String lecNameSelected = selectedLecToAdd;
                
                Lecture selectedToAdd = getLecFromName(lecNameSelected);
                usersSchedule.addLecture(selectedToAdd);
                updateButtonsForAddLec(selectedToAdd);
                
                //Update the courses presented to the user
                mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
                mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
                mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
            }
    	
            catch(NullPointerException npe) {
                System.out.println("Null Pointer Exception.");
            }
		}
	}

    /**
    *   AddSlotBreak listener fires an event every time the add break button is clicked
    *
    */
    class AddSlotBreakListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
			//If it is a real object
            if (grid.getSelectedTimeBlock() != null){
                //Convert the desired stringCoords to an array of ints and update it to a break
				int[] buttonCoordsArray = stringTimeToCoords(selectedTimeString);
				//update the logic
                usersSchedule.addBreak(selectedTimeString);
				
                //Update the GUI
				grid.updateButton("Scheduled Break", " ", "       BREAK"," ", false, true, buttonCoordsArray[0], buttonCoordsArray[1]);
				
                //Update courses presented to user
				mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
		 		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
                mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
			}
			else JOptionPane.showMessageDialog(null, noTimeBlockSelected, "Error",JOptionPane.INFORMATION_MESSAGE);
    	}
    	
    }
    
	/**
    *   DropLecListener fires an event each time a lecture is selected to drop
    */
    class DropLecListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//Find the lecture from the name and remove it
    		if(selectedTimeString != null){
	    		for(Lecture perhapsDropped: usersSchedule.callLecturesTaking()){
	    			for(String timeSlot: perhapsDropped.getTimeSlots()){
	    				if(timeSlot.equals(selectedTimeString)){
	    					toDrop = new Lecture(perhapsDropped);	
	    				}
	    			}
	    		}
	    		updateButtonsForDropedLec(toDrop);
                usersSchedule.dropLecture(toDrop);
    		}
    	}
    }
    
    /**
    *   DropBreakListener fires an event every time a break is selected to be dropped
    *
    */
    class DropBreakListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		//Get the coordinates of the to be dropped block
            int[] buttonCoordsArray = stringTimeToCoords(selectedTimeString);
			
            //If the block is able to be selected(means that there is no class/empty)
            if (grid.getSelectedTimeBlock() != null){
				//Update the GUI
                grid.updateButton("Empty Timeslot", " ", "       EMPTY"," ", false, false, buttonCoordsArray[0], buttonCoordsArray[1]);	
			}
            //Else show error message
			else {JOptionPane.showMessageDialog(null, noTimeBlockSelected, "Error",JOptionPane.INFORMATION_MESSAGE);
			}
			//Update the logic schedule
			usersSchedule.dropBreak(selectedTimeString);
    	}	
    }
    
    /**
    *   TimeGridListener fires an event each time a timeblock is clicked
    *
    */
    class TimeGridListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
            //Set the selected time string to the set action command
    		selectedTimeString = e.getActionCommand();
            
            //Set the clear time block able to be clicked
    		mainwindow.setClearTimeBlockActive();    

            //Update the list shown
    		mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
    		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
    		mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
		}
    	
    }
    
    /**
    *   PrintTimeTableListener fires an event each time the print time table button is clicked  *
    */
    class PrintTimeTableListener implements ActionListener{
	    public void actionPerformed(ActionEvent e){
				//Prints a screenshot of the timegrid and saves them
                JPanel drawPanel = grid.getGridForPicture();
				drawPanel.setPreferredSize(new Dimension(650, 650));
				BufferedImage image = ScreenImage.createImage(drawPanel);
				//ScreenImage is a custom class that captures a component, panel, or entire screen. 
				Graphics2D graphics2D = image.createGraphics(); 
			
				drawPanel.paint(graphics2D);
				graphics2D.dispose();
				try{
					ImageIO.write(image,"png", new File("schedule.png"));
					}
				catch(Exception ex){
					ex.printStackTrace();
					}
				System.exit(0);
			}
	}
    
    /**
    *   RefineTimeBlocksDropListener fires an event each time a button on the drop menu is clicked
    * limits actions possible based on this limitations
    *
    *
    */
    class RefineTimeBlocksDropListener implements ActionListener {
		
        public void actionPerformed(ActionEvent e){
		String buttonLocation = (e.getActionCommand());
			for (int dayIndex = 1; dayIndex < 6; dayIndex++){
				for (int hourIndex = 1; hourIndex < 14; hourIndex++){
					isCoursePresent = grid.checkForCourseUpper(dayIndex, hourIndex);
					isBreakPresent = grid.checkForBreakUpper(dayIndex, hourIndex);
					if (isCoursePresent == true) {
						if (buttonLocation.equals("Remove Breaks")) {
							grid.setEnabledFalseUpper(dayIndex, hourIndex);
							}
						else if (buttonLocation.equals("Remove Courses")) {
							grid.setEnabledTrueUpper(dayIndex, hourIndex);
							}
						}
					else if (isBreakPresent == true) {
						if (buttonLocation.equals("Remove Breaks")) {
							grid.setEnabledTrueUpper(dayIndex, hourIndex);
							}
						else if (buttonLocation.equals("Remove Courses")) {
							grid.setEnabledFalseUpper(dayIndex, hourIndex);
						}
					}
				}
			}
		grid.deselectButtons();
		}
	} 
    
    /**
    *   Default Constructor for runner
    */
    public Controller(){
        //Create instances
    	allLectures = new LectureList();
     	mainwindow = new Schedule();
     	grid = new GridSchedule();
        //Set the visibility to true
     	mainwindow.setVisible(true);
		grid.setVisible(true);
		
        //Add Listeners
		mainwindow.addTabChangeListener(new TabChangeListener());
		mainwindow.addSelectedListener(new AddSelectedLecListener());
        mainwindow.addBreakListenerLower(new AddSlotBreakListener());
		mainwindow.addDropBreakListenerLower(new DropBreakListener());
        mainwindow.addJListSelectingLecListener(new JListSelectingLecListener());
        mainwindow.addMajorListener(new MajorChangeListener());
        mainwindow.addPrintTimeTableListenerLower(new PrintTimeTableListener());
		mainwindow.addClearTimeBlockListener(new ClearTimeListener());
        mainwindow.addDropLecListener(new DropLecListener());
        mainwindow.addRefineTimeBlockListener(new RefineTimeBlocksDropListener());  
        mainwindow.fillComboMajor(getLecturesToAddMajor(selectedMajor, selectedTimeString));
 		mainwindow.fillComboElective(getLecturesToAddElective(selectedMajor, selectedTimeString));
		grid.addTimeGridListener(new TimeGridListener());

        //Set up the master frame
        JFrame master = new JFrame("Schedule");
		master.setMinimumSize(new Dimension(1000, 600));
		master.setResizable(false);
        master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		master.add(mainwindow, BorderLayout.WEST);
		master.add(grid, BorderLayout.CENTER);
		master.pack();
		master.setVisible(true);
		
    }

    /**
    *   Main method to run the class
    */
    public static void main (String[] args){
    	Controller mainproject = new Controller();     	
    }
		
}


