package org.example.connection;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.example.watcher.ZNodeWatcher;

public class ZKConnection {

  public static final String Z_NODE_PATH = "/z";

  private ZooKeeper zk;

  public ZooKeeper connect(String host) throws IOException, InterruptedException, KeeperException {
    CountDownLatch latch = new CountDownLatch(1);
    Watcher watcher = watchedEvent -> {
      if (watchedEvent.getState() == KeeperState.SyncConnected) {
        latch.countDown();
      }
    };

    zk = new ZooKeeper(host, 2000, watcher);
    zk.addWatch(Z_NODE_PATH, new ZNodeWatcher(this), AddWatchMode.PERSISTENT);
    latch.await();
    return zk;
  }

  public List<String> getChildrenOfZNode(String zNodePath, Watcher watcher) {
    try {
      return zk.getChildren(zNodePath, watcher);
    } catch (KeeperException | InterruptedException e) {
      e.printStackTrace();
    }
    return List.of();
  }
}
