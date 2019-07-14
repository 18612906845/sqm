package cn.com.jgyhw.sqm;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackageClasses = {SqmApplication.class}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=SqmApplication.class)
})
@MapperScan(basePackageClasses = {SqmApplication.class})
public class SqmApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqmApplicationTests.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext cac = SpringApplication.run(SqmApplicationTests.class, args);

        LOGGER.debug("*********************启动完毕*********************");
    }

}

