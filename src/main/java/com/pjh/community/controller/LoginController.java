package com.pjh.community.controller;

import com.google.code.kaptcha.Producer;
import com.pjh.community.entity.User;
import com.pjh.community.service.UserService;
import com.pjh.community.utils.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contexPath;


    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model,@PathVariable("userId") int userId, @PathVariable("code") String code){
        int status = userService.activation(userId, code);
        if (status == CommunityConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target","/login");
        }else if(status == CommunityConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作,该账号已经激活过了!");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败,您提供的激活码不正确!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    /**
     *
     * @param response   将图片输出给浏览器
     * @param session    将验证码存入session，便于登录时比对
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session，便于登录时比对  HttpSession
        session.setAttribute("kaptcha",text);
        //将图片输出给浏览器  HttpServletResponse
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param code
     * @param rememberme
     * @param model
     * @param session     获取验证码
     * @param response    将登录的ticket存入Cookie，并返回给浏览器
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response){
        // 首先检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确！");
            return "/site/login";
        }

        // 验证账号密码
        int expiredSeconds = rememberme ? CommunityConstant.REMEMBER_EXPIRED_SECONDS : CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> loginInfo = userService.login(username, password, expiredSeconds);
        if(loginInfo.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",loginInfo.get("ticket").toString());
            cookie.setMaxAge(expiredSeconds);
            cookie.setPath(contexPath);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",loginInfo.get("usernameMsg"));
            model.addAttribute("passwordMsg",loginInfo.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }
}
