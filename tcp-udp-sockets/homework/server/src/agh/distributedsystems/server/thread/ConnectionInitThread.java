package agh.distributedsystems.server.thread;

import agh.distributedsystems.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionInitThread extends Thread {

  private static final String GREETING = "User%d joined the server\n";

  private final ServerSocket serverSocket;

  private Server server;


  public ConnectionInitThread(ServerSocket serverSocket) throws IOException {
    this.serverSocket = serverSocket;
    this.server = Server.getInstance();
  }

  @Override
  public void run() {
    try {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        server.addOutStreamToPort(clientSocket.getPort(), out);

        System.out.printf(GREETING, clientSocket.getPort());

        ClientConnectionThread clientConnectionThread = new ClientConnectionThread(clientSocket);
        clientConnectionThread.start();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
