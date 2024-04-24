# P2P_BitTorrent
## Individual Contributions
* John Mone
  * Server/client structure (threads)
  * Peer creation
  * UNCHOKE, CHOKE, REQUEST, PIECE
  * File reading/writing
  * End behavior
  * Log writing
  * Data representation (BitSets, Byte[])
  * Video recording
* Jiawei Weng
  * Handshake creation
  * Message creation/sending, encoding/decoding
  * BITFIELD, HAVE interpretation
  *  Selecting preferred neighbors
  *  Peer interest statuses
* Darren Wang

# HOW TO RUN/NOTES OF THIS BUILD
1. Download zip file and extract
2. run "javac peerProcess.java"
3. Make sure a PeerInfo.cfg and Common.cfg are present for specifying peer and file information, an example is provided.
4. Have folders present for you to access files, i.e. if you specify peer 1001 has a file, make a folder called peer_1001 with the file in it. An example with a file is provided.
5. run "java peerProcess 'peerID'" where number would be any peer ID you listed in PeerInfo.cfg, ex java peerProcess 1001

# VIDEO SHOWCASE
https://youtu.be/RI3Npigcjmo?si=D66cuT8cK7DG2UUS

NOTES:
 * Make sure you start with the first peerID first and go in order or else the sockets will not work properly
 * This build in particular does not seem to like the visual studio code environment and is made to work on the storm/rain/thunder machines

| peerProcess.java            | Description                                       
|---------------------|---------------------------------------------------|
| beginListening          | Starts server and listens for peers joining             | 
| beginSearch() | Searches for ports to connect to based on PeerInfo.cfg |
| sendHandshake | Encodes message and sends handshake msg|
| sendUnchoke | Creates a dummy byte[], encodes message and sends unchoke message | 
| sendChoke | Creates a dummy byte[], encodes message and sends choke message | 
| updateInterestStatus         | Updates interest status of selectPeer in peerInterestStatus Map           | 
| isPeerLiked           | Returns if a peer is liked               | 
| sendInterestedMessage          | Creates dummy byte[], encodes message and sends interested message    |
| sendNotInterestedMessage          |Creates dummy byte[], encodes message and sends not interested message         | 
| sendhaveMessage          | Creates a byte[] and message with an offset value to be sent          |
| isInterestedInPeer                | Returns if interested in peer based on their BitSet                       |
| requestMessage          | Creates a byte[] and message with an int that represents a particular piece it wants from a peer        |
| pieceMessage     | Creates[] and message containing a piece        |
| selectPreferredNeighbors      |Selects preferred neighbors based on if peers are liked. | 
| loadFile     | Loads file specified in configInfo and peerInfo         |




