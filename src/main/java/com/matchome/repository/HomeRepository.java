package com.matchome.repository;

import com.matchome.model.Home;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface HomeRepository extends CrudRepository<Home, String> {
    List<Home> findAll();
    void deleteByEmail(String email);
}