package com.oaosis.samak.domain.country.repository;

import com.oaosis.samak.domain.country.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByCountry_Code(String countryCode);
}