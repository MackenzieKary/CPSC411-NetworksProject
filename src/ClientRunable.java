import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ClientRunable {

	//private JFrame popupWindow;
	private MainCalendarFrame calendarFrame;
	private LoginWindow logWindow;
	private ModifyEvent mEvent;
	private CreateUser crUser;
	private AllRegisteredUsers allUsers;
	private UserEvents allEventsWindow;
	private LocalDate currentViewedMonth;
	private NotificationsWindow notificationWindow;
	private boolean eventsWindowOpen = false;
	private boolean addEditWindowOpen = false;
	private boolean showUsersWindowOpen = false;
	private boolean loginIsOpen = false;
	public boolean isChanged = false;
	private boolean notificationsWindowOpen = false;
	private DataOutputStream output;
	private BufferedReader buffReader;
	private DayPanel clickedOn;
	private DayPanel oldClickedOn;
	private boolean changedColor = false;
	private int daySelected;
	private String[] recievedEvent;
	private DataOutputStream outBuffer;
	private BufferedReader inBuffer;		// <<  MIGHT NOT NEED THIS, it is down below as well. 
	private boolean loggedIn;
	private LocalDate dayOfEvent;
	private static ClientRunable runner;
	private static Socket clientSocket;
	
	private Event[] currentEvents;
	private Event[] currentNotifications;
	private ArrayList<String> currentUsers;		// This is where we put all the users to be put into "List Users" and share.
	
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
		if(args.length != 2){
			System.out.println("TODO: \n-Get information out of edit/add event"
					+ "\n-Constructor for new add/edit event that takes event information and fills it into the boxes to be edited"
					+ "\n-Add functionality for the submit button in modifyEvent (Add listeners and stuff)"
					+ "\n-Make sure password is filled in before entering program"
					+ "\n-Pull password (Like username) to be submitted to server"
					+ "\n-Do that whole socket networking thing"
					+ "\n-Constructor for list users that takes an array of Strings as names to display"
					+ "\n-");
		}
		
		runner = new ClientRunable();
			clientSocket = new Socket(args[0], Integer.parseInt(args[1])); 
			runner.outBuffer = new DataOutputStream(clientSocket.getOutputStream());
			runner.inBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		runner.calendarFrame.run();
		runner.logWindow.run();
		if(!runner.inBuffer.readLine().equals("fail")){
			while(!runner.inBuffer.readLine().equals("loggedOut")){
				String fromServer = runner.inBuffer.readLine();
				char msgType = fromServer.charAt(0);
				if(msgType == 'a'){
					//runner.addEvent(fromServer.substring(1));
				}
				else if(msgType == 'u'){
					runner.addUserName(fromServer.substring(1));
				}
				else if(msgType == 'n'){
					//runner.addNotification(fromServer.substring(1));
				}
				else if(msgType == 'f'){
					//runner.showFailure(fromSever.subString(1));
				}
			}
		}
		//runner.calendarFrame.run();
		//runner.addEvent("CPSC441 2016-03-23 16:38:00 16:40:00");
	}
	
	public void sendMessage(String msg){

		try{
			outBuffer.writeBytes(msg+"\n");
		}catch(IOException e){
			System.out.println("Error sending message: " + msg);
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public ClientRunable(){
		loggedIn = false;
		logWindow = new LoginWindow();
		logWindow.addWindowListener(new Login());

		
		calendarFrame = new MainCalendarFrame(new DayPanelClickedOn());
		calendarFrame.addAddOrEditListener(new AddOrEditEventListener());
		calendarFrame.addShowUserListener(new ShowUsersListener());
		calendarFrame.addShowUserEventsListener(new YourEvents());
		calendarFrame.addNotificationsListener(new NotificationsListener());
		
		
	}
	public void editCurrentEvent(String eventInfo){
		//eName + " " + eDate.toString()+" "+sTime.toString() + " " + eTime.toString()+" "+ pub + " o:"+ownerName + " "+ eventID;
		String[] eventComponents = eventInfo.split(" ");
		mEvent = new ModifyEvent();
		
		mEvent.setEventName(eventComponents[0]);	//Set the name of the event in the text box
		
		//Set event date
		daySelected = Integer.parseInt(eventComponents[1].substring(eventComponents[1].length()-2, eventComponents[1].length()));
		mEvent.changedTitle(eventComponents[1]);
		
		String startHour = eventComponents[2].substring(0, 2);
		String startMinute = eventComponents[2].substring(3, 5);
		mEvent.setStartHour(startHour);
		mEvent.setStartMin(startMinute);
		
		String endHour = eventComponents[3].substring(0, 2);
		String endMinute = eventComponents[3].substring(3, 5);
		mEvent.setEndHour(endHour);
		mEvent.setEndMin(endMinute);
		mEvent.addSubmitButtonListener(new SubmitAddEventButton());
		mEvent.addWindowListener(new CloseEditWindowEvent());
		mEvent.run();
		addEditWindowOpen = true;
		
		
	}
	public void addUserName(String userNameInfo){
		//End and call the list users window when last user has been added to the list. 
		currentUsers.add(userNameInfo);
	}
	
	
    class AddOrEditEventListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!subWindowOpen()){
				dayOfEvent = calendarFrame.getDate();
				dayOfEvent = dayOfEvent.withDayOfMonth(daySelected);
				mEvent = new ModifyEvent();
				mEvent.addSubmitButtonListener(new SubmitAddEventButton());
				mEvent.changedTitle(dayOfEvent.toString());
				mEvent.addWindowListener(new CloseEditWindowEvent());
				mEvent.run();
				addEditWindowOpen = true;
			}
		} 	
    }
    class SubmitAddEventButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			dayOfEvent = calendarFrame.getDate();
			dayOfEvent = dayOfEvent.withDayOfMonth(daySelected);
			System.out.println(dayOfEvent);
			System.out.println("In Submit Event"+dayOfEvent);
			String[] fromEventBox = mEvent.getSelectedInfo();
			LocalTime startTime = LocalTime.of(Integer.parseInt(fromEventBox[1]),Integer.parseInt(fromEventBox[2]));
			LocalTime endTime = LocalTime.of(Integer.parseInt(fromEventBox[3]),Integer.parseInt(fromEventBox[4]));
			try{
				//Event testInput = new Event(fromEventBox[0], startTime, endTime, dayOfEvent);
				//sendMessage("e:" +testInput.toString());
//				String testInput = fromEventBox[0] + " " + startTime.toString() + " " + endTime.toString() + " " + dayOfEvent.toString();
//				System.out.println(testInput);
			}catch(Exception ex){
				System.out.print("Invalid event times");
			}
			mEvent.closeAddModifyEventWindow();
			System.out.println("THE FOLLOWING INFORMATION IS FROM THE ADD EVENT WINDOW!");
			addEditWindowOpen = false;
		}	
    }
    class ShowUsersListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!subWindowOpen()){
				allUsers = new AllRegisteredUsers();
				allUsers.addWindowListener(new CloseViewUserWindowEvent());
				allUsers.run();
				showUsersWindowOpen = true;
			}
		} 	
    }
    class YourEvents implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!subWindowOpen()){
				//Pass in current events and column names for this
				allEventsWindow = new UserEvents();
				allEventsWindow.addWindowListener(new CloseEventListWindowEvent());
				allEventsWindow.addEditButtonListener(new EditShownEventsButton());
				allEventsWindow.addDeleteButtonListener(new DeleteShownEventsButton());
				allEventsWindow.run();
				eventsWindowOpen = true;
				
			}
		} 	
    }
    class EditShownEventsButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//String str = allEventsWindow.getHighlightedRow.toString());
			allEventsWindow.closeWindow();
			//make constructor for addEventsWindow that includes the information about the event
			mEvent = new ModifyEvent();
			mEvent.addWindowListener(new CloseEditWindowEvent());
			mEvent.run();
			addEditWindowOpen = true;
			
		}	
    }
    class DeleteShownEventsButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int shouldDelete = allEventsWindow.confirmDeletion();
			if (shouldDelete == 0){
				// NO, do NOT delete this event
				System.out.println("THIS EVENT WILL NOT BE DELETED");
				
			}else{
				// Yes, delete this event
				System.out.println("THIS EVENT WILL BE DELETED");	
				//Don't close the window, until the event deletion request has been sent to the server and has been deleted
				allEventsWindow.closeWindow();
				eventsWindowOpen = false;
			}
		}	
    }
    class Login implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!subWindowOpen()){
				isChanged = true;
				// if (username == real and password works, close window, and go to calendar.
				//System.out.println("Login :" + logWindow.getTextField());
				/**
				 * Just added code here 
				 */
				sendMessage("l" + runner.logWindow.getTextField() + "_" + runner.logWindow.getTextField());		//<-- This is being sent!
				
				try {
					inBuffer = 
					          new BufferedReader(new
					          InputStreamReader(clientSocket.getInputStream()));
					String line = inBuffer.readLine();
					System.out.println("response statement: " + line + "\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		//<-- This is being received! 
				logWindow.closeLoginWindow();
				calendarFrame.run(); //TODO:make work
			}
		} 	
    }
    class NotificationsListener implements ActionListener{		//This is for the button in MainCalendarFrame, (Notifications) 
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!subWindowOpen()){
				notificationWindow = new NotificationsWindow();
				notificationWindow.addWindowListener(new CloseNotificationWindow());
				notificationWindow.addAcceptButtonListener(new AcceptNotificationsButton());
				notificationWindow.addDeclineButtonListener(new DeclineNotificationsButton());
				notificationWindow.run();
				notificationsWindowOpen = true;
			}
		} 	
    }
    class AcceptNotificationsButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(notificationWindow.getHighlightedRow());
			notificationWindow.closeWindow();
			notificationsWindowOpen = false;
		}	
    }
    class DeclineNotificationsButton implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int shouldDecline = notificationWindow.confirmDecline();
			if (shouldDecline == 0){
				// NO, do NOT delete this event
				System.out.println("THIS EVENT WILL NOT BE DECLINED");
				
			}else{
				//Yes decline this event
				notificationWindow.closeWindow();
				notificationsWindowOpen = false;
			}
		}	
    }
    public String formatEventInfoFromServer(String serverEventInfo){
    	
    	return "";
    }
    
    
    
    
    private boolean subWindowOpen(){		//This will end up checking to see if there is ANY window open, if there is, don't let any more open up.
    	if (showUsersWindowOpen || addEditWindowOpen || eventsWindowOpen || loginIsOpen || notificationsWindowOpen){
    		return true;
    	}
    	return false;
    }
    class CloseViewUserWindowEvent implements WindowListener{
    	@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			showUsersWindowOpen = false;
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
    }
    class CloseEditWindowEvent implements WindowListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			addEditWindowOpen = false;
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
    	
    }
    class CloseEventListWindowEvent implements WindowListener{
    	@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			eventsWindowOpen = false;
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
    }
    class CloseLoginWindow implements WindowListener{
    	@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			loginIsOpen = false;
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
    }
    class CloseNotificationWindow implements WindowListener{

    	@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			notificationsWindowOpen = false;
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}
    }
    class DayPanelClickedOn implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (clickedOn != null){	//This will reset the color 
				clickedOn.setBackground(new Color(238, 238, 238));
				calendarFrame.toggleAddButtonOff();
				oldClickedOn = clickedOn;
			}
			
			clickedOn = (DayPanel) e.getSource();
			if (oldClickedOn == clickedOn){
				if (changedColor){
					calendarFrame.toggleAddButtonOff();
					clickedOn.setBackground(new Color(238, 238, 238));
					changedColor = false;
				}else{
					clickedOn.setBackground(Color.MAGENTA);
					clickedOn.revalidate();
					calendarFrame.toggleAddButtonOn();
					changedColor = true;
					daySelected = clickedOn.getDay();
				}
			}else{
				clickedOn.setBackground(Color.MAGENTA);
				clickedOn.revalidate();
				calendarFrame.toggleAddButtonOn();
				daySelected = clickedOn.getDay();
				changedColor = true;
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
