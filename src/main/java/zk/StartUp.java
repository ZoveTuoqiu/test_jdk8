package zk;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by gaoziye on 2016/3/23.
 */
public class StartUp {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:/config/applicationContext.xml");
    }

}
