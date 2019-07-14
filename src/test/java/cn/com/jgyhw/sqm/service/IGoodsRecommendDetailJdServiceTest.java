package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.SqmApplicationTests;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * Created by WangLei on 2019/1/15 0015 12:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SqmApplicationTests.class)
@WebAppConfiguration
public class IGoodsRecommendDetailJdServiceTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();// 模拟真实web环境
    }
}
