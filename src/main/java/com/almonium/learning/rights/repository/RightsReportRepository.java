package com.almonium.learning.rights.repository;

import com.almonium.learning.rights.model.entity.RightsReport;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RightsReportRepository extends JpaRepository<RightsReport, UUID> {
    List<RightsReport> findByStatusOrderByCreatedAtDesc(RightsReportStatus status);

    List<RightsReport> findTop100ByOrderByCreatedAtDesc();
}
