import java.net.ServerSocket;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Peer {
    //
    private int peerID;
    private String hostName;
    private String port;
    private int hasFile;
    private fileLoader info;
    private ServerSocket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    Vector<Peer> activePeers = new Vector<Peer>();


   // private fileLoader;
    //Need to add bitfield here
    //also need a peerlist so that these clients can then connect to the servers that we already have listed
    //mainly here for testing, but useful
    @Override
    public String toString() 
    { 
        return "Peer{" +
                "peerID=" + peerID +
                ", hostName='" + hostName + '\'' +
                ", port='" + port + '\'' +
                ", hasFile=" + hasFile +
                '}';
    }
    public Peer(int peerID, String hostName, String port, int hasFile)
    {
        this.peerID = peerID;
        this.hostName = hostName;
        this.port = port;
        this.hasFile = hasFile;
        //only thing missing here is way to store file that the peer has, but for now i'm ignoring that.
    }


    //GETTER FUNCTIONS:
    public int getpeerID()
    {
        return peerID;
    }


    public String gethostName()
    {
        return hostName;
    }



    public String getportNumber()
    {
        return port;
    }


    public int hasFile()
    {
        return hasFile;
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket socket)
    {
        this.serverSocket = socket;
    }

    
    

   


}
