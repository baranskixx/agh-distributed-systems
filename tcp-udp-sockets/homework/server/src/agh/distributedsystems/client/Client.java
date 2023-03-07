package agh.distributedsystems.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client {

  private final static String SERVER_HOST_NAME = "localhost";
  private final static int SERVER_PORT = 12345;

  private final Socket tcpSocket;
  private final DatagramSocket udpSocket;

  private final BufferedReader inStream;
  private final PrintWriter outStream;
  private byte[] receiveBuffer = new byte[1024];

  public Client(int clientPort) throws IOException {
    InetAddress address = InetAddress.getByName(SERVER_HOST_NAME);
    this.tcpSocket = new Socket(address, SERVER_PORT, address, clientPort);
    this.inStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    this.outStream = new PrintWriter(tcpSocket.getOutputStream(), true);
    this.udpSocket = new DatagramSocket(clientPort);
  }

  public void start() {
    Thread outThread = new Thread() {
      @Override
      public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
          String nextLine = scanner.nextLine();
          if (nextLine.startsWith("/u")) {
            try {
              byte[] sendBuffer = nextLine.substring(2).getBytes("UTF-8");
              DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName("localhost"), SERVER_PORT);
              udpSocket.send(sendPacket);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          } else {
            outStream.println(nextLine);
          }
        }
      }
    };

    Thread inThreadTCP = new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            String nextLine = inStream.readLine();
            System.out.println(nextLine);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    };

    Thread inThreadUDP = new Thread() {
      @Override
      public void run() {
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
          while(true) {
            udpSocket.receive(receivePacket);
            String messageStr = new String(receivePacket.getData());
            messageStr = messageStr.substring(0, messageStr.indexOf((char) 0));
            System.out.println(messageStr);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };


    outThread.start();
    inThreadTCP.start();
    inThreadUDP.start();
  }
}
