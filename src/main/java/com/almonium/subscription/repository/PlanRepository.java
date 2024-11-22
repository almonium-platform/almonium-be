package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.Plan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByStripePriceId(String stripePlanId);

    Optional<Plan> findByName(String defaultPlanName);
}
