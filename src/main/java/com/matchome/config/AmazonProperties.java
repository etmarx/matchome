package com.matchome.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("amazon")
@Getter
@Setter
public class AmazonProperties {
    private DynamoDBProperties dynamoDBProperties;
    private AwsProperties awsProperties;
}
