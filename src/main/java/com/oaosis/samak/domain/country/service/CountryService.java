package com.oaosis.samak.domain.country.service;

import com.oaosis.samak.domain.country.application.CityListResponse;
import com.oaosis.samak.domain.country.application.CountryListResponse;
import com.oaosis.samak.domain.country.repository.CityRepository;
import com.oaosis.samak.domain.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public List<CountryListResponse> getCountries() {
        return countryRepository.findAll().stream()
                .map(country -> new CountryListResponse(country.getCode(), country.getName()))
                .toList();
    }

    public List<CityListResponse> getCities(String countryCode) {
        return cityRepository.findByCountry_Code(countryCode).stream()
                .map(city -> new CityListResponse(city.getId(), city.getName()))
                .toList();
    }
}