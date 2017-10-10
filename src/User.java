import java.util.ArrayList;
import java.util.Arrays;
public class User {
	private String userName;
	private String userPassWord;
	private Event[] currentEvents;
	private Event[] eventNotification;
	private boolean loggedOn;
	
	public User(String usn, String pw){
		this.userName = usn;
		this.userPassWord = pw;
		loggedOn = false;
		currentEvents = new Event[0];
		eventNotification = new Event[0];
	}
	public boolean testPassword(String usrnme, String pwrd){
		if(usrnme.equals(userName) && pwrd.equals(userPassWord)){
			System.out.println("PW match found");
			return true;
		}
		System.out.println("");
		return false;
	}
	public boolean removeEvent(String toRemove){
		ArrayList<Event> eventsList = new ArrayList<Event>(Arrays.asList(currentEvents));
		for(int i = 0; i < eventsList.size();i++){
			if(eventsList.get(i).toString().equals(toRemove)){
				eventsList.remove(i);
				currentEvents = new Event[eventsList.size()];
				currentEvents = eventsList.toArray(currentEvents);
				return true;
			}
		}
		return false; 
	}
	
	public boolean addEvent(String toAdd){
		if (!removeEvent(toAdd)){
			ArrayList<Event> eventsList = new ArrayList<Event>(Arrays.asList(currentEvents));
			eventsList.add(new Event(toAdd));
			currentEvents = new Event[eventsList.size()];
			currentEvents = eventsList.toArray(currentEvents);
			return true;
		}
		return false;
	}
	
	public User(){
		
	}
	
	public boolean getLoggedOn(){
		return loggedOn;
	}
	
	public void toggleLoggedOn() {
		this.loggedOn = !loggedOn;
		
	}
	
	public String toString(){
		return userName + " " + userPassWord+" "+loggedOn;
	}
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	
	public Event[] getUserEvents(){
		return currentEvents;
	}
	
	public Event[] getUserNotifications(){
		return eventNotification;
	}
}
