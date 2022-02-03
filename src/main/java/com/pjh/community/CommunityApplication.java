package com.pjh.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
//@MapperScan("com.pjh.community")
public class CommunityApplication {

	@PostConstruct
	public void init(){
		// 解决netty启动冲突问题
		// see Netty4Utils.setAvailableProcessors()

		/*   Netty4Utils.java
		public static void setAvailableProcessors(final int availableProcessors) {
		      // we set this to false in tests to avoid tests that randomly set processors from stepping on each other
		      final boolean set = Booleans.parseBoolean(System.getProperty("es.set.netty.runtime.available.processors", "true"));
		      ...
		 }
		 */
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
