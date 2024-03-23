import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    //Information to load into the config file
    static fileLoader configInfo = new fileLoader();
    HashMap<Integer, Peer> connectedPeers;
    HashMap<Integer, Thread> connectedThreads;
    Vector<Peer> activePeers = new Vector<Peer>();







    public static void main(String[] args) throws IOException{
        if(args.length == 0){
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


                
             
     

 
        }



       

        

    }

    public static void beginSearching()
    {

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
    
    

    public static void serverStart() throws IOException
    {
        //Right now this just listens for the whole time
        System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(currPeer.getpeerID());
        	try {
            		while(true) {
                        //hardcoded test values for now
                		new Handler(listener.accept(), 1001).start();
                        new Handler(listener.accept(), 1002).start();
                        new Handler(listener.accept(), 1003).start();
                        new Handler(listener.accept(), 1004).start();
                        new Handler(listener.accept(), 1005).start();

				System.out.println("Client "  + "1001" + " is connected!");
			
            			}
        	} finally {
            		listener.close();
        	} 

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
    
    
     
        
    }



