package tcphack;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TransportLayer {
    private final NetworkLayer network;
    private final byte[] dst;
    private final int srcPort;
    private final int dstPort;
    private int ackNumber;
    boolean connected;
    private int seqNumber;

    public TransportLayer(NetworkLayer netLayer, byte[] target, int port) {
        network = netLayer;
        dst = target;
        srcPort = (int) (Math.random() * Short.MAX_VALUE);
        dstPort = port;
        connected = false;
        seqNumber = 0;
        ackNumber = 0;
    }

    public boolean connected() {
        return connected;
    }

    public void close() {
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0,
                TCPPacket.ControlBit.FIN.getValue(), 64, new byte[0]);
        network.send(dst, packet.getPkt());
    }

    public void send(String message) {
        byte[] data;
        data = message.getBytes(StandardCharsets.UTF_8);
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0, TCPPacket.ControlBit.PSH.getValue(), 0xFFFF, data);
        seqNumber += data.length;
        network.send(dst, packet.getPkt());
    }

    public String recv() {
        byte[] data = network.recv();
        if (data.length == 0) {
            return null;
        }
        TCPPacket packet = new TCPPacket(data);
        int len = packet.getData().length;
        if (packet.getData().length == 0) {
            len++;
        }
        ackNumber = packet.getSequenceNumber() + len;
        sendAck();
        if (packet.getData().length == 0) {
            if ((packet.getControlBits() & TCPPacket.ControlBit.SYN.getValue()) != 0) {
                System.out.println("CONNECTED!");
                connected = true;
            }
            return "";
        }
        return new String(packet.getData(), StandardCharsets.UTF_8);
//        return "Error";
    }

    public void sendAck() {
        byte[] data = new byte[0];
        TCPPacket packet = new TCPPacket(srcPort, dstPort, ++seqNumber, ackNumber,
                16, 0xFFFF, data);
        network.send(dst, packet.getPkt());
    }

    public void connect() {
        //Lets send a SYN packet to connect.
        byte[] data = new byte[0];
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0, TCPPacket.ControlBit.SYN.getValue(), 0xFFFF, data);
        System.out.println("Sending to " + Arrays.toString(dst) + " packet: " + Arrays.toString(packet.getPkt()));
        network.send(dst, packet.getPkt());
    }
}