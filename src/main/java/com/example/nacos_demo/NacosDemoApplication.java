package com.example.nacos_demo;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zhangxiaoling01
 */
@RestController
@EnableNacosDiscovery
@SpringBootApplication
public class NacosDemoApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    @NacosInjected
    private NamingService namingService;

    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(NacosDemoApplication.class, args);
        System.out.println("-----------server start succ-------------");
    }

    @RequestMapping("/hello")
    public Object hello() throws Exception {
        Map<String, String> result = new HashMap();
        result.put("hello", "world");
        return result;
    }

    @RequestMapping("/invoke")
    public Object invoke() throws Exception {
        Map<String, String> result = new HashMap();
        // 根据服务名从注册中心获取一个健康的服务实例
        Instance instance = namingService.selectOneHealthyInstance(applicationName);
        result.put("ip", instance.getIp());
        result.put("port", instance.getPort() + "");
        String url = String.format("http://%s:%d/hello", instance.getIp(), instance.getPort());
        String httpResult = restTemplate.getForObject(url, String.class);
        System.out.println(String.format("请求URL:%s,响应结果:%s", url, httpResult));
        result.put("result", httpResult);
        return result;
    }

    //    @Override
//    public void run(String... args) throws Exception {
//        String ip = InetAddress.getLocalHost().getHostAddress();
//        System.out.println(String.format("name=%s,ip=%s,port=%s,status=%s", applicationName, ip, serverPort, namingService.getServerStatus()));
//        namingService.registerInstance(applicationName, ip, serverPort);
//    }

}
