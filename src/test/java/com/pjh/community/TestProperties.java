package com.pjh.community;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    @Test
    public void testProperties(){
        Properties properties = new Properties();
        try {
            //new FileInputStream("resources/kaptcha.properties")
            properties.load(TestProperties.class.getResourceAsStream("/kaptcha.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties.getProperty("kaptcha.image.width"));
    }
}
