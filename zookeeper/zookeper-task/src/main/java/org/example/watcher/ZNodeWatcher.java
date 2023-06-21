package org.example.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.example.connection.ZKConnection;
import org.example.print.TreeInfoPrinter;
import org.example.system.SystemUtil;

public class ZNodeWatcher implements Watcher {

  public static final String Z_NODE_PATH = "/z";
  public static final String[] CALCULATOR_START_COMMAND = {"open", "-a", "Calendar"};
  public static final String[] CALCULATOR_STOP_COMMAND = {"pkill", "-x", "Calendar"};

  private ZKConnection zkConnection;

  public ZNodeWatcher(ZKConnection zkConnection) {
    this.zkConnection = zkConnection;
  }

  @Override
  public void process(WatchedEvent event) {
    if (Z_NODE_PATH.equals(event.getPath())) {
      EventType eventType = event.getType();
      switch (eventType) {
        case NodeCreated:
          SystemUtil.executeCommands(CALCULATOR_START_COMMAND);
          break;
        case NodeDeleted:
          SystemUtil.executeCommands(CALCULATOR_STOP_COMMAND);
          break;
        case NodeChildrenChanged:
          TreeInfoPrinter.printTreeInfo(Z_NODE_PATH, zkConnection);
          break;
        default:
          break;
      }
    }
  }
}
