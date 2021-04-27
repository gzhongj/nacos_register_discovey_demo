package com.example.nacos_demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangxiaoling01
 * @description
 * @date 2021/4/27 21:22
 */

@FeignClient(name = "${spring.application.name}")
public interface FeignService {

    @RequestMapping("/invoke")
    String hello();

}
