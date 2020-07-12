package com.s3groupinc.gateway.api.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {
    @Value("${EMAIL_TEMPLATE_NOT_FOUND}")
    private String emailTemplateNotFound;

    private StringBuffer templateId;
    private StringBuffer template;

    public EmailTemplate(StringBuffer templateId) {
        this.templateId = templateId;

        try {
            this.template = loadTemplate(templateId);
        } catch (Exception e) {
            this.template = new StringBuffer("");
        }
    }

    private StringBuffer loadTemplate(StringBuffer templateId) throws Exception {
        InputStream inputStream = new FileInputStream(templateId.toString());
        StringBuffer content = new StringBuffer();
        try {
            content.append(new String(inputStream.readAllBytes()));
        } catch (IOException e) {
            throw new Exception(emailTemplateNotFound);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = template.toString();

        if (template != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return cTemplate;
    }
}