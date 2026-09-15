package com.almonium.user.core.dto.response;

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
    boolean autoRenewal;
    Map<PlanFeature, Integer> limits;
    Plan.Type type;
    Instant startDate;
    Instant endDate;
    /**
     * Whether this member holds a founding-member place. Screens that promise the price is locked, or warn that it
     * ends with the subscription, need to know before they can say either.
     */
    boolean founder;
    /** Present only while a cadence change is pending, which is the only time the settings row exists. */
    ScheduledCadenceChange scheduledChange;

    public record ScheduledCadenceChange(Plan.Type type, Instant effectiveAt) {}
}
