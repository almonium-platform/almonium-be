package com.almonium.subscription.mapper;

import com.almonium.subscription.dto.response.PlanDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.dto.response.SubscriptionInfoDto;
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
    @Mapping(target = "autoRenewal", ignore = true)
    @Mapping(target = "scheduledChange", ignore = true)
    @Mapping(target = "founder", ignore = true)
    SubscriptionInfoDto planSubscriptionToPlanDto(PlanSubscription planSubscription);

    @AfterMapping
    default void setAutoRenewal(
            @MappingTarget SubscriptionInfoDto.SubscriptionInfoDtoBuilder dtoBuilder,
            PlanSubscription planSubscription) {
        dtoBuilder.autoRenewal(planSubscription.getStatus() == PlanSubscription.Status.ACTIVE);
    }

    /** Both halves have to be there. A pending cadence with no date is a row the client cannot render honestly. */
    @AfterMapping
    default void setScheduledChange(
            @MappingTarget SubscriptionInfoDto.SubscriptionInfoDtoBuilder dtoBuilder,
            PlanSubscription planSubscription) {
        if (planSubscription.getScheduledPlan() == null || planSubscription.getScheduledChangeAt() == null) {
            return;
        }
        dtoBuilder.scheduledChange(new SubscriptionInfoDto.ScheduledCadenceChange(
                planSubscription.getScheduledPlan().getType(), planSubscription.getScheduledChangeAt()));
    }

    /** Limits are resolved per entitlement by the service; the plan's own rows are not the answer on their own. */
    @Mapping(target = "limits", ignore = true)
    PlanDto toDto(Plan planSubscription);

    List<PlanDto> toDto(List<Plan> planSubscriptions);
}
