package com.impacttechs.assignment.cdrservice.repository;

import com.impacttechs.assignment.cdrservice.entity.MultiTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiTenantRepository extends JpaRepository<MultiTenant, String> {
}
