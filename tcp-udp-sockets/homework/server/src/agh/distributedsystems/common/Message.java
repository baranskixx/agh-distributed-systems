package agh.distributedsystems.common;

import java.time.LocalDateTime;

public class Message {

  public final String MESSAGE_FORM = "|%s| USER%d: %s";

  private final int senderPort;

  private final String message;

  private final LocalDateTime time;

  public Message(int senderPort, String message, LocalDateTime time) {
    this.senderPort = senderPort;
    this.message = message;
    this.time = time;
  }

  @Override
  public String toString() {
    return String.format(MESSAGE_FORM, time.toString().substring(0, time.toString().lastIndexOf(".")).replace("T", " - "), senderPort, message);
  }

  public int getSenderPort() {
    return senderPort;
  }
}
