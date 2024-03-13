public class handShake {
    private String header;
    private String peerID;
    private int totalLength;



    public handShake(String peerID){
        this.peerID = peerID;
        this.header = "P2PFILESHARINGPROJ";
        totalLength = this.header.length() + 14;

    }
}
