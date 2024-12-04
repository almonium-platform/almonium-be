package com.almonium.subscription.mapper;

import com.almonium.subscription.dto.PlanDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.dto.SubscriptionInfoDto;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PlanSubscriptionMapper {

    @Mapping(target = "name", source = "plan.name")
    @Mapping(target = "type", source = "plan.type")
    @Mapping(target = "limits", ignore = true)
    SubscriptionInfoDto planSubscriptionToPlanDto(PlanSubscription planSubscription);

    @AfterMapping
    default void setAutoRenewal(
            @MappingTarget SubscriptionInfoDto.SubscriptionInfoDtoBuilder dtoBuilder,
            PlanSubscription planSubscription) {
        dtoBuilder.autoRenewal(planSubscription.getStatus() == PlanSubscription.Status.ACTIVE);
    }

    PlanDto toDto(Plan planSubscription);

    List<PlanDto> toDto(List<Plan> planSubscriptions);
}
