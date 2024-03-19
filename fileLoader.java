import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;

public class fileLoader {
    private int numNeighbors;
    private int unchokeInterval;
    private int optimisticInterval;
    private String fileName;
    private int fileSize;
    private int pieceSize;
    private HashMap<Integer, Peer> peersinNetwork;
    
    public fileLoader(){
        this.peersinNetwork = new HashMap<>();
    }
    //going to be creating functions here to load common.cfg and peerinfo.cfg



    public void loadCommon(){
       // int index = 0;
        File commonCFG = new File ("Common.cfg");


        try (Scanner myScanner = new Scanner(commonCFG)) {
            while (myScanner.hasNextLine()){
                String fileData = myScanner.nextLine();
                String[] fileSplit = fileData.split(" ");
                if(fileSplit.length !=2){

                    throw new IllegalArgumentException("Invalid format in the .cfg file");
                }
                String label = fileSplit[0];
                String value = fileSplit[1];

                switch(label){
                    case "NumberofPreferredNeighbors":
                        this.numNeighbors = Integer.parseInt(value);
                        break;
                    case "UnchokingInterval":
                        this.unchokeInterval = Integer.parseInt(value);
                        break;
                    case "OptimisticUnchokingInterval":
                        this.optimisticInterval = Integer.parseInt(value);
                        break;
                    case "FileName":
                        this.fileName = value;
                        break;
                    case "FileSize":
                        this.fileSize = Integer.parseInt(value);
                        break;
                    case "PieceSize":
                        this.pieceSize = Integer.parseInt(value);
                        break;
                    //default:
                   // throw new IllegalArgumentException("Invalid format in the .cfg file");
                   // for some reason this causes it to crash


                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
      

        }

    public void loadpeerInfo(){
        File peerInfo = new File ("PeerInfo.cfg");//https://stackoverflow.com/questions/13405822/using-bufferedreader-readline-in-a-while-loop-properly
        try (BufferedReader br = new BufferedReader(new FileReader(peerInfo))) {
            String line;
            try {
                while((line = br.readLine()) != null){ 
                    String[] parts = line.split(" ");
                    if(parts.length == 4){
                        int peerID = Integer.parseInt(parts[0]);
                        String hostName = parts[1];
                        String port = parts[2];
                        int hasFile = Integer.parseInt(parts[3]);
                        this.peersinNetwork.put(peerID, new Peer(peerID, hostName, port, hasFile));

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    public HashMap<Integer, Peer> getpeerMap(){
        return peersinNetwork;

    }

    public int getnumNeighbors(){
        return numNeighbors;
    }



    public int getunchokeInterval(){
        return unchokeInterval;
    }



    public String fileName(){
        return fileName;
    }



    public int fileSize(){
        return fileSize;
    }



    public int pieceSize(){
        return pieceSize;
    }



    }




