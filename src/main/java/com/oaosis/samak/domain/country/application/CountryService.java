package com.oaosis.samak.domain.country.application;

import com.oaosis.samak.domain.country.repository.CountryMetricsSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryService {
}