public class test {
    //just testing loading files in here.
    public static void main(String[] args) {
        fileLoader loader = new fileLoader();

        loader.loadCommon();

        System.out.println("Number of Preferred Neighbors: " + loader.numNeighbors);
        System.out.println("Unchoking Interval: " + loader.unchokeInterval);
        System.out.println("Optimistic Unchoking Interval: " + loader.optimisticInterval);
        System.out.println("File Name: " + loader.fileName);
        System.out.println("File Size: " + loader.fileSize);
        System.out.println("Piece Size: " + loader.pieceSize);
    }
}


