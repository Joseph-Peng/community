package com.pjh.community.service;

import com.pjh.community.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("singleton") // 默认是单例，启动时初始化
@Scope("prototype") // prototype
public class TestService {

    @Autowired
    private TestDao testDao;

    public TestService(){
        System.out.println("TestService的构造方法");
    }

    public String find(){
        return testDao.select();
    }

    @PostConstruct
    public void init(){
        System.out.println("init");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy");
    }

}
