import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;

//socket information:
//https://www.oracle.com/java/technologies/jpl2-socket-communication.html
public class peerProcess {
   //The peer partaking in the process
    static Peer currPeer;
    private ServerSocket serverSocket;

    //Information to load into the config file
    static fileLoader configInfo = new fileLoader();
    HashMap<Integer, Peer> connectedPeers;







    public static void main(String[] args) throws IOException{
        if(args.length == 0){
            System.out.println("Invalid arguments");
            
        }
        else{
            fileLoader loader = new fileLoader();
            //Loads information from common.cfg and peer.info
            configInfo.loadCommon();
            //This specifically contains the map of the peers
            configInfo.loadpeerInfo();
             Integer inputID = Integer.parseInt(args[0]);
             peerVerifier(inputID);
             System.out.println("peer ID: " + currPeer.getpeerID());
             System.out.println("hostname: " + currPeer.gethostName());
             serverStart();

                
             
     

 
        }
       

        

    }

    public static void peerVerifier(Integer peerID) {
        boolean peerFound = false;
        for(Integer inputID: configInfo.getpeerMap().keySet()){
            if(inputID.equals(peerID)){
              System.out.println("Running peer: " + peerID);
              peerFound = true;
              currPeer = configInfo.getpeerMap().get(peerID);
            }
        }
        if(!peerFound){
            System.out.println("Peer not found :[");
        }
    }

    public static void serverStart() throws IOException{
        ServerSocket socket = new ServerSocket(currPeer.getpeerID());
        currPeer.setServerSocket(socket);
        Thread incomingConnections = new Thread();
        incomingConnections.start();
        try {

        } catch (Exception e) {
            System.out.println("idk what happened");
        }
        
     
        
    }
}


