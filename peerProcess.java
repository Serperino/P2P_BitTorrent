import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.io.ByteArrayInputStream;
import java.util.Vector;
import java.util.BitSet;
import java.io.OutputStream;



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
    static ServerSocket acceptingConnections;
    static byte[] fileData;
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
             if(currPeer.hasFile() == 1){
                currPeer.bitField.set(0, fileLoader.gettotalPieces());
                String folderName = "peer_" + currPeer.getpeerID();
                loadFile(folderName);
    
             }
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
            new clientHandler(requestSocket, peerID).start();
            System.out.println("Connected to " + peerID);
           

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
        acceptingConnections = new ServerSocket(currPeer.getpeerID());
        System.out.println("ServerSocket opened at ID: " + currPeer.getpeerID());
        System.out.println("The server is running."); 
        //Going to have to get the start of a map to determine how many connections have to be made as the rubric says
        //this will happen later though.
       // int connectionstobeMade = configInfo.getPeerMap().start
        for (Integer peerID : configInfo.getpeerMap().keySet()) {
            if(currPeer.getpeerID() == peerID){  
            }
            else{

                new Handler(acceptingConnections.accept(), peerID).start();
				System.out.println("Client "  + (peerID)+ " is connected!");
    
            }
        }
    
    }






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

			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());
            handShake newShake = new handShake(currPeer.getpeerID());
            byte[] handshakeBytes = newShake.encode();
            out.writeObject(handshakeBytes);
            out.flush();
            if(currPeer.hasFile() == 1){
                out.writeObject(fileData);
                out.flush(); // Flush the stream to ensure all data is sent
                System.out.println("File data sent successfully.");
            }
            System.out.println("or do i make it out?");
            System.out.println(currPeer.hasFile());
           // in = new ObjectInputStream(connection.getInputStream());
            
			try
            {
				while(true)
				{
                    //CURRENT DEBUGGING FOR OUTPUT
                    byte[] incomingHandshake = (byte[]) in.readObject();
                    handShake decoded = handShake.decode(incomingHandshake);
                    System.out.println("This is the header SERVERSIDE: " + decoded.getHeader());
                    System.out.println("This is the ID SERVERSIDE: " + decoded.getPeerId());
                    System.out.println("This is the zero bits length SERVERSIDE: " + decoded.getzerobitsLength());
                    
				}
			} catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           finally
            {
                //Close connections
                //try
                //{
                    //in.close();
                    //out.close();
                    //connection.close();
                //}
                //catch(IOException ioException)
               // {
                    
                //ystem.out.println("Disconnect with Client " + no);
                //}
            }
			//catch(ClassNotFoundException classnot)
            //{
				//	System.err.println("Data received in unknown format");
			//	}
		} 
		catch(IOException ioException)
        {
			System.out.println("Disconnect with Client " + no);
		}
		finally
        {
			//Close connections
			//try
            //{
				//in.close();
				//out.close();
				//connection.close();
			//}
			//catch(IOException ioException)
           // {
                
				System.out.println("Disconnect with Client " + no);
			//}
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


    // same handler as before but now for the client
    private static class clientHandler extends Thread {
        private String message;    //message received from the client
		private String MESSAGE;    //uppercase message send to the client
		private Socket connection;
        private ObjectInputStream in;	//stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
		private int no;		//The index number of the client

        public clientHandler(Socket connection, int no) {
            this.connection = connection;
        this.no = no;
    }
    public void run()
         {
            try {
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
               
			//in = new ObjectInputStream(connection.getInputStream());
            
            try
            {
				while(true)
				{
					byte[] incomingHandshake = (byte[]) in.readObject();
                    handShake decoded = handShake.decode(incomingHandshake);
                    System.out.println("This is the header: " + decoded.getHeader());
                    System.out.println("This is the ID: " + decoded.getPeerId());
                    System.out.println("This is the zero bits length: " + decoded.getzerobitsLength());
                    handShake newShake = new handShake(currPeer.getpeerID());
                    byte[] handshakeBytes = newShake.encode();
                    out.writeObject(handshakeBytes);
                    out.flush();
                   
                    //TEST FOR SENDING IMAGE, WORKING 
                    // byte[] test = (byte[]) in.readObject();
                     //System.arraycopy(test, 0, fileData, 16384);
                    //fileData = 
                    // out.writeObject(test);
                    // String tempPath = "peer_1002/test.jpg";
                    // FileOutputStream fileOutputStream = new FileOutputStream(tempPath);
                    // fileOutputStream.write(test);
                    // fileOutputStream.close();

                    
				}
			
            } finally {
                System.out.println("test");
            }
        }
            catch(ClassNotFoundException classnot)
            {
					System.err.println("Data received in unknown format");
				}
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            
          
            } 

         }
   

         public void sendMessage(String msg)
         {
             try
             {
                 out.writeObject(msg);
                 out.flush();
                // System.out.println("Send message: " + msg + " to Client " + no);
             }
             catch(IOException ioException)
             {
                 ioException.printStackTrace();
             }
         }


         public static void loadFile(String folderName) {
            try {
                File folder = new File(folderName);
                File[] allFiles = folder.listFiles();
                File sendFile = allFiles[1];
               // System.out.println("Getting the name:" + sendFile.getName());
                if (sendFile == null) {
                    System.out.println("File not found.");
                }
                FileInputStream in = new FileInputStream(sendFile);
                fileData = new byte[(int) sendFile.length()];
                if(fileData.length == sendFile.length()){
                    System.out.println("im supposed to be here");

                }
                in.read(fileData);
                in.close();
                System.out.println("File loaded successfully for peer " + currPeer.getpeerID());
            } catch (IOException e) {
                System.err.println("Error loading file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    




        
    