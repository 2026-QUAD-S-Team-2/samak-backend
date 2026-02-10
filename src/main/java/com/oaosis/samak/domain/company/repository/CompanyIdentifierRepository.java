package com.oaosis.samak.domain.company.repository;

import com.oaosis.samak.domain.company.entity.CompanyIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyIdentifierRepository extends JpaRepository<CompanyIdentifier, Long> {
}