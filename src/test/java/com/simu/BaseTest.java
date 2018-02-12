package com.simu;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author DengrongGuan
 * @create 2017-12-22 上午9:22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WedApplication.class)
@WebAppConfiguration
public class BaseTest {

}