package com.pjh.community;

import java.io.IOException;

public class WkhtmlTest {

    public static void main(String[] args) {
        String cmd = "d:/soft/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com f:/work/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
