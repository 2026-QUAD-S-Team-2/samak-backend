package com.oaosis.samak.domain.company.repository;

import com.oaosis.samak.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}