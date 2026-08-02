package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.subscription.dto.response.PlanDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PlanSubscriptionServiceTest {
    @Mock
    PaddleApiService paddleApiService;

    @Mock
    PaddlePriceCatalog paddlePriceCatalog;

    @Mock
    FoundingMemberService foundingMemberService;

    @Mock
    PlanService planService;

    @Mock
    PlanSubscriptionRepository subscriptionRepository;

    @Mock
    PlanRepository planRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    PlanSubscriptionService service;
    User user;
    Plan premium;

    @BeforeEach
    void setUp() {
        service = new PlanSubscriptionService(
                paddleApiService,
                paddlePriceCatalog,
                foundingMemberService,
                planService,
                subscriptionRepository,
                planRepository,
                userRepository,
                eventPublisher);
        user = User.builder()
                .id(UUID.randomUUID())
                .paddleCustomerId("ctm_01test")
                .build();
        Plan free = Plan.builder().id(1L).name("FREE").type(Plan.Type.LIFETIME).build();
        premium = Plan.builder().id(2L).name("PREMIUM").type(Plan.Type.MONTHLY).build();
        PlanSubscription freeSubscription = PlanSubscription.builder()
                .user(user)
                .plan(free)
                .status(PlanSubscription.Status.ACTIVE)
                .build();
        when(planRepository.findById(2L)).thenReturn(Optional.of(premium));
        when(planService.getAvailableRecurringPremiumPlans())
                .thenReturn(List.of(new PlanDto(2L, "PREMIUM", Plan.Type.MONTHLY, "Premium", 12.0)));
        when(subscriptionRepository.findByUserAndStatusIn(
                        user, List.of(PlanSubscription.Status.ACTIVE, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END)))
                .thenReturn(Optional.of(freeSubscription));
        when(planService.isPlanDefault(1L)).thenReturn(true);
    }

    @Test
    void reservesFounderSlotBeforeCreatingCheckout() {
        when(foundingMemberService.reserveForCheckout(user)).thenReturn(Optional.of(3));
        when(paddleApiService.createPaymentTransaction(user, premium, Optional.of(3)))
                .thenReturn(new PaddleApiService.CheckoutTransaction("txn_01test", "https://checkout.test"));

        assertThat(service.initiatePlanSubscribing(user, 2L, true)).isEqualTo("https://checkout.test");

        verify(foundingMemberService).attachTransaction(3, user.getId(), "txn_01test");
    }

    @Test
    void doesNotSilentlyChargeRegularPriceWhenFounderOfferSoldOut() {
        when(foundingMemberService.reserveForCheckout(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.initiatePlanSubscribing(user, 2L, true))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("no longer available");

        verifyNoInteractions(paddleApiService);
    }
}
