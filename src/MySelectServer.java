

/*
 *A backend server for our 441 project
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

//import User;

public class MySelectServer {
    public static int BUFFERSIZE = 100;
    public User[] users;
    public static void main(String args[]) throws Exception 
    {
    	MySelectServer server = new MySelectServer();
        if (args.length != 1)
        {
            System.out.println("Usage: UDPServer <Listening Port>");
            System.exit(1);
        }

        // Initialize buffers and coders for channel receive and send
        String line = "";
        String msgCode = "";
        Charset charset = Charset.forName( "us-ascii" );  
        CharsetDecoder decoder = charset.newDecoder();  
        CharsetEncoder encoder = charset.newEncoder();
        ByteBuffer inBuffer = null;
        CharBuffer cBuffer = null;
   
        int bytesSent, bytesRecv;     // number of bytes sent or received
        server.users = new User[]{new User("a"," "), new User("b","c")};
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
                // Walk through the ready set
                while (readyItor.hasNext()) 
                {
                    // Get key from set
                    SelectionKey key = (SelectionKey)readyItor.next();

                    // Remove current entry
                    readyItor.remove();
                    byte[] toSend = new byte[0];
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
                            BufferedReader asdf = new BufferedReader(new InputStreamReader((socket.getInputStream())));
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
                            inBuffer.flip();      // make buffer available  
                            decoder.decode(inBuffer, cBuffer, false);
                            cBuffer.flip();
                           
                            line = cBuffer.toString();
                            
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            System.out.println("Line at char below");
                            System.out.println(line.charAt(0));
                            ByteBuffer outBuff = ByteBuffer.allocate(BUFFERSIZE);
                            if(line.charAt(0)=='l'){
                            	System.out.println("Login attempt:" + line);
                            	
                            	User test = server.matchUsnPW(line.substring(1));
                            	System.out.println(test +"asdf");
                            	if(test==null){
                            		//toSend = new byte[]{'f'};
                            		System.out.println(inBuffer.toString());

                            		inBuffer.flip();
                            		System.out.println("Buffer limit: " + inBuffer.limit());
                            		//inBuffer = ByteBuffer.allocateDirect(BUFFERSIZE);
                            		outBuff.putChar('f');
                            		System.out.println(inBuffer.toString());
                            	}
                            }
                            
                            // Echo the message back 
                            //inBuffer.putChar(toSend);
                            //inBuffer.flip();
                            
                            //bytesSent = cchannel.write(outBuff); 
                            
                            
                            
                            
                            outBuff.flip();      // make buffer available  
                            decoder.decode(outBuff, cBuffer, false);
                            cBuffer.flip();
                            line = cBuffer.toString();
                            System.out.print(line);
                   
                            // Echo the message back
                            outBuff.putChar('f');
                            outBuff.flip();
                            //inBuffer.putChar('f');
                            bytesSent = cchannel.write(outBuff); 
                            
                            
//                            if (bytesSent != bytesRecv)
//                            {
//                            	if(bytesSent == bytesRecv+1){
//                                    System.out.println("THIS IS GOOD?");
//                                }else{
//	                                System.out.println("write() error, or connection closed");
//	                                key.cancel();  // deregister the socket
//	                                continue;
//                                }
//                            }
                            
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
    public String getUserEvents(User toGet){
    	
    	return"";
    }
    public User matchUsnPW(String packetIn){
    	System.out.println(Arrays.toString(users));
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
}
