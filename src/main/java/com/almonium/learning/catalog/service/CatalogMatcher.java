package com.almonium.learning.catalog.service;

import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Finds the catalogue work an edition is of: the work already linked to its work slug, else the best-known work with
 * exactly its title (in any language the catalogue holds) and its author's surname among the work's authors.
 */
@Component
@RequiredArgsConstructor
public class CatalogMatcher {
    private final CatalogStore store;
    private final CatalogWorkRepository catalogWorkRepository;

    public Optional<CatalogWork> linked(String workSlug) {
        if (workSlug == null) {
            return Optional.empty();
        }
        return catalogWorkRepository.findByWorkSlug(workSlug).stream()
                .max(Comparator.comparingInt(CatalogWork::getPopularity));
    }

    /** An unlinked catalogue work matching the title and author, best known first. */
    public Optional<CatalogWork> byTitleAndAuthor(String title, String author) {
        String surname = surname(author);
        return store.exactTitle(TitleFolding.fold(title)).stream()
                .filter(hit -> hit.workSlug() == null)
                .filter(hit -> surname.isEmpty()
                        || (" " + TitleFolding.fold(hit.authorDisplay()) + " ").contains(" " + surname + " "))
                .findFirst()
                .map(Hit::id)
                .flatMap(catalogWorkRepository::findById);
    }

    /** The linked work, else a match by title and author, which is then linked to the slug. */
    public Optional<CatalogWork> linkOrMatch(String workSlug, String title, String author) {
        Optional<CatalogWork> linked = linked(workSlug);
        if (linked.isPresent()) {
            return linked;
        }
        Optional<CatalogWork> match = byTitleAndAuthor(title, author);
        match.ifPresent(work -> {
            if (workSlug != null) {
                store.linkWorkSlug(work.getId(), workSlug);
                work.setWorkSlug(workSlug);
            }
        });
        return match;
    }

    static String surname(String author) {
        String folded = TitleFolding.fold(author);
        int space = folded.lastIndexOf(' ');
        return space < 0 ? folded : folded.substring(space + 1);
    }
}
