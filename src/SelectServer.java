/*
 * A simple TCP select server that accepts multiple connections and echo message back to the clients
 * For use in CPSC 441 lectures
 * Instructor: Prof. Mea Wang
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

import javax.swing.plaf.ListUI;

public class SelectServer {
    public static int BUFFERSIZE = 100;
    public User[] users;
    public static void main(String args[]) throws Exception 
    {
    	SelectServer server = new SelectServer();
        if (args.length != 1)
        {
            System.out.println("Usage: UDPServer <Listening Port>");
            System.exit(1);
        }

        // Initialize buffers and coders for channel receive and send
        String line = "";
        Charset charset = Charset.forName( "us-ascii" );  
        CharsetDecoder decoder = charset.newDecoder();  
        CharsetEncoder encoder = charset.newEncoder();
        ByteBuffer inBuffer = null;
        CharBuffer cBuffer = null;
        int bytesSent, bytesRecv;     // number of bytes sent or received
        server.users = new User[]{new User("a","abc"), new User("b","c")};
        
        // Initialize the selector
        Selector selector = Selector.open();

        // Create a server channel and make it non-blocking
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
       
        // Get the port number and bind the socket
        InetSocketAddress isa = new InetSocketAddress(Integer.parseInt(args[0]));
        channel.socket().bind(isa);

        // Register that the server selector is interested in connection requests
        channel.register(selector, SelectionKey.OP_ACCEPT);

        // Wait for something happen among all registered sockets
        try {
            boolean terminated = false;
            while (!terminated) 
            {
                if (selector.select(500) < 0)
                {
                    System.out.println("select() failed");
                    System.exit(1);
                }
                
                // Get set of ready sockets
                Set readyKeys = selector.selectedKeys();
                Iterator readyItor = readyKeys.iterator();
                String stringToSendBack = "";
                // Walk through the ready set
                while (readyItor.hasNext()) 
                {
                    // Get key from set
                    SelectionKey key = (SelectionKey)readyItor.next();

                    // Remove current entry
                    readyItor.remove();

                    // Accept new connections, if any
                    if (key.isAcceptable())
                    {
                        
                        SocketChannel cchannel = ((ServerSocketChannel)key.channel()).accept();
                        cchannel.configureBlocking(false);
                        System.out.println("Accept conncection from " + cchannel.socket().toString());
                        
                        // Register the new connection for read operation
                        cchannel.register(selector, SelectionKey.OP_READ);
                    } 
                    else 
                    {
                        SocketChannel cchannel = (SocketChannel)key.channel();
                        if (key.isReadable())
                        {
                            Socket socket = cchannel.socket();
                        
                            // Open input and output streams
                            inBuffer = ByteBuffer.allocateDirect(BUFFERSIZE);
                            cBuffer = CharBuffer.allocate(BUFFERSIZE);
                             
                            // Read from socket
                            bytesRecv = cchannel.read(inBuffer);
                            if (bytesRecv <= 0)
                            {
                                System.out.println("read() error, or connection closed");
                                key.cancel();  // deregister the socket
                                continue;
                            }
                            ///************ USE THIS TO SEND STRING BACK TO CLIENT************///
                            inBuffer.flip();      // make buffer available  
                            decoder.decode(inBuffer, cBuffer, false);
                            cBuffer.flip();
                            line = cBuffer.toString();
                          
                          ///************************************************************************///
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            //Above is all good code
                            System.out.println("Line is : " + line);
                            ByteBuffer outBuff = ByteBuffer.allocate(BUFFERSIZE);
                            char lineToCheck = line.charAt(0);
                            line = line.substring(1);
                            System.out.println("THE STUFF " + line);
                            if(lineToCheck == 'l'){
                            	//line = line.substring(1);
                            	System.out.println("Login attempt: " + line);
                            	
                            	User test = server.matchUsnPW(line);
                            	System.out.println(test +"THIS IS THE RESULT OF THE MATCHUsnPW");
                            	if(test==null){
                            		stringToSendBack = "f";
                       
                            	}else{
                            		stringToSendBack = "loggedOn";
                            		String allUserEvents = sendUsersEvents(test);
                            		System.out.println("POST LOGGED ON: " + allUserEvents);
                            		if(allUserEvents.length()>0){
                            			stringToSendBack += "\n"+allUserEvents;
                            		}
                            		System.out.println("Users events: " + allUserEvents);
                            	}
                            }else if(lineToCheck == 'e'){
                            	System.out.println("About to add event");
                            	System.out.println(line);
                            	Event toAdd = new Event(line);
                            	User eventOwner = server.matchUsrName(toAdd.getOwner());
                            	eventOwner.addEvent(line);
                            }else if(lineToCheck == 'd'){//Delete event
                            	System.out.println("About to delete event");
                            	System.out.println(line);
                            	Event toDelete = new Event(line);
                            	User eventOwner = server.matchUsrName(toDelete.getOwner());
                            	eventOwner.removeEvent(line);
                            }else if(lineToCheck == 'c'){//New users
                            	System.out.println("About to make a new user!");
                            	if(server.addNewUser(line)){
                            		stringToSendBack = "loggedOn";
                            	}else{
                            		stringToSendBack = "failed";
                            	}    	
                            }else if(lineToCheck == 'u'){ //Put event in users notification group
                            	stringToSendBack = server.listUsers();
                            }
                            
                            
                            //Below is all good code
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            
                            System.out.println("String = " + stringToSendBack);
                                                
                            // Send the message back
                            outBuff.flip();
                            outBuff = encoder.encode(CharBuffer.wrap((stringToSendBack) + "\n"));
                            cchannel.write(outBuff); 
                            
                            System.out.println("SENT");
                            
                            if (line.equals("terminate\n"))
                                terminated = true;
                         }
                    }
                } // end of while (readyItor.hasNext()) 
            } // end of while (!terminated)
        }
        catch (IOException e) {
            System.out.println(e);
        }
        
 
        // close all connections
        Set keys = selector.keys();
        Iterator itr = keys.iterator();
        while (itr.hasNext()) 
        {
            SelectionKey key = (SelectionKey)itr.next();
            //itr.remove();
            if (key.isAcceptable())
                ((ServerSocketChannel)key.channel()).socket().close();
            else if (key.isValid())
                ((SocketChannel)key.channel()).socket().close();
        }
    }
    private boolean addNewUser(String signUp){
    	String[] userNamePassword = signUp.split("_");
    	User testIfNameUsed = matchUsrName(userNamePassword[0]);
    	if(testIfNameUsed != null){
    		return false;
    	}
    	else{
    		addUserToArray(new User(userNamePassword[0],userNamePassword[1]));
    	}
    	return true;
    }
    private void addUserToArray(User user) {
		ArrayList<User> arrUserList = new ArrayList<User>(Arrays.asList(users));
		arrUserList.add(user);
		users = new User[arrUserList.size()];
		users = arrUserList.toArray(users);
		
	}
	private static String sendUsersEvents(User test) {
		String eventsMessage = "";
    	for(Event toSend: test.getUserEvents()){
    		eventsMessage+="a"+toSend.toString()+"\n";
    	}
    	
    	return eventsMessage;
		
	}
	public User matchUsnPW(String packetIn){
    	System.out.println(Arrays.toString(users));
    	System.out.println("packetIN: " + packetIn);
    	String[] splitPacket = packetIn.split("_");
    	System.out.println(Arrays.toString(splitPacket));
    	for(User possibleMatch: users){
    		if(possibleMatch.testPassword(splitPacket[0], splitPacket[1])){
    			possibleMatch.toggleLoggedOn();
    			return possibleMatch;
    		}
    	}
    	System.out.println("Returning null");
    	return null;
    }
    public User matchUsrName(String packetIn){
    	System.out.println("Packet IN: "+ packetIn);
    	for(User possibleMatch: users){
    		System.out.println(possibleMatch.getUserName());
    		if(possibleMatch.getUserName().equals(packetIn)){
    			System.out.println("Match found!");
    			return possibleMatch;
    		}
    	}
    	System.out.println("Returning null in match user name");
    	return null;
    }

    public String listUsers(){
    	String toReturn ="";
    	for(User getNameFrom: users){
    		toReturn += "u" + getNameFrom.getUserName() +"\n";
    	}
    	return toReturn;
    }
}
