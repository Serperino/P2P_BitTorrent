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
    private static int totalPieces;
    Message message;
    handShake handshake;
    private HashMap<Integer, Peer> peersinNetwork;
    
    
    public fileLoader()
    {
        this.peersinNetwork = new HashMap<>();
    }
    //going to be creating functions here to load common.cfg and peerinfo.cfg



    public void loadCommon()
    {
       // int index = 0;
       //Always will be reading from this file
        File commonCFG = new File ("Common.cfg");


        try (Scanner myScanner = new Scanner(commonCFG)) 
        {
            while (myScanner.hasNextLine()){
                String fileData = myScanner.nextLine();
                String[] fileSplit = fileData.split(" ");
                //Filesplit must be 2 as it has to have a label and the content, if its more or less the format is invalid
                if(fileSplit.length !=2)
                {

                    throw new IllegalArgumentException("Invalid format in the .cfg file");
                }
                String label = fileSplit[0];
                String value = fileSplit[1];


                //Uses the label to read what info is being read in from cfg file
                switch(label)
                {
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
        } catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        int pieceNumber = (fileSize/pieceSize);
         totalPieces = (int) Math.ceil((double) fileSize/pieceSize);
        
        System.out.println("these are the totalpieces: " + totalPieces);

        }

    public void loadpeerInfo()
    {
        //Always will be reading from this file
        File peerInfo = new File ("PeerInfo.cfg");//https://stackoverflow.com/questions/13405822/using-bufferedreader-readline-in-a-while-loop-properly
        try (BufferedReader br = new BufferedReader(new FileReader(peerInfo))) 
        {
            String line;
            try 
            {
                while((line = br.readLine()) != null)
                { 
                    String[] parts = line.split(" ");
                    //Peer info is in the form of 4 parts, so if it isnt 4 parts, the format is invalid
                    if(parts.length == 4)
                    {
                        int peerID = Integer.parseInt(parts[0]);
                        String hostName = parts[1];
                        String port = parts[2];
                        int hasFile = Integer.parseInt(parts[3]);
                        this.peersinNetwork.put(peerID, new Peer(peerID, hostName, port, hasFile, totalPieces));

                    }

                }
            } catch (IOException e) 
            {
                e.printStackTrace();
                
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        }

    }

    
    public HashMap<Integer, Peer> getpeerMap()
    {
        return peersinNetwork;

    }




    public int getnumNeighbors()
    {
        return numNeighbors;
    }



    public void setnumNeighbors(int numNeighbors)
    {
        this.numNeighbors = numNeighbors;
    }
    



    public int getunchokeInterval()
    {
        return unchokeInterval;
    }



    public void setunchokeInterval(int unchokeInterval)
    {
        this.unchokeInterval = unchokeInterval;
    }



    public String getfileName()
    {
        return fileName;
    }





    public void setfileName (String fileName)
    {
        this.fileName = fileName;
    }





    public int getfileSize()
    {
        return fileSize;
    }





    public void setfileSize(int fileSize)
    {
        this.fileSize = fileSize;
    }





    public int getpieceSize()
    {
        return pieceSize;
    }





    public void setpieceSize(int pieceSize)
    {
        this.pieceSize = pieceSize;
    }






    public int getoptimisticInterval()
    {
        return optimisticInterval;
    }

    public static  int gettotalPieces()
    {
        return totalPieces;
    }









    public void setoptimisticInterval(int optimisticInterval)
    {
        this.optimisticInterval = optimisticInterval;
    }



    }




