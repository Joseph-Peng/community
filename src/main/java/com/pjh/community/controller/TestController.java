package com.pjh.community.controller;

import com.pjh.community.service.TestService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello SpringBoot.";
    }

    @RequestMapping("/find")
    @ResponseBody
    public String find(){
        return testService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){

        System.out.println(request.getMethod());
        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+" : "+ value);
        }
        System.out.println(request.getParameter("code"));

        System.out.println(response.getStatus());
        //设置返回内容的类型
        response.setContentType("text/html;charset=utf-8");
        try(
                // 放在这里会自动帮我们完成在finally中的close
                PrintWriter writer = response.getWriter();
            ) {
            writer.write("<h1>Fake News Community</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "20") int limit){
        System.out.println("current = "+current+", limit = "+limit);
        return "some students";
    }

    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        return "a student"+ id;
    }

    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        return "a student : name = "+ name +",age = "+age;
    }

    // 响应html数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("name","韩立");
        mv.addObject("age",10);
        mv.setViewName("test/view");
        return mv;
    }
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","深圳大学");
        model.addAttribute("age",80);
        return "test/view";
    }

    @RequestMapping(path = "/person",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPerson(){
        Map<String, Object> data = new HashMap<>();
        data.put("name","彭佳豪");
        data.put("公司","腾讯");
        data.put("年薪",500000);
        return data;
    }

    @RequestMapping(path = "/persons",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getPersons(){
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> p1 = new HashMap<>();
        p1.put("name","彭佳豪");
        p1.put("公司","腾讯");
        p1.put("年薪",500000);
        data.add(p1);

        Map<String, Object> p2 = new HashMap<>();
        p2.put("name","WZQ");
        p2.put("公司","腾讯");
        p2.put("年薪",42);
        data.add(p2);

        Map<String, Object> p3 = new HashMap<>();
        p3.put("name","LX");
        p3.put("公司","字节跳动");
        p3.put("年薪",45);
        data.add(p3);
        return data;
    }
}
