package com.pjh.community.controller;

import com.google.code.kaptcha.Producer;
import com.pjh.community.entity.User;
import com.pjh.community.service.UserService;
import com.pjh.community.utils.CommunityConstant;
import com.pjh.community.utils.CommunityUtil;
import com.pjh.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

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
            model.addAttribute("msg", "????????????,??????????????????????????????????????????????????????,???????????????!");
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
            model.addAttribute("msg","????????????,???????????????????????????????????????!");
            model.addAttribute("target","/login");
        }else if(status == CommunityConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","????????????,???????????????????????????!");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","????????????,??????????????????????????????!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    /**
     *
     * @param response   ???????????????????????????
     * //@param session    ??????????????????session????????????????????????
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response /*,HttpSession session*/){
        // ???????????????
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // ??????????????????session????????????????????????  HttpSession
        //session.setAttribute("kaptcha",text);

        // ??????redis???????????????
        // ??????????????????
        String kaptchaOwner = CommunityUtil.getUUID();
        Cookie cookie = new Cookie("kaptchaOwner",kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contexPath);
        response.addCookie(cookie);
        // ??????????????????Redis
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey, text, 60, TimeUnit.SECONDS);

        //???????????????????????????  HttpServletResponse
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("????????????????????????"+e.getMessage());
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param code
     * @param rememberme
     * @param model
     * //@param session     ???????????????
     * @param response    ????????????ticket??????Cookie????????????????????????
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner){
        // ?????????????????????
        //String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if(StringUtils.isNoneBlank(kaptchaOwner)){
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","?????????????????????");
            return "/site/login";
        }

        // ??????????????????
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
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
