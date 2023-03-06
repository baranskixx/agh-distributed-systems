package agh.distributedsystems.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

  private final static String SERVER_HOST_NAME = "localhost";

  private final static int SERVER_PORT = 12345;
  private final Socket socket;

  private final DatagramSocket datagramSocket;

  private final BufferedReader in;
  private final PrintWriter out;

  private byte[] receiveBuffer = new byte[1024];

  public Client() throws IOException {
    this.socket = new Socket(SERVER_HOST_NAME, SERVER_PORT);
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
    this.datagramSocket = new DatagramSocket();
  }

  public void start() {
    Thread outThread = new Thread() {
      @Override
      public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
          String nextLine = scanner.nextLine();

          if (nextLine.startsWith("-u")) {
            byte[] sendBuffer = nextLine.substring(2).getBytes();
            try {
              DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName("localhost"), SERVER_PORT);
              datagramSocket.send(sendPacket);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          } else {
            out.println(nextLine);
          }
        }
      }
    };

    Thread inThreadTCP = new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            String nextLine = in.readLine();
            System.out.println(nextLine);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    };

    Thread inThrreadUDP = new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            datagramSocket.receive(receivePacket);
            System.out.println(Arrays.toString(receivePacket.getData()));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    };

    outThread.start();
    inThreadTCP.start();
    inThrreadUDP.start();
  }
}
