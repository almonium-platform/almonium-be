package com.almonium.learning.rights.service;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.CatalogImportedEvent;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.engine.CountryTerms;
import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.entity.RightsOverride;
import com.almonium.learning.rights.model.entity.WorkRightsBlock;
import com.almonium.learning.rights.model.enums.BlockSource;
import com.almonium.learning.rights.model.enums.RightsEffect;
import com.almonium.learning.rights.repository.RightsOverrideRepository;
import com.almonium.learning.rights.repository.WorkRightsBlockRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Which countries each shelf work is hidden in, and why. The list is derived from three things and recomputed when
 * any of them changes: the copyright engine's verdict per country, the production countries (a work protected where
 * we make or host books is hidden everywhere), and /ops overrides, which win. Reader requests check an in-memory copy
 * that every slot reloads each minute, so a takedown reaches every reader within a minute.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkRightsService {
    private final CopyrightEngine engine;
    private final ProductionRights productionRights;
    private final CatalogWorkRepository catalogWorkRepository;
    private final BookRepository bookRepository;
    private final RightsOverrideRepository overrideRepository;
    private final WorkRightsBlockRepository blockRepository;
    private final TransactionTemplate transactionTemplate;

    private volatile Map<String, Set<String>> blockedByWork = Map.of();

    public boolean readable(String workSlug, String country) {
        if (workSlug == null) {
            return true;
        }
        Set<String> blocked = blockedByWork.getOrDefault(workSlug, Set.of());
        return !blocked.contains(WorkRightsBlock.EVERYWHERE) && !blocked.contains(country);
    }

    /** The countries a work is hidden in, sorted, {@code *} meaning everywhere. */
    public Set<String> blockedCountries(String workSlug) {
        return blockedByWork.getOrDefault(workSlug, Set.of());
    }

    /** The facts a shelf work is judged by: its linked catalogue work's, else its own edition's. */
    public ShelfWorkFacts factsFor(String workSlug) {
        Optional<CatalogWork> linked = catalogWorkRepository.findByWorkSlug(workSlug).stream()
                .max(Comparator.comparingInt(CatalogWork::getPopularity));
        if (linked.isPresent()) {
            return new ShelfWorkFacts(workSlug, linked.get().rightsFacts(), linked.get());
        }
        Book representative = bookRepository.findByWorkSlug(workSlug).stream()
                .min(Comparator.comparing((Book book) -> book.getOriginalBook() != null))
                .orElse(null);
        if (representative == null) {
            return new ShelfWorkFacts(workSlug, new RightsFacts(null, List.of()), null);
        }
        Integer year = representative.getPublicationYear() > 0 ? representative.getPublicationYear() : null;
        List<AuthorFacts> authors =
                representative.getAuthor() == null || representative.getAuthor().isBlank()
                        ? List.of()
                        : List.of(AuthorFacts.of(representative.getAuthor(), null, null));
        return new ShelfWorkFacts(workSlug, new RightsFacts(year, authors), null);
    }

    /**
     * The block list for one work as the engine and the overrides make it: country to source. Production comes
     * first: protected in a production country that no override allows means hidden everywhere.
     */
    public Map<String, BlockSource> computeBlocks(String workSlug) {
        RightsFacts facts = factsFor(workSlug).facts();
        int year = productionRights.currentYear();
        List<RightsOverride> overrides = overrideRepository.findByWorkSlug(workSlug);
        Map<String, RightsEffect> byCountry = new HashMap<>();
        overrides.forEach(override -> byCountry.put(override.getCountry(), override.getEffect()));

        Map<String, BlockSource> blocks = new TreeMap<>();
        boolean allowedEverywhere = byCountry.get(WorkRightsBlock.EVERYWHERE) == RightsEffect.ALLOW;
        if (!allowedEverywhere) {
            boolean productionBlocked = productionRights.getCountries().stream()
                    .anyMatch(country -> engine.verdict(facts, country, year).status() == RightsStatus.PROTECTED
                            && byCountry.get(country) != RightsEffect.ALLOW);
            if (productionBlocked) {
                blocks.put(WorkRightsBlock.EVERYWHERE, BlockSource.ENGINE);
            }
            for (String country : CountryTerms.countries()) {
                if (engine.verdict(facts, country, year).status() == RightsStatus.PROTECTED) {
                    blocks.put(country, BlockSource.ENGINE);
                }
            }
        }
        if (byCountry.get(WorkRightsBlock.EVERYWHERE) == RightsEffect.BLOCK) {
            blocks.put(WorkRightsBlock.EVERYWHERE, BlockSource.OVERRIDE);
        }
        byCountry.forEach((country, effect) -> {
            if (WorkRightsBlock.EVERYWHERE.equals(country)) {
                return;
            }
            if (effect == RightsEffect.BLOCK) {
                blocks.put(country, BlockSource.OVERRIDE);
            } else {
                blocks.remove(country);
            }
        });
        return blocks;
    }

    public void recompute(String workSlug) {
        Map<String, BlockSource> blocks = computeBlocks(workSlug);
        transactionTemplate.executeWithoutResult(status -> {
            blockRepository.deleteByWorkSlug(workSlug);
            blockRepository.flush();
            List<WorkRightsBlock> rows = new ArrayList<>();
            blocks.forEach((country, source) -> rows.add(new WorkRightsBlock(workSlug, country, source)));
            blockRepository.saveAll(rows);
        });
        Map<String, Set<String>> next = new HashMap<>(blockedByWork);
        next.put(workSlug, Set.copyOf(blocks.keySet()));
        blockedByWork = Map.copyOf(next);
    }

    /** Every work on the shelf or under an override; nightly, after an import, and on demand from /ops. */
    public int recomputeAll() {
        Set<String> slugs = new LinkedHashSet<>();
        bookRepository.findAll().stream()
                .map(Book::getWorkSlug)
                .filter(slug -> slug != null)
                .forEach(slugs::add);
        overrideRepository.findAll().forEach(override -> slugs.add(override.getWorkSlug()));
        slugs.forEach(this::recompute);
        reload();
        return slugs.size();
    }

    @EventListener
    public void onCatalogImported(CatalogImportedEvent event) {
        int works = recomputeAll();
        log.info("Recomputed reading rights for {} shelf works after a catalogue import", works);
    }

    @Scheduled(cron = "0 20 0 * * ?", zone = "UTC")
    public void nightly() {
        recomputeAll();
    }

    /** Picks up blocks the other application slot wrote. */
    @Scheduled(fixedDelayString = "${app.rights.reload-interval:PT1M}", initialDelay = 0)
    public void reload() {
        Map<String, Set<String>> next = new HashMap<>();
        for (WorkRightsBlock block : blockRepository.findAll()) {
            next.computeIfAbsent(block.getWorkSlug(), slug -> new LinkedHashSet<>())
                    .add(block.getCountry());
        }
        Map<String, Set<String>> frozen = new HashMap<>();
        next.forEach((slug, countries) -> frozen.put(slug, Set.copyOf(countries)));
        blockedByWork = Map.copyOf(frozen);
    }
}
