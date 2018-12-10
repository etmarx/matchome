package com.matchome.controller;

import com.matchome.model.Home;
import com.matchome.model.HomeStatus;
import com.matchome.service.HomeService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@Api(basePath = "/v1/homes",
        description="Add, search and delete homes"
)
@RequestMapping("/v1/homes")
public class MatchHomeController {

    private final HomeService homeService;

    public MatchHomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @Timed(value = "add_home", percentiles = {0.5, 0.95})
    @ApiOperation(value = "add a new home")
    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addHome(@RequestBody Home home,
                                  final HttpServletRequest servletRequest) {

        final String homeId = homeService.saveHome(home);

        return ResponseEntity.created(locationHeader(servletRequest.getRequestURL(), homeId)).build();

    }

    @Timed(value = "search_homes", percentiles = {0.5, 0.95})
    @ApiOperation(value = "search homes")
    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Home>> searchHomes(@RequestParam int zipCode,
                                                  @RequestParam(required=false) HomeStatus homeStatus,
                                                  @RequestParam(required=false) String neighborhood,
                                                  @RequestParam(required=false) Integer bedrooms,
                                                  @RequestParam(required=false) Double bathrooms,
                                                  @RequestParam(required = false) String email) {

        Home home = Home.builder()
                .zipCode(zipCode)
                .homeStatus(homeStatus)
                .neighborhood(neighborhood)
                .bedrooms(bedrooms)
                .bathrooms(bathrooms)
                .email(email)
                .build();

        return ResponseEntity.ok().body(homeService.findAllMatchingHomes(home));
    }

    @Timed(value = "find_home_by_id", percentiles = {0.5, 0.95})
    @ApiOperation(value = "find home by id")
    @RequestMapping(value = "/{id}", method = org.springframework.web.bind.annotation.RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Home> findHome(@PathVariable("id") String id) {

        return ResponseEntity.ok().body(homeService.findById(id));
    }

    @Timed(value = "delete_home_by_email", percentiles = {0.5, 0.95})
    @ApiOperation(value = "delete home by email")
    @RequestMapping(value = "email/{email}", method = org.springframework.web.bind.annotation.RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteHomeByEmail(@PathVariable("email") String email) {

        homeService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @Timed(value = "delete_home_by_id", percentiles = {0.5, 0.95})
    @ApiOperation(value = "delete home by home id")
    @RequestMapping(value = "/{id}", method = org.springframework.web.bind.annotation.RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteHomeById(@PathVariable("id") String id) {

        homeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private URI locationHeader(final StringBuffer uri, final String id) {
        return URI.create(String.format("%s/%s", uri, id));
    }


}
