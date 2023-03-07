package agh.distributedsystems.server;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.thread.ClientConnectionThread;
import agh.distributedsystems.server.thread.ConnectionInitThread;
import agh.distributedsystems.server.thread.UdpReceiveThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class Server {

  public static final String SERVER_ALREADY_STARTED_ERR_MSG = "Server is already running!";

  public static final String DEFAULT_SERVER_ADDRESS = "localhost";
  public static final int DEFAULT_SERVER_PORT = 12345;

  private static Server instance = null;

  private final int portNumber;

  private boolean started = false;

  private ServerSocket socket;

  private ConnectionInitThread connectionInitThread;

  private UdpReceiveThread udpReceiveThread;

  private Map</*ClientPort*/Integer, /*ClientInStream*/PrintWriter> outStreamsTcp = new HashMap<>();


  public static Server getInstance() throws IOException {
    if (instance == null) {
      instance = new Server(DEFAULT_SERVER_PORT);
    }
    return instance;
  }

  private Server(int portNumber) throws IOException {
    this.portNumber = portNumber;
    this.socket = new ServerSocket(this.portNumber);
  }

  public void start() throws IOException {
    if (started) {
      System.err.println(SERVER_ALREADY_STARTED_ERR_MSG);
    } else {
      started = true;
      udpReceiveThread = new UdpReceiveThread();
      connectionInitThread = new ConnectionInitThread();
      udpReceiveThread.start();
      connectionInitThread.start();
    }
  }

  public synchronized void startNewClientConnection(Socket clientSocket) throws IOException {
    PrintWriter clientOuputStream = new PrintWriter(clientSocket.getOutputStream(), true);
    int clientPort = clientSocket.getPort();
    outStreamsTcp.put(clientPort, clientOuputStream);
    ClientConnectionThread clientConnectionThread = new ClientConnectionThread(clientSocket);
    clientConnectionThread.start();
  }

  public void sendTcpMessage(Message message) {
    for (int receiverPort : outStreamsTcp.keySet()) {
      if (receiverPort != message.getSenderPort()) {
        PrintWriter outStream = outStreamsTcp.get(receiverPort);
        outStream.println(message);
      }
    }
    System.out.println(message);
  }

  public void sendUdpMessage(DatagramSocket sender, String message, int senderPort) throws IOException {
    InetAddress address = InetAddress.getByName(DEFAULT_SERVER_ADDRESS);
    byte[] sendBuffer = message.getBytes("UTF-8");

    for (int port : outStreamsTcp.keySet()) {
      if (senderPort != port) {
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        sender.send(sendPacket);
      }
    }
  }

  public int getPortNumber() {
    return portNumber;
  }

  public ServerSocket getSocket() {
    return socket;
  }

  public Set<Integer> getPorts(){
    return outStreamsTcp.keySet();
  }
}
