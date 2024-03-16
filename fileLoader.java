import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class fileLoader {
    int numNeighbors;
    int unchokeInterval;
    int optimisticInterval;
    String fileName;
    int fileSize;
    int pieceSize;
    //going to be creating functions here to load common.cfg and peerinfo.cfg



    public void loadCommon(){
        int index = 0;
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

    }




