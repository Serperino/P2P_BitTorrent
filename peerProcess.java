import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.util.Vector;

//socket information:
//https://www.oracle.com/java/technologies/jpl2-socket-communication.html
public class peerProcess {
   //The peer partaking in the process
    static Peer currPeer;
    private ServerSocket serverSocket;
    static Socket requestSocket;
    static ObjectOutputStream out;        
 	static ObjectInputStream in;          
	String message;                
	String MESSAGE;          
    message typemessage;
    handShake handshake;











    //Information to load into the config file
    //Data structures being used (WIP)
    static fileLoader configInfo = new fileLoader();
    HashMap<Integer, Peer> connectedPeers;
    HashMap<Integer, Thread> connectedThreads;
    Vector<Peer> activePeers = new Vector<Peer>();



















    public static void main(String[] args) throws IOException{
        if(args.length == 0)
        {
            System.out.println("Invalid arguments");
            
        }
        else
        {
            fileLoader loader = new fileLoader();
            //Loads information from common.cfg and peer.info
            configInfo.loadCommon();
            //This specifically contains the map of the peers
            configInfo.loadpeerInfo();
             Integer inputID = Integer.parseInt(args[0]);
             peerVerifier(inputID);
             System.out.println("Running peer: " + currPeer.getpeerID());
             beginListening();
             beginSearching();


                
             
     


 
        }



       

        

    }
    








    //Uses some of the client code 
    public static void beginSearching() throws UnknownHostException, IOException
    {
        for (Integer peerID : configInfo.getpeerMap().keySet()) 
        {
            if(currPeer.getpeerID() == peerID)
            {
                break;
            }
            else
            {
            Peer workingPeer = configInfo.getpeerMap().get(peerID);
            String address = workingPeer.gethostName();
            requestSocket = new Socket("localhost", peerID);
            System.out.println("Connected to " + peerID);
           // out = new ObjectOutputStream(requestSocket.getOutputStream());
			//out.flush();
			//in = new ObjectInputStream(requestSocket.getInputStream());

            }
    }
    }

   

    





    public static void peerVerifier(Integer peerID) 
    {
        boolean peerFound = false;
        for(Integer inputID: configInfo.getpeerMap().keySet())
        {
            if(inputID.equals(peerID))
            {
              System.out.println("Running peer: " + peerID);
              peerFound = true;
              currPeer = configInfo.getpeerMap().get(peerID);
            }
        }
        if(!peerFound)
        {
            System.out.println("Peer not found :[");
        }
    }









    //temporary lambda function code until thread class is setup, again just testing connections
    public static void beginListening()
     {

        Runnable serverStart = () -> {
            try {
                serverStart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Thread listeningThread = new Thread(serverStart);
        listeningThread.start();

    }
    
    








    //Uses some of the server code in the example
    public static void serverStart() throws IOException
    {
        ServerSocket acceptingConnections = null;
        System.out.println("The server is running."); 
        //Going to have to get the start of a map to determine how many connections have to be made as the rubric says
        //this will happen later though.
       // int connectionstobeMade = configInfo.getPeerMap().start
        for (Integer peerID : configInfo.getpeerMap().keySet()) {
            if(currPeer.getpeerID() == peerID){
                acceptingConnections = new ServerSocket(currPeer.getpeerID());
                System.out.println("ServerSocket opened at ID: " + currPeer.getpeerID());
                int clientNum = 1;
                try {
            		while(true) {
                        
                		new Handler(acceptingConnections.accept(), peerID).start();
				        System.out.println("Client "  + (peerID + clientNum)+ " is connected!");
                        clientNum++;
            			}
        	} finally {
                acceptingConnections.close();
        	} 
        

            }
            //GRAVEYARD OF CODE might pull  ideas from this later
            // try {
            //     while(true) {
            //         Peer workingPeer = configInfo.getpeerMap().get(peerID);
            //         //String address = workingPeer.gethostName();
            //         //new Handler(, peerID).start();
            //         System.out.println("Client "  + peerID + " is connected!");
            //     }
            // } catch (IOException e) {
            //     System.err.println("Error opening ServerSocket for peer with ID: " + peerID);
            //     e.printStackTrace();
            // }
        }
        //Right now this just listens for the whole time
        	//ServerSocket listener = new ServerSocket(currPeer.getpeerID());
        	// try {
            // 		while(true) {
            //             new Handler(acceptingConnections.accept(),clientNum).start();
            //             System.out.println("Client "  + clientNum + " is connected!");
                       

            //             // //hardcoded test values for now
            //     		// new Handler(listener.accept(), 1001).start();
            //             // new Handler(listener.accept(), 1002).start();
            //             // new Handler(listener.accept(), 1003).start();
            //             // new Handler(listener.accept(), 1004).start();
            //             // new Handler(listener.accept(), 1005).start();

			// //	System.out.println("Client "  + "1001" + " is connected!");
			
            // 			}
        	//} finally {
            	//	listener.close();
                   // acceptingConnections.close()
;        //	} 

        //This is likely going to be in the final version, but using the base code right now for testing
        // try{
        //     ServerSocket socket = new ServerSocket(currPeer.getpeerID());
        //     Thread hostThread = new Thread();
        //     currPeer.setServerSocket(socket);
        //     currPeer.sethostThread(hostThread);
        //     System.out.println("Server started on " + currPeer.getpeerID());
        //     currPeer.gethostThread().start();
                
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }
       
          
         }
    
    










     //This is the handler class copied directly from the server example code
     //These functions will likely not all be in the final model but are just here for testing right now
     //will modify in the future as needed
     private static class Handler extends Thread {
        private String message;    //message received from the client
		private String MESSAGE;    //uppercase message send to the client
		private Socket connection;
        private ObjectInputStream in;	//stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
		private int no;		//The index number of the client

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

        public void run()
         {
 		try
        {
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try
            {
				while(true)
				{
					//receive the message sent from the client
					message = (String)in.readObject();
					//show the message to the user
					System.out.println("Receive message: " + message + " from client " + no);
					//Capitalize all letters in the message
					MESSAGE = message.toUpperCase();
					//send MESSAGE back to the client
					sendMessage(MESSAGE);
				}
			}
			catch(ClassNotFoundException classnot)
            {
					System.err.println("Data received in unknown format");
				}
		}
		catch(IOException ioException)
        {
			System.out.println("Disconnect with Client " + no);
		}
		finally
        {
			//Close connections
			try
            {
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException)
            {
				System.out.println("Disconnect with Client " + no);
			}
		}
	}














	//send a message to the output stream
	public void sendMessage(String msg)
	{
		try
        {
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}
		catch(IOException ioException)
        {
			ioException.printStackTrace();
		}
	}

    }






}

        
    



