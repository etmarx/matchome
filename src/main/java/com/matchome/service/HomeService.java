package com.matchome.service;

import com.matchome.errors.ErrorType;
import com.matchome.exception.MatchHomeInvalidEmailAddressException;
import com.matchome.exception.MatchHomeNotFoundException;
import com.matchome.model.Home;
import com.matchome.repository.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.matchome.errors.ErrorResponseUtil.logAndGetErrorResponse;

@Slf4j
@Component
public class HomeService {

    private final HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    /**
     * Saves home.
     *
     * @param home home object
     * @return String (UUID) home id
     */
    public String saveHome(Home home) {

        if (home.getEmail() != null && !EmailValidator.getInstance().isValid(home.getEmail())) {
            throw new MatchHomeInvalidEmailAddressException(logAndGetErrorResponse(ErrorType.INVALID_EMAIL));
        }

        return homeRepository.save(home).getId();
    }


    /**
     * Retrieves home by id.
     *
     * @param id home id
     * @return Home object
     */
    public Home findById(String id) {
        return homeRepository.findById(id).orElseThrow(() -> new MatchHomeNotFoundException(
                logAndGetErrorResponse(ErrorType.NOT_FOUND_ERROR)));
    }

    /**
     * Retrieves list of home that match search.
     *
     * @param home home search criteria
     * @return List<Home> homes matching search criteria
     */
    public List<Home> findAllMatchingHomes(Home home) {
        return findAllHomes().stream()
                .filter(h -> home.getZipCode() == null || home.getZipCode().equals(h.getZipCode()))
                .filter(h -> home.getHomeStatus() == null || home.getHomeStatus().equals(h.getHomeStatus()))
                .filter(h -> home.getEmail() == null || home.getEmail().equals(h.getEmail()))
                .filter(h -> home.getNeighborhood() == null || home.getNeighborhood().equals(h.getNeighborhood()))
                .filter(h -> home.getBedrooms() == null || home.getBedrooms().equals(h.getBedrooms()))
                .filter(h -> home.getBathrooms() == null || home.getBathrooms().equals(h.getBathrooms()))
                .collect(Collectors.toList());
    }

    /**
     * Deletes home by id.
     *
     * @param id home id
     */
    public void deleteById(String id) {
        homeRepository.deleteById(id);
    }

    /**
     * Deletes home by email.
     *
     * @param email home email
     */
    public void deleteByEmail(String email) {
        homeRepository.deleteByEmail(email);
    }

    private List<Home> findAllHomes() {
        return homeRepository.findAll();
    }


}
