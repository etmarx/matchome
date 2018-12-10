package com.matchome.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableDynamoDBRepositories("com.matchome.repository")
@EnableConfigurationProperties({
        AwsProperties.class})
@Configuration
public class DynamoDBConfig {
    private final String awsAccessKey;
    private final String awsSecretKey;

    public DynamoDBConfig (AwsProperties awsProperties) {
        this.awsAccessKey = awsProperties.getAccessKey();
        this.awsSecretKey = awsProperties.getSecretKey();

    }

    @Bean
    @Profile("prd")
    public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {
        @SuppressWarnings("deprecation")
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials);

        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

}