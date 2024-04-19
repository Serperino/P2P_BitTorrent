public class test {
    //just testing loading files in here.
    public static void main(String[] args) {
        fileLoader loader = new fileLoader();

        loader.loadCommon();
        loader.loadpeerInfo();

        System.out.println("Number of Preferred Neighbors: " + loader.getnumNeighbors());
        System.out.println("Unchoking Interval: " + loader.getunchokeInterval());
        System.out.println("Optimistic Unchoking Interval: " + loader.getoptimisticInterval());
        System.out.println("File Name: " + loader.getfileName());
        System.out.println("File Size: " + loader.getfileSize());
       // System.out.println("Piece Size: " + loader.pieceSize());

        for (Integer peerID : loader.getpeerMap().keySet()) {
            Peer peer = loader.getpeerMap().get(peerID);
            System.out.println("Peer ID: " + peerID + ", Peer details: " + peer);
        }
    }
}


