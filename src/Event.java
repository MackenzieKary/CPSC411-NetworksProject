import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.MissingFormatArgumentException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class Event{
	private static int trackingEventID = 0;
	private boolean isPublic;
	private LocalTime sTime;
	private LocalTime eTime;
	private LocalDate eDate;
	private String eName;
	private String ownerName;
	private int eventID;
	private String period;
	
	public Event(){
		
	}
	/**
	 * @param packetIn: string in the format 'name yyyy-mm-dd hh:mm:ss hh:mm:ss {t|f} o:name eventID"
	 */
	
	public Event(String packetIn)throws IllegalFormatException{
		String[] eventComponents = packetIn.split("_");
		if(eventComponents.length == 6){		
			System.out.println(Arrays.toString(eventComponents));
			this.eName = eventComponents[0];
			this.eDate =LocalDate.parse(eventComponents[1],DateTimeFormatter.ISO_DATE);
			this.sTime = LocalTime.parse(eventComponents[2],DateTimeFormatter.ISO_LOCAL_TIME);
			this.eTime = LocalTime.parse(eventComponents[3], DateTimeFormatter.ISO_LOCAL_TIME);
			if(eventComponents[4].equals("f")){
				this.isPublic = false;
			}
			else{
				this.isPublic = true;
			}
			this.ownerName = eventComponents[5].substring(2);
			System.out.println("Event Owner: " + ownerName);
			this.eventID = trackingEventID;
			trackingEventID++;
		}
		else if(eventComponents.length == 7){
			System.out.println(Arrays.toString(eventComponents));
			this.eName = eventComponents[0];
			this.eDate =LocalDate.parse(eventComponents[1]);
			this.sTime = LocalTime.parse(eventComponents[2],DateTimeFormatter.ISO_LOCAL_TIME);
			this.eTime = LocalTime.parse(eventComponents[3], DateTimeFormatter.ISO_LOCAL_TIME);
			if(eventComponents[4].equals("f")){
				this.isPublic = false;
			}
			else{
				this.isPublic = true;
			}
			this.ownerName = eventComponents[5].substring(2);
			System.out.println("Event Owner: " + ownerName);
			this.eventID = Integer.parseInt(eventComponents[6]);
		}
		else {
			throw new MissingFormatArgumentException("Incorrect number of args in event constructor");
		}
		if(eTime.isBefore(sTime)){
			throw new MissingFormatArgumentException("Event end is before Event start");
		}
		else if(eTime.equals(sTime)){
			throw new MissingFormatArgumentException("Start and end time are the same");
		}
	}
	//A method to makes sure the tracking ID remains relevant when events are constructed for alternate reasons than creation (comparisons ect)
	public static Event eventFromString(String fromEvent){
		return new Event(fromEvent);
	}
	
	public boolean overlapsWith(Event checkAgainst){
		//One event starts after the other ends
		if (sTime.isAfter(checkAgainst.getEndTime())){
			return false;
		}
		else if(eTime.isBefore(checkAgainst.getStartTime())){
			return false;
		}
		return true;
		
	}
	public String toString(){
		String pub;
		if(isPublic){
			pub = "t";
		}
		else{
			pub = "f";
		}
		return eName + "_" + eDate.toString()+"_"+sTime.toString() + "_" + eTime.toString()+"_"+ pub + "_o:"+ownerName + "_"+ eventID;
	}
	
	public boolean equals(Event comparedTo){
		if(comparedTo.toString().equals(this.toString()))
			return true;	
		else 
			return false;
	}
	public static void main(String[] args){
		Event test = new Event("CPSC441 2016-03-23 16:38:00 16:40:00 f o:Chris 123");
		System.out.println(test);
		Event test2 = Event.eventFromString(test.toString());
		Event noID = new Event("asdf 2016-03-23 16:34:00 16:37:00 f o:Chris");
		System.out.println(noID);
		System.out.println(test.overlapsWith(noID));
	}
	public String geteName() {
		return eName;
	}
	public void seteName(String eName) {
		this.eName = eName;
	}
	public LocalTime getStartTime(){
		return sTime;
	}
	public LocalTime getEndTime(){
		return eTime;
	}
	public String getOwner(){
		return ownerName;
	}
	public LocalDate getDate(){
		return eDate;
	}
	public String getPeriod(){
		return period;
	}
	
	//public void 
}