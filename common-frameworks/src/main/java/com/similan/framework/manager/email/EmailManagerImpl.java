package com.similan.framework.manager.email;

import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.template.TemplateResourceService;
import com.similan.framework.template.TemplateResourceService.EmailContent;

@Slf4j
@Service("emailInternalService") // TODO: move to the actual EmailInternalServiceImpl class
@FieldDefaults(level=AccessLevel.PRIVATE)
public class EmailManagerImpl implements EmailManager {
  @Autowired
  List<EmailSender> emailSenders;
  @Autowired
  TemplateResourceService templateRepository;
  @Autowired
  PlatformCommonSettings commonSettings;
  
  EmailSender emailSender;
  
  @Setter
  @Getter
  String emailSenderId;
  
  @PostConstruct
  public void initialize() {
    checkState(StringUtils.isNotBlank(emailSenderId), "Email sender id must be set.");
    Map<String, EmailSender> emailSendersById = new HashMap<>();
    for (EmailSender emailSender : emailSenders) {
      String emailSenderId = emailSender.getId();
      if (emailSendersById .put(emailSenderId, emailSender) != null) {
        throw new IllegalArgumentException("Duplicate email sender id:  " + emailSenderId);
      }
    }
    emailSender = emailSendersById.get(emailSenderId);
    checkState(emailSender != null, "Email sender with id " + emailSenderId + " not found.");
  }

  /**
   * gets the template, composes the body and sends email
   * 
   * @throws Exception
   * @throws ParseErrorException
   * @throws ResourceNotFoundException
   */
  public void send(String from, String to, String template,
      Map<String, Object> arguments) {
    
    /* add base URL to argument list */
    Object baseUrl = this.getCommonSettings().getPortalApplcationUrl()
        .getValue();
    arguments.put("baseUrl", baseUrl);
    String logoUrl = (String)arguments.get("businessLogo");
    log.info("Branded logo " + logoUrl);
    
    if(StringUtils.isBlank(logoUrl)){
      logoUrl = this.getCommonSettings().getPortalApplcationUrl()
          .getValue() + "images/businessReachLogo.png";
      arguments.put("businessLogo", logoUrl);
    }
        
    /* get the composed content */
    String allContent = this.templateRepository.getComposedTemplateContent(
        template, arguments);
    EmailContent content = getEmailContent(allContent);

    log.info("Email from " + from + " email to be sent to " + to
        + " content:\n" + content);
    this.emailSender.send(from, to, null, content.getSubject(),
        content.getBody());
  }

  public PlatformCommonSettings getCommonSettings() {
    return commonSettings;
  }

  public void setCommonSettings(PlatformCommonSettings commonSettings) {
    this.commonSettings = commonSettings;
  }

  private static int getFirstLineEnd(String content) {
    int newLine = content.indexOf('\n');
    int cartridgeReturn = content.indexOf('\r');
    int end;
    if (newLine == -1) {
      end = cartridgeReturn;
    } else if (cartridgeReturn == -1) {
      end = newLine;
    } else {
      end = Math.min(newLine, cartridgeReturn);
    }
    return end;
  }

  public static EmailContent getEmailContent(String content) {
    int end = getFirstLineEnd(content);
    if (end == -1) {
      return new EmailContent(content, "");
    }

    String rawSubject = content.substring(0, end);
    String subject = rawSubject.trim();

    log.info("Email subject " + subject);

    char c;
    while ((c = content.charAt(++end)) == '\r' || c == '\n')
      ;
    String body = content.substring(end, content.length());

    return new EmailContent(subject, body);
  }
}
