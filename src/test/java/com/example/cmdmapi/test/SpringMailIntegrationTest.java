package com.example.cmdmapi.test;
import com.example.cmdmapi.service.EmailService;
import com.example.cmdmapi.model.Email;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class SpringMailIntegrationTest {
    @Autowired
    private EmailService emailService;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Test
    public void shouldSendSingleMail() throws MessagingException, IOException {
        Email mail = new Email();
        mail.setFrom("eric@copyvic.com.br");
        mail.setTo("eric@copyvic.com.br");
        mail.setSubject("Teste de envio de email");
        mail.setContent("Este é o teste de envio de email se deus quiser funciona mas vai sim");

        emailService.sendSimpleMessage(mail);

        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];

        assertEquals(mail.getSubject(), current.getSubject());
        assertEquals(mail.getTo(), current.getAllRecipients()[0].toString());
        assertTrue(String.valueOf(current.getContent()).contains(mail.getContent()));

    }
}
