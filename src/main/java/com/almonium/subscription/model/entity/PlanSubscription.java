package com.almonium.subscription.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.model.enums.EmailTemplateType;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PlanSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    Plan plan;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String stripeSubscriptionId;
    Instant startDate;

    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        ACTIVE,
        CANCELED,
        ON_HOLD
    }

    public enum Event implements EmailTemplateType {
        SUBSCRIPTION_CREATED,
        SUBSCRIPTION_UPDATED,
        SUBSCRIPTION_CANCELED,
        SUBSCRIPTION_PAYMENT_FAILED
    }
}
