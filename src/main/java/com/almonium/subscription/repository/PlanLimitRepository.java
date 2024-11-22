package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.PlanLimit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanLimitRepository extends JpaRepository<PlanLimit, Long> {

    List<PlanFeatureLimit> findByPlanId(long planId);
}
