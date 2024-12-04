package com.almonium.user.core.dto;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SubscriptionInfoDto {
    String name;
    Map<PlanFeature, Integer> limits;
    Boolean autoRenewal;
    Plan.Type type;
    Instant startDate;
    Instant endDate;
}
