package com.pjh.community;

import com.pjh.community.dao.TestDao;
import com.pjh.community.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);

		TestDao testDao = applicationContext.getBean(TestDao.class);
		System.out.println(testDao.select());

		testDao = applicationContext.getBean("hibernateDao", TestDao.class);
		System.out.println(testDao.select());
	}

	@Test
	public void testBeanManagement(){
		TestService service = applicationContext.getBean(TestService.class);
		System.out.println(service);

		service = applicationContext.getBean(TestService.class);
		System.out.println(service);
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired  // 也可以加在set方法之前
	private TestDao testDao;
	@Autowired
	private TestDao testDao2;
	@Autowired
	@Qualifier("hibernateDao") // 指定名称的bean
	private TestDao hibernateDao;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Autowired
	private TestService testService;

	@Test
	public void testDI(){
		System.out.println(testDao.select());
		System.out.println(testDao2.select());
		System.out.println(hibernateDao.select());
		System.out.println(simpleDateFormat.format(new Date()));
		System.out.println(testService);
	}

}
