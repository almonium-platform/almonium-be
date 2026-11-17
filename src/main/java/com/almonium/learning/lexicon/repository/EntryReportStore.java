package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.dto.response.EntryReportGroup;
import com.almonium.learning.lexicon.model.enums.EntryReportReason;
import com.github.f4b6a3.uuid.UuidCreator;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The entry_report table. Languages are kept lower-case, as the lexicon spells them; the queue hands them back
 * upper-case, as the {@code Language} constant does.
 */
@Repository
@RequiredArgsConstructor
public class EntryReportStore {
    static final String OPEN = "OPEN";
    static final String RESOLVED = "RESOLVED";

    private final NamedParameterJdbcTemplate jdbc;

    /** The lexeme a report is about and the version of its entry the reporter was shown. */
    public record Target(UUID lexemeId, String headword, int entryVersion) {}

    public record NewReport(
            String language,
            Target target,
            UUID senseKey,
            EntryReportReason reason,
            String comment,
            String context,
            String book,
            UUID reporterId) {}

    public record OpenReport(
            String language,
            String headword,
            UUID senseKey,
            EntryReportReason reason,
            String comment,
            String context,
            String book,
            int entryVersion,
            Instant createdAt) {}

    public Optional<Target> senseTarget(String language, UUID senseKey) {
        return jdbc
                .query(
                        """
                        select l.id, l.headword, l.entry_version
                        from lexicon.sense s join lexicon.lexeme l on l.id = s.lexeme_id
                        where s.sense_key = :key and l.language = :language
                        """,
                        new MapSqlParameterSource("key", senseKey).addValue("language", language),
                        (row, index) -> new Target(
                                row.getObject("id", UUID.class),
                                row.getString("headword"),
                                row.getInt("entry_version")))
                .stream()
                .findFirst();
    }

    /** The most common lexeme written under, or lemmatised to, this headword. */
    public Optional<Target> wordTarget(String language, String headword) {
        return jdbc
                .query(
                        """
                        select id, headword, entry_version from lexicon.lexeme
                        where language = :language and (headword = :headword or lemma = :headword)
                        order by zipf desc nulls last, homograph_no
                        limit 1
                        """,
                        new MapSqlParameterSource("language", language).addValue("headword", headword),
                        (row, index) -> new Target(
                                row.getObject("id", UUID.class),
                                row.getString("headword"),
                                row.getInt("entry_version")))
                .stream()
                .findFirst();
    }

    public UUID insert(NewReport report, Instant now) {
        UUID id = UuidCreator.getTimeOrderedEpoch();
        jdbc.update(
                """
                insert into lexicon.entry_report (id, language, headword, lexeme_id, sense_key, entry_version, reason,
                    comment, context, book, reporter_id, status, created_at)
                values (:id, :language, :headword, :lexemeId, :senseKey, :entryVersion, :reason,
                    :comment, :context, :book, :reporterId, :status, :createdAt)
                """,
                new MapSqlParameterSource("id", id)
                        .addValue("language", report.language())
                        .addValue("headword", report.target().headword())
                        .addValue("lexemeId", report.target().lexemeId())
                        .addValue("senseKey", report.senseKey())
                        .addValue("entryVersion", report.target().entryVersion())
                        .addValue("reason", report.reason().name())
                        .addValue("comment", report.comment())
                        .addValue("context", report.context())
                        .addValue("book", report.book())
                        .addValue("reporterId", report.reporterId())
                        .addValue("status", OPEN)
                        .addValue("createdAt", Timestamp.from(now)));
        return id;
    }

    /**
     * The open reports grouped by headword, sense and reason, the most common word first. A sense a rewrite dropped
     * still groups by its key, with no definition.
     */
    public List<EntryReportGroup> openGroups() {
        return jdbc.query(
                """
                select upper(r.language) as language, r.headword, r.sense_key, r.reason,
                       count(*) as reports,
                       count(distinct r.reporter_id) as reporters,
                       max(r.entry_version) as entry_version,
                       min(r.created_at) as first_at,
                       max(r.created_at) as last_at,
                       (select s.definition from lexicon.sense s where s.sense_key = r.sense_key) as definition,
                       (select max(l.zipf) from lexicon.lexeme l
                         where l.language = r.language and l.headword = r.headword) as zipf,
                       coalesce(
                           (select l.entry_version from lexicon.sense s join lexicon.lexeme l on l.id = s.lexeme_id
                             where s.sense_key = r.sense_key),
                           (select max(l.entry_version) from lexicon.lexeme l
                             where l.language = r.language and l.headword = r.headword),
                           0) as current_version
                from lexicon.entry_report r
                where r.status = 'OPEN'
                group by r.language, r.headword, r.sense_key, r.reason
                order by zipf desc nulls last, reports desc, last_at desc
                """,
                (row, index) -> new EntryReportGroup(
                        row.getString("language"),
                        row.getString("headword"),
                        row.getObject("sense_key", UUID.class),
                        row.getString("definition"),
                        EntryReportReason.valueOf(row.getString("reason")),
                        row.getLong("reports"),
                        row.getLong("reporters"),
                        row.getObject("zipf", BigDecimal.class),
                        row.getInt("entry_version"),
                        row.getInt("current_version"),
                        row.getTimestamp("first_at").toInstant(),
                        row.getTimestamp("last_at").toInstant(),
                        List.of()));
    }

    public List<OpenReport> openReports() {
        return jdbc.query(
                """
                select upper(language) as language, headword, sense_key, reason, comment, context, book,
                       entry_version, created_at
                from lexicon.entry_report
                where status = 'OPEN'
                order by created_at desc
                """,
                (row, index) -> new OpenReport(
                        row.getString("language"),
                        row.getString("headword"),
                        row.getObject("sense_key", UUID.class),
                        EntryReportReason.valueOf(row.getString("reason")),
                        row.getString("comment"),
                        row.getString("context"),
                        row.getString("book"),
                        row.getInt("entry_version"),
                        row.getTimestamp("created_at").toInstant()));
    }

    /** Closes every open report of one group; returns how many it closed. */
    public int resolve(String language, String headword, UUID senseKey, EntryReportReason reason, Instant now) {
        return jdbc.update(
                """
                update lexicon.entry_report set status = :resolved, handled_at = :now
                where status = 'OPEN' and language = :language and headword = :headword and reason = :reason
                  and sense_key is not distinct from cast(:senseKey as uuid)
                """,
                new MapSqlParameterSource("resolved", RESOLVED)
                        .addValue("now", Timestamp.from(now))
                        .addValue("language", language)
                        .addValue("headword", headword)
                        .addValue("reason", reason.name())
                        .addValue("senseKey", senseKey));
    }
}
