package com.almonium.subscription;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.dto.PlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PlanSubscriptionMapper {

    @Mapping(target = "name", source = "plan.name")
    @Mapping(target = "type", source = "plan.type")
    @Mapping(target = "limits", ignore = true)
    PlanDto planSubscriptionToPlanDto(PlanSubscription planSubscription);
}
