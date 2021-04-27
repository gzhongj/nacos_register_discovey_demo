package com.example.nacos_demo;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhangxiaoling01
 */
@RestController
@EnableNacosDiscovery
@SpringBootApplication
@EnableFeignClients
public class NacosDemoApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    @NacosInjected
    private NamingService namingService;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private FeignService feignService;

    public static void main(String[] args) {
        SpringApplication.run(NacosDemoApplication.class, args);
        System.out.println("-----------server start succ-------------");
    }

    @RequestMapping("/hello")
    public Object hello() throws Exception {
        Map<String, String> result = new HashMap();
        result.put("hello", "world");

        String feignResult = feignService.hello();
        System.out.println("feign result=" + feignResult);
        return result;
    }

    @RequestMapping("/invoke")
    public Object invoke() throws Exception {
        Map<String, String> result = new HashMap();
        result.put("result", "/invoke");
        return result;
    }

    /**
     * 手动注册服务
     *
     * @param args
     * @throws Exception
     */
    public void run(String... args) throws Exception {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println(String.format("name=%s,ip=%s,port=%s,status=%s", applicationName, ip, 8080, namingService.getServerStatus()));
        namingService.registerInstance(applicationName, ip, 8080);
    }

}
