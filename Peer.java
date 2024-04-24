import java.net.ServerSocket;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.BitSet;

public class Peer {
    //All attributes a peer has
    private int peerID;
    private String hostName;
    private int port;
    private int hasFile;
    private fileLoader info;
    private ServerSocket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
 //  private Server server;
    private Thread listenThread;
    //public byte[] bitField;
    static public BitSet bitField;
    Vector<Peer> activeNeighbors = new Vector<Peer>();


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
    public Peer(int peerID, String hostName, int port, int hasFile, int totalPieces)
    {
        this.peerID = peerID;
        this.hostName = hostName;
        this.port = port;
        this.hasFile = hasFile;
        this.bitField = new BitSet(totalPieces);
        if(hasFile == 1){

            
           

        }
    }


    //GETTER FUNCTIONS:


    public Thread gethostThread()
    {
        return listenThread;
    }
    public void sethostThread(Thread thread)
    {
        this.listenThread = thread;
    }

    // public Server getServer(){
    //     return server;
    // }

    public int getpeerID()
    {
        return peerID;
    }


    public String gethostName()
    {
        return hostName;
    }



    public int getportNumber()
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

    // public void  setServer(Server server)
    // {
    //     this.server = server;
    // }

    public BitSet getbitField()
    {
        return bitField;
    }

    public void setBitField(BitSet bitField){
        this.bitField = bitField;
    }

    

   


}
