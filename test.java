public class test {
    //just testing loading files in here.
    public static void main(String[] args) {
        fileLoader loader = new fileLoader();

        loader.loadCommon();
        loader.loadpeerInfo();

        System.out.println("Number of Preferred Neighbors: " + loader.getnumNeighbors());
        System.out.println("Unchoking Interval: " + loader.getunchokeInterval());
        System.out.println("Optimistic Unchoking Interval: " + loader.);
        System.out.println("File Name: " + loader.fileName);
        System.out.println("File Size: " + loader.fileSize);
        System.out.println("Piece Size: " + loader.pieceSize);

        for (Integer peerID : loader.peersinNetwork.keySet()) {
            Peer peer = loader.peersinNetwork.get(peerID);
            System.out.println("Peer ID: " + peerID + ", Peer details: " + peer);
        }
    }
}


