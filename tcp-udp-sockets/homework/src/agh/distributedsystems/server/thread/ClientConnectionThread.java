package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientConnectionThread extends Thread {

  private Socket clientSocket;

  public ClientConnectionThread(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    try {
      BufferedReader userInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      int clientPort = clientSocket.getPort();
      Server server = Server.getInstance();

      while (true) {
        String messageText = userInputStream.readLine();
        server.sendTcpMessage(new Message(clientPort, messageText, LocalDateTime.now()));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
