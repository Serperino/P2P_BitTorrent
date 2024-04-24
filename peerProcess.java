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
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.BitSet;
import java.io.OutputStream;
import java.util.List;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


import java.util.Collections;
import java.util.Comparator;

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
    static String fileName;  
   // message typemessage;
    handShake handshake;
    static ServerSocket acceptingConnections;
    static byte[] fileData;
    //staitc list 
    //static RandomAccessFile writeFile;
    //Information to load into the config file
    //Data structures being used (WIP)
    static fileLoader configInfo = new fileLoader();
    //HashMap<Integer, Peer> connectedPeers;
    //HashMap<Integer, Thread> connectedThreads;
    Vector<Peer> activePeers = new Vector<Peer>();
    //track neighbors bitfield by bitfield msg and update by have msg.
    static HashMap<Integer, BitSet> peerBitfields = new HashMap<>();
    //update status interest or not
    static Timer timer = new Timer();
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static boolean interestShown;
    static List<Integer> unchokedPeers = new ArrayList<>();
    static List<Integer> peers = new ArrayList<>();
    static logWriter peerLog = null;







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
              peerLog = new logWriter(currPeer.getpeerID());
              
             if(currPeer.hasFile() == 1){
                currPeer.bitField.set(0, fileLoader.gettotalPieces());
                String folderName = "peer_" + currPeer.getpeerID();
                loadFile(folderName);
                fileName = "peer_" + currPeer.getpeerID() + "/" + configInfo.getfileName();
              

             }
             else {
                
                fileData = new byte[configInfo.getfileSize()];
                fileName = "peer_" + currPeer.getpeerID() + "/" + configInfo.getfileName();
                //System.out.println(fileName);
             }
            beginListening();
             beginSearching();

        }

    }
    

    //Uses some of the client code 
    
    public static void hostOpen() throws UnknownHostException, IOException
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
            //System.out.println("i break here?" + peerID);
            //requestSocket = new Socket("localhost", peerID);
         //   System.out.println(address);
           // System.out.println("port number:" + workingPeer.getportNumber());
            requestSocket = new Socket(address, workingPeer.getportNumber());
          //  System.out.println("socket at" + workingPeer.getportNumber());
            new Handler(requestSocket, workingPeer.getportNumber()).start();
            System.out.println("Connected to " + peerID);
          //  System.out.println("CURRENT PEER ID" + currPeer.getpeerID());

            peerLog.logConnection((int)peerID, currPeer.getpeerID());
          

            }
    }
    }
    public static void beginSearching()
    {

       Runnable hostOpen = () -> {
           try {
            hostOpen();
           } catch (IOException e) {
               e.printStackTrace();
           }
       };

       Thread listeningThread = new Thread(hostOpen);
       listeningThread.start();

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
              //  e.printStackTrace();
            }
        };

        Thread listeningThread = new Thread(serverStart);
        listeningThread.start();

    }


    
    //Uses some of the server code in the example
    public static void serverStart() throws IOException
    {
        acceptingConnections = new ServerSocket(currPeer.getportNumber());
        System.out.println("ServerSocket opened at ID: " + currPeer.getportNumber());
        System.out.println("The server is running."); 
        //Going to have to get the start of a map to determine how many connections have to be made as the rubric says
        //this will happen later though.
       // int connectionstobeMade = configInfo.getPeerMap().start
        for (Integer peerID : configInfo.getpeerMap().keySet()) {
            if(currPeer.getpeerID() == peerID){  
            }
            else{
                if(!peers.contains(peerID)){
                    peers.add(peerID);
                   }
                Peer serverstartPeer = configInfo.getpeerMap().get(peerID);
                new Handler(acceptingConnections.accept(), serverstartPeer.getportNumber()).start();
                peerLog.logConnection((int)peerID, currPeer.getpeerID());
				System.out.println("Client "  + (peerID)+ " is connected!");
    
            }
        }
    
    }






     private static class Handler extends Thread {
        private String message;    //message received from the client
		private String MESSAGE;    //uppercase message send to the client
		private Socket connection;
        private  ObjectInputStream in;	//stream read from the socket
        private   ObjectOutputStream out;    //stream write to the socket
		private int no;		//The index number of the client
        private byte[] payload;
        byte[] fileInfo;
        int downloadsatZero = 0;
        int totalDownloads = 0;
         BitSet receivedbitField;
        static boolean chokeSent = false;
        int test = 0;
        boolean unchoked = false;
        private int PeerID;
        int pieceOffset;
        boolean bitfieldSent = false;
        int fileOffset;
        static int piecesHeld = 0;
        boolean hasFile;
        int iterations = 0;
        int total = 0;
        static int  messagesReceived = 0;
        static boolean filestoExchange = true;
        Timer optTimer = new Timer();
        static int peersDone = 0;
        static boolean timerRunning = false;
        static HashMap<Integer, Boolean> peerInterestStatus = new HashMap<>();  
        static HashMap<Integer, Integer> downloadRates = new HashMap<>();
        static  List<Integer> interestedPeers = new ArrayList<>();
        static List<Integer> chosenforDownload = new ArrayList<>();
        static List<Integer> tobeoptimisticallyUnchoked = new ArrayList<>();
        volatile HashMap<Integer, Integer> highestdownloadRates = new HashMap<>();
        Timer sendChoke = new Timer();
        boolean everyonehasEverything = false;
        //ArrayList<Integer> avaliablePieces = new ArrayList<>();



        int downloadRate;


        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

            
        public void run()
         {
 		try
        {

          //  receivedbitField = new BitSet(configInfo.gettotalPieces());
            //System.out.println(connection);
            RandomAccessFile fileWrite = new RandomAccessFile(fileName, "rw"); 
			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());
            sendHandshake();
            
            
            if(currPeer.hasFile() == 1 || !currPeer.getbitField().isEmpty()){
                bitfieldSent = true;
                hasFile = true;
              //  byte[] bitFieldconvert = currPeer.getbitField().toByteArray();
                Message message = new Message(MessageType.BITFIELD, currPeer.getbitField().toByteArray());
                byte[] toSend = message.encode();
                out.writeObject(toSend);
                out.flush(); // Flush the stream to ensure all data is sent
            
            }
           
            
			try
            {   
               

                byte[] incomingHandshake = (byte[]) in.readObject();
                handShake decoded = handShake.decode(incomingHandshake);
                downloadRates.put(decoded.getPeerId(), downloadRate);
              

                PeerID=decoded.getPeerId();
                peerLog.handShake(PeerID);
              
     

                //FOR CALCULATING PEERS
                 if(timerRunning == false){
                  
                     timerRunning = true;
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            
                          
                           
                            
                            for (Map.Entry<Integer, BitSet> entry : peerBitfields.entrySet()) {
                                int id = entry.getKey(); // Get the ID
                                BitSet bitSet = entry.getValue(); // Get the bitset
                                for(int i = 0; i < configInfo.gettotalPieces(); i++){
                                if( currPeer.getbitField().get(i) && bitSet.get(i)){
                                    everyonehasEverything = true;
                                }
                                else{
                             
                                    everyonehasEverything = false;
                                    break;
                                }
        
                            }        
                        }
                        if(everyonehasEverything == true && iterations != 2){
                            iterations++;
                          //  everyonehasEverything = false;
                          everyonehasEverything = false;
                        }
                    
                              
                    if(everyonehasEverything == true && iterations == 2 || downloadsatZero >= 5 && hasFile){
                        try {
                            System.out.println("All users have all files, Disconnecting...");
                            sleep(8000);
                            if(currPeer.hasFile() == 1){
                                int lastIndex = peers.size() - 1;
                                peers.remove(lastIndex);
                            }
                            for(int peer : peers){
                                System.out.println("Disconnected from: " + peer);
                            }
                            System.exit(0);
                         
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                           // e.printStackTrace();
                        }
                       // filestoExchange = false;
                    }

                            interestedPeers = selectPreferredNeighbors();
                            if(!interestedPeers.isEmpty()){
                                peerLog.preferredNeighbors(interestedPeers);

                            }
                          
                            
                         
                                if(interestedPeers.size() >= configInfo.getnumNeighbors()){
                                    if(currPeer.hasFile() == 1){
    
                                    }
                                    interestShown = true;
                                    chokeSent = true;
    
    
                                }
                            
                         
                        
                               // totalDownloads = 0;
                               if(!downloadRates.isEmpty()){
                            for (Map.Entry<Integer, Integer> entry : downloadRates.entrySet()) {
                                int peerId = entry.getKey();
                                int rate = entry.getValue();
                                chosenforDownload.add(peerId);
                                totalDownloads += rate;

                               // System.out.println("Peer ID: " + peerId + ", Download Rate: " + rate);
                                
                            }
                        }
                            if(totalDownloads == 0){
                                downloadsatZero += 1;
                            }
                            else{
                                downloadsatZero = 0;
                            }
                            for (Map.Entry<Integer, Integer> entry : downloadRates.entrySet()) {
                                int peerId = entry.getKey();
                                chosenforDownload.add(peerId);
                                //entry.setValue(0);

                            }

    
    
    
    
                        }
                    }, 0, configInfo.getunchokeInterval() * 1000);


                    
                    optTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                         
                           
                            boolean foundPeer = false;
                            if(!interestedPeers.isEmpty()){
                                for (Integer peers : interestedPeers) {
                                    if (!chosenforDownload.contains(peers)) {
                                        tobeoptimisticallyUnchoked.add(peers);
                                        foundPeer = true;
                                    }
                                }
                            }
                         
                            if(foundPeer){
                                Random optunchoke = new Random();
                                int randomIndex = optunchoke.nextInt(tobeoptimisticallyUnchoked.size());
                                int randomElement = tobeoptimisticallyUnchoked.get(randomIndex);
                                peerLog.logoptimisticUnchoke(PeerID);
                                sendUnchoke();
                                
                                
                            }
                            else{
                                if(interestedPeers.size() != 0 && !interestedPeers.isEmpty() & tobeoptimisticallyUnchoked.size() != 0){
                                    Random optunchoke = new Random();
                                    int randomIndex = optunchoke.nextInt(interestedPeers.size());
                                    int randomElement = interestedPeers.get(randomIndex);
                                    int tobeLogged = tobeoptimisticallyUnchoked.get(randomElement);
                                    int toUnchoke = tobeoptimisticallyUnchoked.get(randomElement);
                                    peerLog.logoptimisticUnchoke(PeerID);
                                    sendUnchoke();
                                }
                               

                            }
                           
                            
                        }
                    }, 0, configInfo.getoptimisticInterval()*1000);
                }

                
                sendChoke.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
               
                        
                    
                       
                         
                            for (int i = 0; i < interestedPeers.size(); i++) {
                                Integer peer = interestedPeers.get(i);
                                if(!interestedPeers.isEmpty()){
                                if(interestedPeers.contains(PeerID)){
                                     interestShown = false;
                                     unchokedPeers.add(PeerID);
            
                                     sendUnchoke();
                                     break;
                                 }
                                 else {
                                  if(interestedPeers.size() > configInfo.getnumNeighbors()){
                                    sendChoke();
                                    break;
                                  }
                                    
                                 }
                                }
                                  
                            }
                        
                        
                    
                          




                    }
                }, 0, configInfo.getunchokeInterval() * 1000);
               
               
                
               
                Timer updateInfo = new Timer();
                updateInfo.scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run(){
                        Message updateMessage = new Message(MessageType.BITFIELD, currPeer.getbitField().toByteArray());
                        byte[] update = updateMessage.encode();
                        try {
                           out.writeObject(update);
                       } catch (IOException e) {
                           // TODO Auto-generated catch block
                         //  e.printStackTrace();
                       }
                        try {
                           out.flush();
                       } catch (IOException e) {
                           // TODO Auto-generated catch block
                         // e.printStackTrace();
                       }

                }
            },0,5000);
               

                
				while(filestoExchange)
				{
                   
                  



                   

                 
              
                byte[] incomingMessage = (byte[]) in.readObject();
                Message decodedMessage = Message.decode(incomingMessage); // Create an instance and then call decode
                payload=decodedMessage.getPayload();
              
               if(messagesReceived == 10){
               
            }
                    switch(decodedMessage.getType()){
                        case CHOKE:
                        peerLog.logChoke(PeerID);
                        unchoked = false;
                        break;

                        case UNCHOKE:
                        peerLog.logUnchoke(PeerID);
                        if(unchoked){
                            continue;
                        }
                        else { 
                            unchoked = true;
                        if(!hasFile){
                         ArrayList<Integer> unchokeavailablePieces = new ArrayList<>();
                         for(int i = 0; i < receivedbitField.length(); i++){
                             if(receivedbitField.get(i) && !currPeer.getbitField().get(i)){
                                
                                unchokeavailablePieces.add(i);
                            
                             }
        

                         }
                         if(!unchokeavailablePieces.isEmpty()){
                            Random random = new Random();
                            int randomunchokeIndex = random.nextInt(unchokeavailablePieces.size());
                            int randomPiece = unchokeavailablePieces.get(randomunchokeIndex);
                         
                             requestMessage(randomPiece);
                             downloadRate++;
                             break;
                         }
                      
                        }
                        break;
                    }
                        case INTERESTED:
                        messagesReceived++;

                        updateInterestStatus(PeerID, true);
                        peerLog.logInterested(PeerID);
                        interestShown = true;
                        break;
                    
                        case NOT_INTERESTED:
                        messagesReceived++;

                        peerLog.lognotInterested(PeerID);
                        updateInterestStatus(PeerID, false);
                        break;
                        
                        case BITFIELD:
                      //  System.out.println("get bitfiled msg");
                        messagesReceived++;

                        receivedbitField = BitSet.valueOf(decodedMessage.getPayload());
                        peerBitfields.put(PeerID, receivedbitField);
                    

                        
                        
                        if(isInterestedInPeer(PeerID)){
                            sendInterestedMessage();
                        }
                        else{
                            sendNotInterestedMessage();
                        }
                    
                        break;
                    
                    


                        case HAVE:
                            ByteBuffer haveBuffer = ByteBuffer.wrap(payload);
                            pieceOffset = haveBuffer.getInt();
                       
                            BitSet haveBits = BitSet.valueOf(decodedMessage.getPayload());
                           
                            peerBitfields.put(PeerID, haveBits);
                            payload = decodedMessage.getPayload();
                        
                             receivedbitField.set(pieceOffset);
                            peerLog.logHave(PeerID, pieceOffset);
                            if(!hasFile){
                                if(isInterestedInPeer(PeerID)){
                                    sendInterestedMessage();
                                }
                                else{
                                    sendNotInterestedMessage();
                                }
                            }
                           
                      
                        peerLog.logHave(PeerID, pieceOffset);
                        messagesReceived++;

                        break;

                        case REQUEST:
                        ByteBuffer buffer = ByteBuffer.wrap(decodedMessage.getPayload());
                        int index = buffer.getInt();
                        int offset = index * configInfo.getpieceSize();
                        byte[] requestedPiece = new byte[configInfo.getpieceSize()];

                        if(index == (configInfo.getfileSize()/configInfo.getpieceSize())){
                            int fileLength = configInfo.getfileSize() - offset;
                            requestedPiece = new byte[fileLength];
                       
                            System.arraycopy(fileData, offset, requestedPiece, 0, fileLength);                            //https://www.geeksforgeeks.org/system-arraycopy-in-java/

                            pieceMessage(index, requestedPiece);
                            downloadRates.put(PeerID, downloadRate++);
                            messagesReceived++;

                            break;
                        }
                        
                     
                        System.arraycopy(fileData, offset, requestedPiece, 0, configInfo.getpieceSize()); //https://www.geeksforgeeks.org/system-arraycopy-in-java/
                        pieceMessage(index, requestedPiece);
                        downloadRates.put(PeerID, downloadRate++);
                        break;


                        
                        case PIECE:
                        int forloopSize = 0;
                        Vector<Integer> availablePieces = new Vector<>();
                        payload = decodedMessage.getPayload();
                        ByteBuffer pieceBuffer = ByteBuffer.wrap(payload);
                         pieceOffset = pieceBuffer.getInt(); // Extract the offset from the first 4 bytes
                    
                         fileOffset = pieceOffset * configInfo.getpieceSize();
                     
                         if((pieceOffset+1) == configInfo.gettotalPieces()){
                            int bytes = configInfo.getfileSize() - fileOffset;
                            System.arraycopy(payload, 4, fileData, fileOffset, bytes); 
                            fileWrite.seek(fileOffset);
                            fileWrite.write(payload, 4, configInfo.getpieceSize());
                            piecesHeld++;
                            peerLog.logDownload(PeerID, pieceOffset, piecesHeld);
                            if(configInfo.gettotalPieces() == piecesHeld){
                                peerLog.logFinished();

                            }
                            currPeer.getbitField().set(pieceOffset);
                         }
                         else{
                            System.arraycopy(payload, 4, fileData, fileOffset, configInfo.getpieceSize()); 
                            fileWrite.seek(fileOffset);
                            fileWrite.write(payload, 4, configInfo.getpieceSize());
                            piecesHeld++;
                            peerLog.logDownload(PeerID, pieceOffset, piecesHeld);
                            if(configInfo.gettotalPieces() == piecesHeld){
                                peerLog.logFinished();

                            }
                            currPeer.getbitField().set(pieceOffset);
                            if(bitfieldSent){
                                sendhaveMessage(pieceOffset);
                            }
                            else{
                                Message message = new Message(MessageType.BITFIELD, currPeer.getbitField().toByteArray());
                                byte[] toSend = message.encode();
                                out.writeObject(toSend);
                                out.flush();
                                bitfieldSent = true; 
                            }
                         
                         }
                  

                        availablePieces.clear();
                        availablePieces = new Vector<>();
                        for(int i = 0; i < configInfo.gettotalPieces(); i++){
                            if( !currPeer.getbitField().get(i) && receivedbitField.get(i)){
                                
                                
                                availablePieces.add(i);

                            }
                            else{
                            }
                            forloopSize++;

                           }


                               if(!availablePieces.isEmpty()){
                              
                                Random random = new Random();
                                int randomIndex = random.nextInt(availablePieces.size());
                                int randomPiece = availablePieces.get(randomIndex);
                                if(receivedbitField.get(randomPiece) && currPeer.getbitField().get(randomPiece)){
                                    break;
                                }
                                 requestMessage(randomPiece);
                                 downloadRate++;
                                 total++;
                                 messagesReceived++;

                                 break;
 
                               }
                               else {
                                sendNotInterestedMessage();
                               }
                           
                               messagesReceived++;


                
                               break;


                         

                    }
                  
                    
                   
                    
				}
			} catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
               // e.printStackTrace();
            }// catch (InterruptedException e) {
                // TODO Auto-generated catch block
               // e.printStackTrace();
         //   } 
           finally
            {
              //Close connections
			//try
           // {
				//in.close();
				//out.close();
				//connection.close();
			//}
			//catch(IOException ioException)
           {
                
				//System.out.println("Disconnect with Client " + no);
			}
        }
			
		} 
		catch(IOException ioException)
        {
			//System.out.println("Disconnect with Client " + no);
        } 
	


    


	}

    public   void sendHandshake(){
        handShake newShake = new handShake(currPeer.getpeerID());
     //   System.out.println(currPeer.getpeerID());
        byte[] handshakeBytes = newShake.encode();
        sendMessage(handshakeBytes);

     } 
     public   void sendUnchoke(){
        byte[] dummy = new byte[20];
        Message message = new Message(MessageType.UNCHOKE,dummy);
        byte[] encodedMessage = message.encode();
        sendMessage(encodedMessage);
     } 
     public   void sendChoke(){
        byte[] dummy = new byte[20];
        Message message = new Message(MessageType.CHOKE,dummy);
        byte[] encodedMessage = message.encode();
        sendMessage(encodedMessage);
     } 


     public void updateInterestStatus(int PeerID, boolean isInterested) {
       // System.out.println("Updating interest status for" + PeerID);
        peerInterestStatus.put(PeerID, isInterested);
    }
    public boolean isPeerLiked(int PeerID) {
       // System.out.println(PeerID + "IN ISPEERLIKED");
        return peerInterestStatus.getOrDefault(PeerID, false);
    }
    public  void sendInterestedMessage(){
        byte[] dummy = new byte[20];
        Message message = new Message(MessageType.INTERESTED,dummy);
        byte[] encodedMessage = message.encode();
        sendMessage(encodedMessage);
        updateInterestStatus(PeerID, true);


     } public   void sendNotInterestedMessage(){
        byte[] dummy = new byte[20];
        Message message = new Message(MessageType.NOT_INTERESTED,dummy);
        byte[] encodedMessage = message.encode();
        sendMessage(encodedMessage);
        updateInterestStatus(PeerID, false);

     }
     public   void sendhaveMessage(int offset){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
     //   System.out.println("SENDING" + offset);
        buffer.putInt(offset);
        byte[] payload = buffer.array();
        Message message = new Message(MessageType.HAVE,payload);
        byte[] encodedMessage = message.encode();
        sendMessage(encodedMessage);

     }
     
    public boolean isInterestedInPeer(int peerId) {
        BitSet theirBitfield = peerBitfields.get(peerId);
        BitSet ourBitfield = currPeer.getbitField();  
        BitSet interestSet = (BitSet) theirBitfield.clone();
        interestSet.andNot(ourBitfield);
        return !interestSet.isEmpty();
    }
    public    void sendMessage(byte[] msg){
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    public  void requestMessage(int i ){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);
        byte[] payload = buffer.array();
        Message message = new Message(MessageType.REQUEST, payload);
        byte[] toSend = message.encode();
        sendMessage(toSend);
    
     }

       public  void pieceMessage(int i, byte[] pieceRequested ){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + configInfo.getpieceSize());
        buffer.putInt(i);
        buffer.put(pieceRequested);
        byte[] payload = buffer.array();
        Message message = new Message(MessageType.PIECE, payload);
        byte[] toSend = message.encode();
        sendMessage(toSend);
    
     }

     public List<Integer> selectPreferredNeighbors() {
        List<Integer> preferredNeighbors = new ArrayList<>();
        for (Integer peerId : peerInterestStatus.keySet()) {
            if (isPeerLiked(peerId)) {
                preferredNeighbors.add(peerId);
            }
        }
        return preferredNeighbors;
    }
    
    


   
    }


   



       
         public static void loadFile(String folderName) {
            try {
                File folder = new File(folderName);
                File[] allFiles = folder.listFiles();
                File sendFile = allFiles[0];
               // System.out.println("Getting the name:" + sendFile.getName());
                if (sendFile == null) {
                    System.out.println("File not found.");
                }
                FileInputStream in = new FileInputStream(sendFile);
                fileData = new byte[(int) sendFile.length()];
                if(fileData.length == sendFile.length()){

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

    