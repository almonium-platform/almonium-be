package com.almonium.learning.rights.service;

import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.service.CatalogMatcher;
import com.almonium.learning.rights.dto.response.ProductionCheck;
import com.almonium.learning.rights.exception.ProductionBlockedException;
import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.entity.RightsOverride;
import com.almonium.learning.rights.model.entity.WorkRightsBlock;
import com.almonium.learning.rights.model.enums.RightsEffect;
import com.almonium.learning.rights.repository.RightsOverrideRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * May this work be prepared and published at all: free in Ukraine, where editions are made, and in Germany, where
 * they are hosted. The book processor asks before it spends anything on a book, and publication asks again. A work
 * the facts cannot settle passes: refusing every unknown would refuse most of Gutenberg. An /ops ALLOW for the
 * production countries (or everywhere) overrides a protected verdict.
 */
@Service
@RequiredArgsConstructor
public class ProductionGate {
    private final ProductionRights productionRights;
    private final CatalogMatcher catalogMatcher;
    private final RightsOverrideRepository overrideRepository;

    public ProductionCheck check(String workSlug, String title, String author, Integer publicationYear) {
        Optional<CatalogWork> work = workSlug == null
                ? catalogMatcher.byTitleAndAuthor(title, author)
                : catalogMatcher.linkOrMatch(workSlug, title, author);
        RightsFacts facts = work.map(CatalogWork::rightsFacts)
                .orElseGet(() -> new RightsFacts(
                        publicationYear != null && publicationYear > 0 ? publicationYear : null,
                        author == null || author.isBlank() ? List.of() : List.of(AuthorFacts.of(author, null, null))));
        CountryVerdict verdict = productionRights.verdict(facts);
        RightsStatus status = verdict.status();
        if (status == RightsStatus.PROTECTED && allowedByOverride(workSlug)) {
            status = RightsStatus.FREE;
        }
        return new ProductionCheck(
                status,
                verdict.shownYear(),
                productionRights.getCountries(),
                work.map(CatalogWork::getId).orElse(null),
                work.map(CatalogWork::getTitle).orElse(null),
                work.map(CatalogWork::getAuthorDisplay).orElse(null),
                explain(status, verdict, work.isPresent()));
    }

    /** Throws when the work may not be published; used by publication itself. */
    public void requirePublishable(String workSlug, String title, String author, Integer publicationYear) {
        ProductionCheck check = check(workSlug, title, author, publicationYear);
        if (check.status() == RightsStatus.PROTECTED) {
            throw new ProductionBlockedException(check.explanation());
        }
    }

    private boolean allowedByOverride(String workSlug) {
        if (workSlug == null) {
            return false;
        }
        Set<String> allowed = overrideRepository.findByWorkSlug(workSlug).stream()
                .filter(override -> override.getEffect() == RightsEffect.ALLOW)
                .map(RightsOverride::getCountry)
                .collect(Collectors.toSet());
        return allowed.contains(WorkRightsBlock.EVERYWHERE) || allowed.containsAll(productionRights.getCountries());
    }

    private String explain(RightsStatus status, CountryVerdict verdict, boolean fromCatalogue) {
        String where = String.join(" and ", productionRights.getCountries());
        String basis = fromCatalogue ? "the catalogue's dates" : "the edition's own details only";
        return switch (status) {
            case FREE -> "Free to prepare in " + where + " (by " + basis + ").";
            case UNKNOWN ->
                "Could not confirm the copyright status in " + where + " from " + basis
                        + "; it may be prepared, but check the author's death year.";
            case PROTECTED ->
                "Under copyright in " + where
                        + (verdict.freeFrom().earliest() == null
                                ? ""
                                : (verdict.freeFrom()
                                                        .earliest()
                                                        .equals(verdict.freeFrom()
                                                                .latest())
                                                ? " until the end of "
                                                : " until at least the end of ")
                                        + (verdict.freeFrom().earliest() - 1))
                        + " (by " + basis + ").";
        };
    }
}
