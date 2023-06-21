package org.example.system;

import java.io.IOException;

public class SystemUtil {

  public static void executeCommands(String... command) {
    if (command.length == 0) {
      return;
    }

    try {
      Runtime.getRuntime().exec(command);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
