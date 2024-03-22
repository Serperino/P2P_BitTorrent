import java.nio.ByteBuffer;
import java.util.Arrays;

public class handShake {
    private static String header;
    private int peerID;
    private static int ZERO_BITS_LENGTH = 10;
    private static int PEER_ID_SIZE = 4;
    private static int length = 14;



    public handShake(int peerID)
    {
        this.peerID = peerID;
        handShake.header = "P2PFILESHARINGPROJ";
        handShake.length=header.length()+ZERO_BITS_LENGTH+PEER_ID_SIZE;

    }
    public byte[] encode() 
    {
        ByteBuffer buffer = ByteBuffer.allocate(header.length() + ZERO_BITS_LENGTH + PEER_ID_SIZE);
        buffer.put(header.getBytes());
        buffer.put(new byte[ZERO_BITS_LENGTH]); 
        buffer.putInt(peerID);
        return buffer.array();
    }
    public static handShake decode(byte[] handshakeBytes) 
    {
        if (handshakeBytes.length != header.length() + ZERO_BITS_LENGTH + PEER_ID_SIZE) 
        {
            throw new IllegalArgumentException("Invalid handshake message length");
        }

        byte[] headerBytes = Arrays.copyOfRange(handshakeBytes, 0, header.length());
        String header = new String(headerBytes);

        if (!header.equals(header)) 
        {
            throw new IllegalArgumentException("Invalid handshake header");
        }
        byte[] zeroBytes = Arrays.copyOfRange(handshakeBytes, header.length(), header.length()+10);
        boolean allZero = true;
        for (byte b : zeroBytes) 
        {
            if (b != 0) {
                allZero = false;
                break;
                }
        }

        if (!allZero) {

            throw new IllegalArgumentException("Zero bytes section is invalid");
        }

        int peerId = ByteBuffer.wrap(handshakeBytes, header.length() + ZERO_BITS_LENGTH, PEER_ID_SIZE).getInt();

        return new handShake(peerId);
    }
    //GETTER FUNCTIONS
    public int getPeerId() 
    {
        return peerID;
    }



    public String getHeader()
    {
        return header;
    }


    public int getzerobitsLength()
    {
        return ZERO_BITS_LENGTH;
    }


    public int getpeeridSize()
    {
        return PEER_ID_SIZE;
    }



    public int getLength()
    {
        return length;
    }







}
