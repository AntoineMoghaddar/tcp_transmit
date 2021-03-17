package tcphack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class NetworkLayer {
    TcpHandler handler;
    byte[] src;

    public NetworkLayer(TcpHandler tcp, byte[] myAddress) {
        src = myAddress;
        handler = tcp;
    }

    public void send(byte[] dst, byte[] data) {
        ByteArrayOutputStream outputStram = new ByteArrayOutputStream();

        byte[] headers = IPv6.makeHeaders(data.length, src, dst);

        try {
            outputStram.write(headers);
            outputStram.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] packet = outputStram.toByteArray();

        IntBuffer intbuf = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] finpack = new int[intbuf.remaining()];

        System.out.println("value of packet 6"
                + finpack[6]);
        handler.sendData(finpack);
    }

    public byte[] recv() {
        int[] data = handler.receiveData(1);
        System.out.println(data.length);
        if (data.length == 0) {
            System.out.println("returning nothing");
            return new byte[0];
        }
        byte[] res = new byte[data.length - 40];
        System.arraycopy(data, 40, res, 0, data.length - 40);
        return res;
    }
}