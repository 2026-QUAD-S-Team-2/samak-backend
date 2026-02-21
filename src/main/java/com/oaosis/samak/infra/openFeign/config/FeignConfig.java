package com.oaosis.samak.infra.openFeign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.oaosis.samak.infra.openFeign")
public class FeignConfig {
}