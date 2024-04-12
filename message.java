
//May use this eventually,



import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import java.nio.ByteBuffer;

public class message {
    public enum MessageType {
        CHOKE(0),
        UNCHOKE(1),
        INTERESTED(2),
        NOT_INTERESTED(3),
        HAVE(4),
        BITFIELD(5),
        REQUEST(6),
        PIECE(7);

        private final int value;

        MessageType(int value) {
            this.value = value;
        }

        public static MessageType fromInt(int i) {
            for (MessageType type : MessageType.values()) {
                if (type.getValue() == i) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid message type value: " + i);
        }

        public int getValue() {
            return value;
        }
    }

    private MessageType type;
    private byte[] payload;

    public message(MessageType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    // Encode the message into a byte array
    public byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + 1 + payload.length);
        buffer.putInt(1 + payload.length);
        buffer.put((byte) type.getValue());
        buffer.put(payload);
        return buffer.array();
    }

    // Decode a byte array to create a Message object
    public static message decode(byte[] encodedMessage) {
        ByteBuffer buffer = ByteBuffer.wrap(encodedMessage);
        int length = buffer.getInt();
        if (length != encodedMessage.length - 4) {
            throw new IllegalArgumentException("Invalid message length.");
        }
        byte typeValue = buffer.get();
        MessageType messageType = MessageType.fromInt(typeValue);
        byte[] payload = new byte[length - 1];
        buffer.get(payload);
        return new message(messageType, payload);
    }

    
}

