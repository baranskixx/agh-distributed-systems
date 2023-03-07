package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

public class UdpReceiveThread extends Thread{

  private Server server;

  public UdpReceiveThread() throws IOException {
    this.server = Server.getInstance();
  }

  @Override
  public void run() {
    byte[] receiveBuffer = new byte[2048];

    try (DatagramSocket datagramSocket = new DatagramSocket(server.getPortNumber())){
      while(true) {
        Arrays.fill(receiveBuffer, (byte) 0);
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        datagramSocket.receive(receivePacket);
        Message udpMessage = new Message(receivePacket.getPort(), new String(receivePacket.getData()), LocalDateTime.now());
        server.sendUdpMessage(datagramSocket, udpMessage.toString(), receivePacket.getPort());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
