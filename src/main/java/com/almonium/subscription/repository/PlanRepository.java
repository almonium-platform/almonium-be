package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.Plan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByTypeInAndActiveTrue(List<Plan.Type> types);

    Optional<Plan> findByStripePriceId(String stripePlanId);

    Optional<Plan> findByName(String defaultPlanName);
}
