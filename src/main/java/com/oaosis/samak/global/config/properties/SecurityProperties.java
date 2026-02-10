package com.oaosis.samak.global.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
public class SecurityProperties {

    private final List<String> whitelist = new ArrayList<>();
}

