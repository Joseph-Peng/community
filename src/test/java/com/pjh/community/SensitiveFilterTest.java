package com.pjh.community;

import com.pjh.community.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "★★赌★博★赌★博我★不赌★博fab★☆c";
        System.out.println(sensitiveFilter.filter(text));
    }
}
