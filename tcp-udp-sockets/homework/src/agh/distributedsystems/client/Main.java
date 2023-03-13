package agh.distributedsystems.client;

import java.io.IOException;import java.net.ConnectException;

public class Main {

  public static void main(String[] args) throws IOException {
    try {
    Client client = new Client(Integer.parseInt(args[0]));
    client.start();
    } catch (ConnectException e) {
      System.out.println("Cannot connect to specified server!");
      System.exit(-1);
    }
  }
}
