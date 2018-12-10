package com.matchome.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dynnamdb")
@Getter
@Setter
public class DynamoDBProperties {
    private String accessKey;
    private String secretKey;
}
