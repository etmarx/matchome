package com.matchome.controller;

import com.matchome.errors.ErrorType;
import com.matchome.exception.MatchHomeNotFoundException;
import com.matchome.model.Home;
import com.matchome.model.HomeStatus;
import com.matchome.service.HomeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class MatchHomeControllerIT {

    @Autowired
    private HomeService homeService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String USER = "testuser@test.com";
    private static final String MATCH_HOMES_URL = "/v1/homes";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final Home home = new Home();
    private String id;

    @Before
    public void setup()  {
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        home.setZipCode(55555);
        home.setBathrooms(3.5);
        home.setHomeStatus(HomeStatus.AVAILABLE);
        home.setEmail(USER);
        id = homeService.saveHome(home);

    }

    @After
    public void teardown() {
        homeService.deleteByEmail(USER);
    }

    @Test
    public void testHealthCheck() {
        String body = this.restTemplate.getForObject("/actuator/health", String.class);
        assertThat(body).contains("UP");
    }

    @Test
    public void testAddHome_success() {
        Home home = new Home();
        home.setZipCode(55555);
        home.setBathrooms(2d);
        home.setHomeStatus(HomeStatus.AVAILABLE);
        home.setEmail(USER);

        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<Home> responseEntity = restTemplate.postForEntity(MATCH_HOMES_URL, request, Home.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = Objects.requireNonNull(responseEntity.getHeaders().get("Location")).get(0);
        Path path = Paths.get(location);
        String id = path.getFileName().toString();
        home.setId(id);
        assertThat(homeService.findById(id)).isEqualTo(home);
    }

    @Test
    public void testGetHomeById_success() {

        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<Home> responseEntity = restTemplate.exchange(String.format("%s/%s", MATCH_HOMES_URL, id), HttpMethod.GET, request, Home.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(homeService.findById(id)).isEqualTo(home);
    }

    @Test
    public void testGetHomeById_not_found() {

        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(String.format("%s/xxx", MATCH_HOMES_URL), HttpMethod.GET, request, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).contains(ErrorType.NOT_FOUND_ERROR.getDetail())).isTrue();
    }

    @Test
    public void testSearchHomes_success() {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(MATCH_HOMES_URL)
                // Add query parameter
                .queryParam("email", USER)
                .queryParam("zipCode", 55555);
        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<Home[]> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Home[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(1);
    }

    @Test(expected = MatchHomeNotFoundException.class)
    public void testDeleteHomeById_success() {

        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<Home> responseEntity = restTemplate.exchange(String.format("%s/%s", MATCH_HOMES_URL, id), HttpMethod.DELETE, request, Home.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        homeService.findById(id);
    }

    @Test(expected = MatchHomeNotFoundException.class)
    public void testDeleteHomeByEmail_success() {

        HttpEntity<Home> request = new HttpEntity<>(home, httpHeaders);
        ResponseEntity<Home> responseEntity = restTemplate.exchange(String.format("%s/email/%s", MATCH_HOMES_URL, USER), HttpMethod.DELETE, request, Home.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        homeService.findById(USER);
    }

}