package agh.distributedsystems.server;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.thread.ConnectionInitThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {

  public static final String SERVER_ALREADY_STARTED_ERR_MSG = "Server is already running!";

  public static final int DEFAULT_SERVER_PORT = 12345;

  private static Server instance = null;

  private final int portNumber;

  private boolean started = false;

  private ServerSocket socket;

  private Map<Integer, PrintWriter> outStreams = new HashMap<>();

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
      ConnectionInitThread connectionInitThread = new ConnectionInitThread(socket);
      connectionInitThread.start();
    }
  }

  public synchronized void addOutStreamToPort(int port, PrintWriter printWriter) {
    outStreams.put(port, printWriter);
  }

  public void sendMessageToEveryChatMember(Message message) {
    for (int port : outStreams.keySet()) {
      if (message.getSenderPort() != port) {
        outStreams.get(port).println(message);
      }
    }
  }

  public int getPortNumber() {
    return portNumber;
  }

  public ServerSocket getSocket() {
    return socket;
  }
}
