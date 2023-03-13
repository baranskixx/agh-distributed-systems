package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionInitThread extends Thread {

  private final static String SERVER_JOIN_MESSAGE = "JOINED";

  public ConnectionInitThread(){
  }

  @Override
  public void run() {
    try {
      Server server = Server.getInstance();
      ServerSocket serverSocket = server.getSocket();
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
