package com.matchome.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.matchome.model.Home;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prd")
public class EmbeddedDynamoDBConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        System.setProperty("sqlite4java.library.path", "target/lib");
        AmazonDynamoDB amazonDynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(Home.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

        return amazonDynamoDB;

    }

}