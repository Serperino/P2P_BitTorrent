import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

//Code taken from server.java example file, just placeholder 
public class Handler extends Thread {
    private String message;    //message received from the client
	private String MESSAGE;    //uppercase message send to the client
	private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
	private int no;		//The index number of the client

    public Handler(Socket connection, int no) {
        this.connection = connection;
	    this.no = no;
    }

}
