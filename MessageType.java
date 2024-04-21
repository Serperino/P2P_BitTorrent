public enum MessageType {
    CHOKE(0),
    UNCHOKE(1),
    INTERESTED(2),
    NOT_INTERESTED(3),
    HAVE(4),
    BITFIELD(5),
    REQUEST(6),
    PIECE(7);
   // END(8);

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