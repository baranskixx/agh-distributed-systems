package agh.distributedsystems.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client {

  private static final String SERVER_HOST_NAME = "localhost";
  private static final int SERVER_PORT = 12345;

  private final Socket tcpSocket;
  private final DatagramSocket udpSocket;

  private final BufferedReader inStream;
  private final PrintWriter outStream;
  private byte[] receiveBuffer = new byte[1024];

  public Client(int clientPort) throws IOException {
    InetAddress address = InetAddress.getByName(SERVER_HOST_NAME);
    this.tcpSocket = new Socket(address, SERVER_PORT, address, clientPort);
    this.udpSocket = new DatagramSocket(clientPort);
    this.inStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    this.outStream = new PrintWriter(tcpSocket.getOutputStream(), true);
  }

  public void start() {
    Thread outThread =
        new Thread() {
          @Override
          public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
              try {
                String nextLine = scanner.nextLine();
                if (nextLine.equals("/u")) {
                  String fullMessageText = "";
                  nextLine = "";
                  do {
                    fullMessageText = fullMessageText.concat(nextLine).concat("\n");
                    nextLine = scanner.nextLine();
                  } while (!nextLine.equals("/u"));
                  System.out.println(fullMessageText);
                  byte[] sendBuffer = fullMessageText.getBytes("UTF-8");
                  DatagramPacket sendPacket =
                      new DatagramPacket(
                          sendBuffer,
                          sendBuffer.length,
                          InetAddress.getByName("localhost"),
                          SERVER_PORT);
                  udpSocket.send(sendPacket);
                } else if (nextLine.equals("/e")) {

                } else {
                  outStream.println(nextLine);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        };

    Thread inThreadTCP =
        new Thread() {
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

    Thread inThreadUDP =
        new Thread() {
          @Override
          public void run() {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            while (true) {
              try {
                udpSocket.receive(receivePacket);
                String messageStr = new String(receivePacket.getData());
                messageStr = messageStr.substring(0, messageStr.indexOf((char) 0));
                System.out.println(messageStr);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
          }
        };

    outThread.start();
    inThreadTCP.start();
    inThreadUDP.start();
  }
}
