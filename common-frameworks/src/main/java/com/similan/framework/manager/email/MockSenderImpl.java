package com.similan.framework.manager.email;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockSenderImpl implements EmailSender {

  @Override
  public String getId() {
    return "mock";
  }

  /**
   * send message
   */
  public void send(String sender, String recipient, String replyTo,
      String subject, String bodyMsg) {
    StringBuilder builder = new StringBuilder();
    builder.append("=== EMAIL ===\n");
    builder.append("From: ").append(sender).append("\n");
    builder.append("To: ").append(recipient).append("\n");
    builder.append("Reply-To: ").append(replyTo).append("\n");
    builder.append("Subject: ").append(subject).append("\n");
    builder.append(bodyMsg).append("\n\n\n");
    log.info(builder.toString());
  }

}
