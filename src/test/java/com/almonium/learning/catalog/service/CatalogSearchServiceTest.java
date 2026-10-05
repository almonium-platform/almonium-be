package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.CatalogHit.CatalogState;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ReadingRights;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CatalogSearchServiceTest {
    CatalogStore store = mock(CatalogStore.class);
    BookRepository books = mock(BookRepository.class);
    ReadingRights readingRights = mock(ReadingRights.class);
    CatalogSearchService service = new CatalogSearchService(store, books, readingRights);

    static Hit hit(String title, String workSlug, RightsStatus status) {
        return new Hit(UUID.randomUUID(), title, null, null, 1900, "Someone", 10, workSlug, status, null, 1.0);
    }

    @Test
    void eachHitSaysWhatTheReaderCanDoWithIt() {
        when(store.search("prince", 20))
                .thenReturn(List.of(
                        hit("On the shelf", "readable", RightsStatus.FREE),
                        hit("Hidden here", "hidden", RightsStatus.FREE),
                        hit("Linked but withdrawn", "withdrawn", RightsStatus.FREE),
                        hit("Wantable", null, RightsStatus.UNKNOWN),
                        hit("Protected", null, RightsStatus.PROTECTED)));
        when(books.findByWorkSlug("readable")).thenReturn(List.of(new Book()));
        when(books.findByWorkSlug("hidden")).thenReturn(List.of(new Book()));
        when(books.findByWorkSlug("withdrawn")).thenReturn(List.of());
        when(readingRights.mayRead("readable")).thenReturn(true);
        when(readingRights.mayRead("hidden")).thenReturn(false);

        assertThat(service.search("Prince", 20))
                .extracting(CatalogHit::state)
                .containsExactly(
                        CatalogState.ON_SHELF,
                        CatalogState.NOT_IN_YOUR_COUNTRY,
                        CatalogState.WANTABLE,
                        CatalogState.WANTABLE,
                        CatalogState.UNDER_COPYRIGHT);
    }

    @Test
    void aQueryTooShortToFoldToTwoCharactersFindsNothing() {
        assertThat(service.search(" ! ", 20)).isEmpty();
        assertThat(service.search("a", 20)).isEmpty();
    }
}
