package tcphack;

public class IPv6 {

//2001:610:1908:ff02:e472:904c:1d87:5b90 --> Destination Address
//2001:67c:2564:ad00:0000:0b00:100a:0000 --> Source Address

    //byte values
    public static byte[] destinationIP = new byte[]{
            (byte) 0x20, (byte) 0x01, (byte) 0x06, (byte) 0x10,
            (byte) 0x19, (byte) 0x08, (byte) 0xff, (byte) 0x02,
            (byte) 0xe4, (byte) 0x72, (byte) 0x90, (byte) 0x4c,
            (byte) 0x1d, (byte) 0x87, (byte) 0x5b, (byte) 0x90
    };

    public static byte[] SourceIP = new byte[]{
            (byte) 0x20, (byte) 0x01, (byte) 0x06, (byte) 0x7c,
            (byte) 0x25, (byte) 0x64, (byte) 0xad, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0xb0, (byte) 0x00,
            (byte) 0x10, (byte) 0x0a, (byte) 0x00, (byte) 0x00
    };

    // creation of headers
    public static byte[] makeHeaders(int payloadLen, byte[] source, byte[] destination) {
        byte[] headers = new byte[40];
        headers[0] = 96; // 16 bits converted
        headers[4] = (byte) ((payloadLen & 0xFF00) >> 8);
        headers[5] = (byte) (payloadLen & 0xFF);
        headers[6] = (byte) 253;
//                (byte) MyTcpHandler.VERSION;
        headers[7] = 64;
        for (int i = 0; i != 16; ++i) {
            headers[8 + i] = source[i];
            headers[24 + i] = destination[i];
        }
        return headers;
    }
}
