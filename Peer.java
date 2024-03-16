import java.net.ServerSocket;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Peer {
    private String peerID;
    private String hostName;
    private String port;
    private boolean hasFile;
    private fileLoader;
    //Need to add bitfield here
    //also need a peerlist so that these clients can then connect to the servers that we already have listed

    public Peer(String peerID, String hostName, String port, boolean hasFile){
        this.peerID = peerID;
        this.hostName = hostName;
        this.port = port;
        this.hasFile = hasFile;
        //only thing missing here is way to store file that the peer has, but for now i'm ignoring that.
    }

    public void serverStart(){ //every client will be acting as a server, so we open up on that port
        //Idea right now is every peer makes a server, and then connects to a list of servers that we make
        	try {
                ServerSocket listener = new ServerSocket(Integer.parseInt(this.port));
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


}
