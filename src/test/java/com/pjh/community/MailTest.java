package com.pjh.community;

import com.pjh.community.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Test
    public void testTextMail(){
        mailClient.sendEmail("1301173255@qq.com","Test JavaMailSender","Hello.");
    }

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testHTMLMail(){
        Context context = new Context();
        context.setVariable("username","彭佳豪");
        String content = templateEngine.process("mail/demo",context);
        System.out.println(content);

        mailClient.sendEmail("1301173255@qq.com","Test JavaMailSender HTML",content);
    }

}
