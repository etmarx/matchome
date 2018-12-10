package com.matchome.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("amazon.aws")
@Getter
@Setter
public class AwsProperties {
    private String accessKey;
    private String secretKey;
}
