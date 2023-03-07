package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientConnectionThread extends Thread {

  private Server server;
  private BufferedReader userInputStream;
  private int port;

  public ClientConnectionThread(Socket clientSocket) throws IOException {
    this.userInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    this.server = Server.getInstance();
    this.port = clientSocket.getPort();
  }

  @Override
  public void run() {
    try {
      while (true) {
        String messageText = userInputStream.readLine();
        server.sendTcpMessage(new Message(port, messageText, LocalDateTime.now()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
