package com.oaosis.samak.domain.company.application;

import com.oaosis.samak.domain.guide.repository.GuideCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
}