package com.almonium.subscription.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.model.enums.EmailTemplateType;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan_subscription")
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
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

    Instant endDate;

    @LastModifiedDate
    Instant updatedAt;

    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        ACTIVE, // premium plan active and not canceled
        ACTIVE_TILL_CYCLE_END, // premium plan active but canceled by user
        CANCELED, // premium plan canceled by user, plan expired
        INACTIVE, // lifetime plan is inactive
        ON_HOLD // when payment failed
    }

    public enum Event implements EmailTemplateType {
        // remove prefix SUBSCRIPTION_
        SUBSCRIPTION_CREATED,
        SUBSCRIPTION_CANCELED, // premium plan canceled by user, plan is still active till the end of the billing cycle
        SUBSCRIPTION_ENDED, // plan expired
        SUBSCRIPTION_RENEWED,
        SUBSCRIPTION_PAYMENT_FAILED
    }
}
