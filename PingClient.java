/**
 * PingClient.java
 *
 * Login name: yilunq
 *
 * @Yilun Qian
 *
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;


public class PingClient{

    private static int count = 3;
    private static final String PING = "PING";
    private static final String CRLF = "\r\n";
    private static final int TIMEOUT = 1000; // set to be 1 second

    private static int packetCounter = 0;

    private static int sentStat = count;
    private static int receivedStat = 0;
    private static int lostStat = 0;
    private static int byteStat = 0;
    private static List<String> dataStat = new ArrayList<>();
    private static List<Long> rrts = new ArrayList<>();

    public static void increaseReceivedStat() {
        receivedStat++;
    }

    public static void increaseLostStat() {
        lostStat++;
    }

    public static void setByteStat(int i) {
        byteStat = i;
    }

    public static void setRrts(long i) {
        rrts.add(i);
    }

    public static void setDataStat(InetAddress IPaddr, int byteStat, long time) {
        String s = "Reply from " + IPaddr.getHostAddress() + ": bytes = " + byteStat + " time = "
                + time + "ms";
        StringBuilder sb = new StringBuilder(s);
        dataStat.add(sb.toString());
    }

    public static void setDataStat(String s) {
        dataStat.add(s);
    }

    public static int getPacketCounter() {
        return packetCounter;
    }

    public static void setPacketCounter(int i) {
        packetCounter = i;
    }

    public static void main(String[] args) throws Exception {
        // Get command line argument
        if (args.length == 3) {
            count = Integer.parseInt(args[2]);
            sentStat = count;
        }
        if (args.length < 2) {
            System.out.println("Required arguments: host, port");
            return;
        }

        InetAddress laddr = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        System.out.println("\n *** Ping Client is working *** \n *** Ping messages send to "
                + laddr.getHostAddress() + "\n *** it is listening to port number: " + port
                + "\nPlease wait...");

        int defaultDataLength = (PING + " "
                        + PingClient.getPacketCounter() + " "
                        + System.currentTimeMillis() + " "
                        + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " "
                        + CRLF).getBytes().length;

        System.out.println("\nPinging " + laddr.getHostName()
                + " [" + laddr.getHostAddress() + "] " + " with " 
                + defaultDataLength + " bytes of data:\n");

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);

        // Launch each thread per 1 second
        while (packetCounter < count) {
            try {
                ClientRequest clientRequest = new ClientRequest(laddr, socket, port);
                Thread thread = new Thread(clientRequest);
                thread.start();
            } catch (Exception e) {
                System.out.println(e);
            }
            Thread.sleep(1000);
        }
        // Guarantee all tasks are done.
        Thread.sleep(1000);
        printResult(laddr);
        System.out.println("\n");
    }

    private static void printResult(InetAddress laddr){
        System.out.println("\nPing statistics from " + laddr.getHostAddress() + ":");
        System.out.println("Packets: Sent = " + sentStat + ", Received = " + receivedStat
                + ", Lost = " + lostStat + " (" + (double)lostStat/sentStat + "% loss),");

        if(lostStat == sentStat) {
            System.out.println("\nAll packets are lost! Please try again.\n");
        }
        else {
            System.out.println("Approximate round trip times in milli-seconds:");
            System.out.print("Minimux = " + Collections.min(rrts) + "ms, ");
            System.out.print("Maximun = " + Collections.max(rrts) + "ms, ");

	        double sum = 0;
	        for (long i : rrts) {
	            sum += i;
        	}
        double avg = sum/rrts.size();
        System.out.print("Average = " + String.format("%.2f", avg)
                + "ms");
        }
    }
}

class ClientRequest implements Runnable {
    private InetAddress laddr;
    private String timeStamp;
    private String PING = "PING";
    private String CRLF = "\r\n";
    private DatagramSocket socket;
    private int port;

    public ClientRequest(InetAddress laddr, DatagramSocket socket, int port) {
        this.laddr = laddr;
        this.socket = socket;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            sendAndReceive();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendAndReceive() throws Exception {
        // Create a datagram packet to received data
        DatagramPacket inPacket = new DatagramPacket(new byte[1024], 1024);

        // Create a datagram packet to package data to send
        DatagramPacket outPacket = new DatagramPacket(new byte[1024], 1024, laddr, port);

// --------------------------------- SEND DATA MESSAGE ------------------------------------
        // Set data to be sent
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String data = PING + " "
                + PingClient.getPacketCounter() + " "
                + System.currentTimeMillis() + " "
                + timeStamp + " "
                + CRLF;
        byte[] buff = data.getBytes();
		outPacket.setData(buff);
        // Send
        socket.send(outPacket);
        // Update sequence_number
        PingClient.setPacketCounter(PingClient.getPacketCounter()+1);

// --------------------------------- RECEIVE DATA MESSAGE ------------------------------------
        boolean receivedResponse = true;
        try {
            if (receivedResponse) {
                socket.receive(inPacket);
                String byteLine = printData("Received from ", inPacket);
                if (!inPacket.getAddress().equals(laddr)) {
                    throw new IOException("Received packet from an unknown source");
                }
                PingClient.increaseReceivedStat();
                String[] tokens = byteLine.split(" ");
                long packetMillSec = new Long(tokens[2]);
                long currentMillSec = System.currentTimeMillis();

                // Overcome case of 02 - 58
                if (packetMillSec > currentMillSec) {
                    currentMillSec += 60;
                }
                long RTT = Math.abs(currentMillSec - packetMillSec);
                PingClient.setDataStat(laddr, buff.length, RTT);
                PingClient.setByteStat(byteLine.length());
                PingClient.setRrts(RTT);

                System.out.println("Reply from " + laddr.getHostAddress()
                        + ": bytes = " + byteLine.length()
                        + " time = " + RTT + "ms");
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout!");
            PingClient.setDataStat("Timeout!");
            receivedResponse = false;
            PingClient.increaseLostStat();
        }
    }

    private static String printData(String s, DatagramPacket packet) throws Exception {
        // Obtain references to the packet's array of bytes
        byte[] buf = packet.getData();
        // Wrap the bytes in a byte array input stream,
        // so you can read the data as a stream of bytes
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters
        InputStreamReader isr = new InputStreamReader(bais);
        // Wrap the input stream reader in a buffered reader
        // so you can read the character data a line at a time
        // (A line is a sequence of chars terminated by any combination of \r and \n)
        BufferedReader br = new BufferedReader(isr);
        // The message data is contained in a single line, so read this line
        String line = br.readLine();
        return line;
    }
}