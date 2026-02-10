package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.analysis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {
}