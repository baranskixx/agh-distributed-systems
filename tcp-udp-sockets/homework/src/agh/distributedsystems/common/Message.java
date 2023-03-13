package agh.distributedsystems.common;

import java.time.LocalDateTime;

public class Message {

  private static final String CHAT_MESSAGE_PATTERN = "[%s]USER%d:%s";

  private final int senderPort;

  private final String messageText;

  private final LocalDateTime time;

  public Message(int senderPort, String messageText, LocalDateTime time) {
    this.senderPort = senderPort;
    this.messageText = messageText;
    this.time = time;
  }

  public int getSenderPort() {
    return senderPort;
  }

  @Override
  public String toString() {
    String formattedTime = time.toString().substring(0, time.toString().lastIndexOf(".")).replace("T", " ");
    return String.format(CHAT_MESSAGE_PATTERN, formattedTime, senderPort, messageText);
  }
}
