import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class logWriter {
    private final int peerID;
    private final String logFilePath;


    public logWriter(int peerID) {
        this.peerID = peerID;
        this.logFilePath = "log_peer_" + peerID + ".log";
        File file = new File(logFilePath);
    }

    public synchronized void logConnection(int hostPeer, int clientPeer){
        String message = String.format("[%s]: Peer " + clientPeer + " makes a connection to Peer " + hostPeer, getcurrentTime());
        writeLog(message);
        System.out.println(message);

      //  System.out.println("file written successfully!");


    }

    public void preferredNeighbors(List<Integer> preferredNeighbors) {
        String neighborList = "";
        for (int i = 0; i < preferredNeighbors.size(); i++) {
        if (i > 0) {
            neighborList += ", ";
        }
        neighborList += preferredNeighbors.get(i).toString();
    }
        String logMessage = String.format("[%s]: Peer " +  peerID + " has the preferred neighbors" + neighborList, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }

    public void logUnchoke(int neighborID) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " is unchoked by " + neighborID, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }

    public void handShake(int neighborID) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " has received a handshake from " + neighborID, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }



    public void logoptimisticUnchoke(int neighborID) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " has the optimistically unchoked neighbor " + neighborID, getcurrentTime());
       System.out.println(logMessage);
        writeLog(logMessage);
    }

    public void logChoke(int neighborID) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " is choked by  " + neighborID, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }
    public void logHave(int neighborID, int pieceIndex) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " received the 'have' message from " + neighborID + " for the piece " + pieceIndex, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }

    public void logInterested(int interestedPeer) {
        String logMessage = String.format("[%s]: Peer " + peerID + " received the 'interested' message from " + interestedPeer, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }
    public void lognotInterested(int notinterestedPeer) {
        String logMessage = String.format("[%s]: Peer " + peerID + " received the 'not interested' message from " + notinterestedPeer, getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);
    }
    public void logDownload(int neighborID, int pieceIndex, int numPieces) {
        String logMessage = String.format("[%s]: Peer " + peerID +  " has downloaded the piece " + pieceIndex + " from " + neighborID + "." + " Now the number of pieces it has is " + numPieces, getcurrentTime());
       System.out.println(logMessage);
        writeLog(logMessage);
    }
    public void logFinished(){
        String logMessage = String.format("[%s]: Peer " + peerID +  " has downloaded the complete file. ", getcurrentTime());
        System.out.println(logMessage);
        writeLog(logMessage);

    }




    private String getcurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private void writeLog(String logMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.println(logMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
   

   



}
