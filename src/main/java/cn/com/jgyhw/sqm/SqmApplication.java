package cn.com.jgyhw.sqm;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@ImportResource({"classpath:applicationContext*.xml"})
@SpringBootApplication
@MapperScan("cn.com.jgyhw.sqm.mapper")
public class SqmApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SqmApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SqmApplication.class);
	}

}

