package com.almonium.learning.catalog.service;

import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.CatalogHit.CatalogState;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.rights.model.RightsStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Search over every notable book (W1): a query always lands on real works, each marked by what the reader can do with
 * it. A work counts as on the shelf only while an edition of it is published.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogSearchService {
    static final int MIN_QUERY = 2;

    private final CatalogStore store;
    private final BookRepository bookRepository;

    public List<CatalogHit> search(String query, int limit) {
        String folded = TitleFolding.fold(query);
        if (folded.length() < MIN_QUERY) {
            return List.of();
        }
        return store.search(folded, limit).stream().map(this::toHit).toList();
    }

    private CatalogHit toHit(Hit hit) {
        CatalogState state;
        if (hit.workSlug() != null
                && !bookRepository.findByWorkSlug(hit.workSlug()).isEmpty()) {
            state = CatalogState.ON_SHELF;
        } else if (hit.productionStatus() == RightsStatus.PROTECTED) {
            state = CatalogState.UNDER_COPYRIGHT;
        } else {
            state = CatalogState.WANTABLE;
        }
        return new CatalogHit(
                hit.id(),
                hit.title(),
                hit.originalTitle(),
                hit.originalLanguage(),
                hit.authorDisplay(),
                hit.publicationYear(),
                state,
                hit.workSlug(),
                hit.productionFreeFrom());
    }
}
