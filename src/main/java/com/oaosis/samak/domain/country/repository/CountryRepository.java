package com.oaosis.samak.domain.country.repository;

import com.oaosis.samak.domain.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCode(String code);
}