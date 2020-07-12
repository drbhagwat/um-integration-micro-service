package api.email;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {
    @Value("${EMAIL_TEMPLATE_NOT_FOUND}")
    private String emailTemplateNotFound;

    private StringBuilder templateId = null;
    private StringBuilder template = null;

    public EmailTemplate(StringBuilder templateId) {
        this.templateId = templateId;

        try {
            this.template = loadTemplate(templateId);
        } catch (Exception e) {
            this.template = new StringBuilder("");
        }
    }

    private StringBuilder loadTemplate(final StringBuilder templateId) throws Exception {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(templateId.toString())) {
            content.append(new String(inputStream.readAllBytes()));
        } catch (IOException e) {
            throw new Exception(emailTemplateNotFound);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = template.toString();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return cTemplate;
    }
}