package tcphack;

class MyTcpHandler extends TcpHandler {
    public static int VERSION = 253;
    public boolean done, sent;

    public static void main(String[] args) {
        new MyTcpHandler();
    }

    public MyTcpHandler() {
        super();

        done = false;
        sent = false;
        NetworkLayer nLayer = new NetworkLayer(this, IPv6.SourceIP);
        TransportLayer tLayer = new TransportLayer(nLayer, IPv6.destinationIP, 7710);
        tLayer.connect();
        System.out.println("tLayer connected");
        long lastMsg = System.currentTimeMillis();
        while (!done) {
            String result = tLayer.recv();
            System.out.println("Value of result: " + result);
            if (result != null) {
                lastMsg = System.currentTimeMillis();
                System.out.print(result);
            }
            if (tLayer.connected() && !sent) {
                sent = true;
                tLayer.send("GET /?nr=1570900 HTTP/1.0\n\n");
                System.out.println("GET request send");
            }
            if (System.currentTimeMillis() - lastMsg >= 10 * 1000) {
                tLayer.close();
                done = true;
                break;
            }
            System.out.println("nothing....");
        }
        System.out.println("system closed");
    }
}

//
//package ns.tcphack;
//
//class MyTcpHandler extends TcpHandler {
//    public static void main(String[] args) {
//        new MyTcpHandler();
//    }
//
//    public MyTcpHandler() {
//        super();
//
//        boolean done = false;
//
//        // array of bytes in which we're going to build our packet:
//        int[] txpkt = new int[40];		// 40 bytes long for now, may need to expand this later
//
//        txpkt[0] = 0x60;	// first byte of the IPv6 header contains version number in upper nibble
//        // fill in the rest of the packet yourself...:
//        txpkt[1] = .........;
//        txpkt[2] = .........;
//		......
//
//        this.sendData(txpkt);	// send the packet
//
//        while (!done) {
//            // check for reception of a packet, but wait at most 500 ms:
//            int[] rxpkt = this.receiveData(500);
//            if (rxpkt.length==0) {
//                // nothing has been received yet
//                System.out.println("Nothing...");
//                continue;
//            }
//
//            // something has been received
//            int len=rxpkt.length;
//
//            // print the received bytes:
//            int i;
//            System.out.print("Received "+len+" bytes: ");
//            for (i=0;i<len;i++) System.out.print(rxpkt[i]+" ");
//            System.out.println("");
//        }
//    }
//}
