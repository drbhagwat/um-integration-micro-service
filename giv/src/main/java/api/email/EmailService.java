package api.email;

import api.util.ArrayListOfStringsToStringArray;

import java.util.*;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


/**
 * Email Service to send an automatic email to the newly created user.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Slf4j
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties
@PropertySource("file:${CONF_DIR}/email.cfg")
public class EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

/*
  @Autowired
  private UserRepository userRepository;
*/

  @Value("${spring.mail.enabled}")
  private String enabled;

  @Value("${spring.mail.isHtml}")
  private boolean isHtml;

  @Value("${RECIPIENTS}")
  private String[] recipients;

  public void sendMail(String templateId, List<String> paramsToBeReplaced, String subject, List<String> toList) {
    if (enabled.trim().equalsIgnoreCase("y")) {
      EmailTemplate emailTemplate = new EmailTemplate(new StringBuilder(templateId));
      Map<String, String> replacements = new HashMap<>();

      Optional.ofNullable(paramsToBeReplaced).ifPresent(params -> {
        int i = 1;

        for (String param : paramsToBeReplaced) {
          replacements.put("parameter" + i++, param);
        }
      });

      String body = emailTemplate.getTemplate(replacements);

      String[] finalEmailList;

      try {
        if (toList != null) {
          finalEmailList = ArrayListOfStringsToStringArray.Convert(toList);
        } else {
//          List<String> globalEmailList = userRepository.getEmailsOfAllActiveAdmins();

          List<String> globalEmailList = new ArrayList<>();
          globalEmailList.addAll(Arrays.asList(recipients));
          finalEmailList = ArrayListOfStringsToStringArray.Convert(globalEmailList);
        }
        if (isHtml) {
          sendHtmlMail(finalEmailList, subject, body);
        } else {
          sendTextMail(finalEmailList, subject, body);
        }
      } catch (Exception exception) {
        log.info(exception.getMessage());
      }
    }
  }

  private void sendHtmlMail(String[] to, String subject, String text) throws Exception {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text, isHtml);
    try {
      javaMailSender.send(message);
    } catch (Exception exception) {
      log.info(exception.getMessage());
    }
  }

  private void sendTextMail(String[] to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    try {
      javaMailSender.send(message);
    } catch (Exception exception) {
      log.info(exception.getMessage());
    }
  }
}