import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;



public class peerProcess {
   //The peer partaking in the process
    static Peer currPeer;
    //Information to load into the config file
    static fileLoader configInfo = new fileLoader();
    HashMap<Integer, Peer> connectedPeers;







    public static void main(String[] args){
        if(args.length == 0){
            System.out.println("Invalid arguments");
            
        }
        else{
            fileLoader loader = new fileLoader();
            //Loads information from common.cfg and peer.info
            System.out.println("WHY DONT U  WORK");
            configInfo.loadCommon();
            System.out.println("WHY DONT U  WORK");
            //This specifically contains the map of the peers
            configInfo.loadpeerInfo();
             Integer inputID = Integer.parseInt(args[0]);
             peerVerifier(inputID);
             System.out.println("peer ID: " + currPeer.getpeerID());
             System.out.println("hostname: " + currPeer.gethostName());
             
     

 
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
}


