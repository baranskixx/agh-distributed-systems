package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionInitThread extends Thread {

  private final static String SERVER_JOIN_MESSAGE = "JOINED";

  private Server server;

  public ConnectionInitThread() throws IOException {
    this.server = Server.getInstance();
  }

  @Override
  public void run() {
    ServerSocket serverSocket = server.getSocket();
    try {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        server.startNewClientConnection(clientSocket);
        server.sendTcpMessage(new Message(clientSocket.getPort(), SERVER_JOIN_MESSAGE, LocalDateTime.now()));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
