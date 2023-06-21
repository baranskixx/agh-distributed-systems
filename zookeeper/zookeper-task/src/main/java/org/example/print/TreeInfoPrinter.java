package org.example.print;

import java.util.List;
import org.example.connection.ZKConnection;

public class TreeInfoPrinter {

  public static final String SEPARATOR = "----------------------------";

  public static void printTreeInfo(String rootZNode, ZKConnection zkConnection) {
    List<String> childrenList = zkConnection.getChildrenOfZNode(rootZNode, null);
    if (rootZNode.equals("/z")) {
      System.out.println("Liczba dzieci '/z' = " + childrenList.size());
    }

    System.out.println(rootZNode);
    for (String ch : childrenList) {
      printTreeInfo(rootZNode.concat("/").concat(ch), zkConnection);
    }

    if (rootZNode.equals("/z")) {
      System.out.println(SEPARATOR);
    }
  }

}
