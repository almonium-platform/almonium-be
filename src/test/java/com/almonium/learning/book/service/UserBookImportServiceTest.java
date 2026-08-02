package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_BOOK_IMPORTS_PER_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.model.entity.UserBookImport;
import com.almonium.learning.book.repository.UserBookImportRepository;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UserBookImportServiceTest {
    @Mock
    UserBookImportRepository repository;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    BookProcessorClient processorClient;

    @Mock
    PublishedBookContentService contentService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    UserBookImportService service;

    @Test
    void enforcesMonthlyPlanLimitAndQueuesProcessorImport() {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(repository.countByUserIdAndCreatedAtGreaterThanEqual(eq(user.getId()), any(Instant.class)))
                .thenReturn(2L);
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "book.epub", "application/epub+zip", new byte[] {1, 2, 3});

        BookImportDto result = service.create(user, source, "My Book", "An Author", "Private", Language.EN, 1920);

        verify(planValidationService).validatePlanFeature(user, MAX_BOOK_IMPORTS_PER_MONTH, 3);
        verify(processorClient)
                .createPrivateImport(
                        eq(result.id()),
                        eq(user.getId()),
                        eq(source),
                        eq("My Book"),
                        eq("An Author"),
                        eq("Private"),
                        eq(Language.EN),
                        eq(1920));
        assertThat(result.status().name()).isEqualTo("QUEUED");
    }
}
