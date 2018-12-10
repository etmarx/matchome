# Matchome Spring Boot Application

## Prerequisites
* Java 8+
* Maven 3+

## How To Build
* In your local workspace, from the command line,

	`mvn clean verify`
		
## Running the application
* The result of the build will generate an artifact inside __./target__ directory and the name will be something similar to __matchome-0.0.1-SNAPSHOT.jar__.

* This jar file is a fully executable jar and can be executed just like any executable binary. See [details](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html)

	`cd ./target`
	
	`chmod 755 matchome-0.0.1-SNAPSHOT.jar`
	
	`./matchome-0.0.1-SNAPSHOT.jar`
	

## Actuator

* This application is equipped with [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) and comes with a list of production ready endpoints for us.

* Health check endpoint which is available at __/actuator/health__

```
	{
		status: "UP"
	}
```

* Prometheus metrics endpoint which is available at __/actuator/prometheus__

## Swagger
* Swagger API documentation can be found at __/swagger-ui.html__

## Profiles
* Spring boot application allows you to run the application using different [profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html)

* This application provides one default profile and one for production use. User is free to modify or create new profiles for different workspaces.

* __application.yml__ file is for default profile and __application-prd.yml__ is for production.

## DynamoDB

matchome API uses DynamoDB to save and search data. Tests run with DynamoDBEmbedded
