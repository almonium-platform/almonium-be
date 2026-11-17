package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.request.EntryReportRequest;
import com.almonium.learning.lexicon.dto.request.EntryReportResolveRequest;
import com.almonium.learning.lexicon.dto.response.EntryReportGroup;
import com.almonium.learning.lexicon.dto.response.EntryReportReceipt;
import com.almonium.learning.lexicon.repository.EntryReportStore;
import com.almonium.learning.lexicon.repository.EntryReportStore.OpenReport;
import com.almonium.learning.lexicon.repository.EntryReportStore.Target;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The flag on a dictionary entry: a member says a sense, or the whole word, is wrong in one of five ways. The server
 * attaches the lexeme and the entry version from what was flagged, so a report is usable without a comment. Nothing
 * changes in the entry: reports wait in the /ops queue until an operator resolves them.
 */
@Service
@RequiredArgsConstructor
public class EntryReportService {
    /** How many reports of one group the queue shows; the counts cover all of them. */
    static final int ITEMS_PER_GROUP = 5;

    private final EntryReportStore store;
    private final Clock clock;

    public EntryReportReceipt report(User reporter, EntryReportRequest request) {
        String language = request.language().name().toLowerCase(Locale.ROOT);
        String headword = request.headword().trim();
        Target target = (request.senseKey() != null
                        ? store.senseTarget(language, request.senseKey())
                        : store.wordTarget(language, headword))
                .orElseThrow(() -> new EntityNotFoundException("No dictionary entry to report."));
        UUID id = store.insert(
                new EntryReportStore.NewReport(
                        language,
                        target,
                        request.senseKey(),
                        request.reason(),
                        blankToNull(request.comment()),
                        blankToNull(request.context()),
                        blankToNull(request.book()),
                        reporter.getId()),
                clock.instant());
        return new EntryReportReceipt(id);
    }

    public List<EntryReportGroup> queue() {
        Map<GroupKey, List<EntryReportGroup.Item>> items = new LinkedHashMap<>();
        for (OpenReport report : store.openReports()) {
            List<EntryReportGroup.Item> list = items.computeIfAbsent(
                    new GroupKey(
                            report.language(),
                            report.headword(),
                            report.senseKey(),
                            report.reason().name()),
                    key -> new ArrayList<>());
            if (list.size() < ITEMS_PER_GROUP) {
                list.add(new EntryReportGroup.Item(
                        report.comment(), report.context(), report.book(), report.entryVersion(), report.createdAt()));
            }
        }
        return store.openGroups().stream()
                .map(group -> new EntryReportGroup(
                        group.language(),
                        group.headword(),
                        group.senseKey(),
                        group.definition(),
                        group.reason(),
                        group.reports(),
                        group.reporters(),
                        group.zipf(),
                        group.entryVersion(),
                        group.currentVersion(),
                        group.firstAt(),
                        group.lastAt(),
                        items.getOrDefault(
                                new GroupKey(
                                        group.language(),
                                        group.headword(),
                                        group.senseKey(),
                                        group.reason().name()),
                                List.of())))
                .toList();
    }

    public int resolve(EntryReportResolveRequest request) {
        return store.resolve(
                request.language().toLowerCase(Locale.ROOT),
                request.headword(),
                request.senseKey(),
                request.reason(),
                clock.instant());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private record GroupKey(String language, String headword, UUID senseKey, String reason) {}
}
