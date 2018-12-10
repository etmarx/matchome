package com.matchome.service;

import com.matchome.exception.MatchHomeInvalidEmailAddressException;
import com.matchome.exception.MatchHomeNotFoundException;
import com.matchome.model.Home;
import com.matchome.model.HomeStatus;
import com.matchome.repository.HomeRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.QueryTimeoutException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeServiceTest {

    @Mock
    private HomeRepository homeRepository;

    @InjectMocks
    private HomeService homeService;

    private final Home home = new Home();
    private final String id = UUID.randomUUID().toString();
    private Home savedHome;

    @Before
    public void setup() {
        home.setEmail("test@test.com");
        home.setHomeStatus(HomeStatus.AVAILABLE);
        home.setZipCode(98006);
        home.setNeighborhood("The Hills");
        home.setBedrooms(5);
        home.setBathrooms(3.5);
        savedHome = SerializationUtils.clone(home);
        savedHome.setId(id);
       when(homeRepository.save(home)).thenReturn(savedHome);
    }


    @Test
    public void testSaveHome_success() {

        assertThat(homeService.saveHome(home)).isEqualTo(savedHome.getId());
    }


    @Test(expected = MatchHomeInvalidEmailAddressException.class)
    public void testSaveHome_invalidEmail() {

        home.setEmail("xxx");
        homeService.saveHome(home);
    }

    @Test
    public void testFindById_success() {
        when(homeRepository.findById(savedHome.getId().toString())).thenReturn(Optional.of(savedHome));
        assertThat(homeService.findById(savedHome.getId())).isEqualTo(savedHome);
    }

    @Test(expected = QueryTimeoutException.class)
    public void testFindById_data_error() {
        when(homeRepository.findById(savedHome.getId().toString())).thenThrow(new QueryTimeoutException("timeout"));
        homeService.findById(savedHome.getId());
    }

    @Test(expected = MatchHomeNotFoundException.class)
    public void testFindById_not_found() {
        when(homeRepository.findById(savedHome.getId().toString())).thenReturn(Optional.empty());
        homeService.findById(savedHome.getId());
    }

    @Test
    public void testFindAllMatchingHomes_success() {
        Home home1 = Home.builder().neighborhood("Lake").bedrooms(5).bathrooms(3.5).build();
        Home search = Home.builder().neighborhood("The Hills").bedrooms(5).bathrooms(3.5).build();
        when(homeRepository.findAll()).thenReturn(Arrays.asList(savedHome, home1));
        assertThat(homeService.findAllMatchingHomes(search).size()).isEqualTo(1);
    }

    @Test
    public void testFindAllMatchingHomes_no_match() {
        Home home1 = Home.builder().neighborhood("Lake").bedrooms(5).bathrooms(3.5).build();
        Home search = Home.builder().neighborhood("The Hills").bedrooms(3).bathrooms(3.5).build();
        when(homeRepository.findAll()).thenReturn(Arrays.asList(savedHome, home1));
        assertThat(homeService.findAllMatchingHomes(search).isEmpty()).isTrue();
    }

    @Test
    public void test_DeleteById_success() {
        homeService.deleteById(savedHome.getId());
        Mockito.verify(homeRepository, times(1)).deleteById(savedHome.getId().toString());
    }

    @Test
    public void testDeleteByEmail_success() {
        homeService.deleteByEmail(home.getEmail());
        Mockito.verify(homeRepository, times(1)).deleteByEmail(home.getEmail());
    }
}