package com.s3groupinc.gateway.api.email;

import com.s3groupinc.gateway.api.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Email Service to send an automatic email to the newly created user.
 */
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties
@PropertySource("file:${CONF_DIR}/email.cfg")
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.enabled}")
    private String enabled;

    @Value("${spring.mail.isHtml}")
    private boolean isHtml;

    @Value("${spring.mail.forgotSubject}")
    private String subject;

//  public void sendMail(String templateId, List<String> paramsToBeReplaced, String subject, List<String> toList) {
//    if (enabled.trim().equalsIgnoreCase("y")) {
//      EmailTemplate emailTemplate = new EmailTemplate(new StringBuffer(templateId));
//
//      Map<String, String> replacements = new HashMap<String, String>();
//
//      if (paramsToBeReplaced != null) {
//        int i = 1;
//
//        for (String param : paramsToBeReplaced) {
//          replacements.put("parameter" + Integer.toString(i++), param);
//        }
//      }
//      String body = emailTemplate.getTemplate(replacements);
//
//      String finalEmailList[] = null;
//
//      try {
//        if (toList != null) {
//          finalEmailList = ArrayListOfStringsToStringArray.Convert(toList);
//        } else {
//          List<String> globalEmailList = userRepository.getEmailsOfAllActiveAdmins();
//
//          for (String email : recipients) {
//            globalEmailList.add(email);
//          }
//          finalEmailList = ArrayListOfStringsToStringArray.Convert(globalEmailList);
//        }
//        if (isHtml) {
//          sendHtmlMail(finalEmailList, subject, body);
//        } else {
//          sendTextMail(finalEmailList, subject, body);
//        }
//      } catch (Exception exception) {
//        exception.printStackTrace();
//      }
//    }
//  }

    public void sendHtmlMail(String to, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);
        try {
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendTextMail(String to[], String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}