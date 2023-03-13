package agh.distributedsystems.server.thread;

import agh.distributedsystems.common.Message;
import agh.distributedsystems.server.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.Arrays;

public class UdpReceiveThread extends Thread{

  public UdpReceiveThread(){

  }

  @Override
  public void run() {
    try (DatagramSocket datagramSocket = new DatagramSocket(Server.getInstance().getPortNumber())){
      byte[] receiveBuffer = new byte[2048];
      while(true) {
        Arrays.fill(receiveBuffer, (byte) 0);
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        datagramSocket.receive(receivePacket);
        Message udpMessage = new Message(receivePacket.getPort(), new String(receivePacket.getData()), LocalDateTime.now());
        Server.getInstance().sendUdpMessage(datagramSocket, udpMessage.toString(), receivePacket.getPort());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
