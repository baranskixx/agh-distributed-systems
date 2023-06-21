package org.example;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.example.connection.ZKConnection;

public class Main {

  public static final String HOST_DEFAULT = "127.0.0.1:2181";

  public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
    ZKConnection zkConnection = new ZKConnection();
    zkConnection.connect(HOST_DEFAULT);
    while (true) {
    }
  }
}