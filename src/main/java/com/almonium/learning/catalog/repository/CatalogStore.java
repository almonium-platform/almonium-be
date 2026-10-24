package com.almonium.learning.catalog.repository;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.rights.model.RightsStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

/**
 * The catalogue's bulk and search SQL: hundreds of thousands of rows per import, and a trigram search JPA cannot
 * express. Plain JDBC, batched.
 */
@Repository
@RequiredArgsConstructor
public class CatalogStore {
    private static final int BATCH = 2000;

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate named;
    private final ObjectMapper objectMapper;

    /** A work row as the import writes it. */
    public record WorkRow(
            UUID id,
            String wikidataId,
            Integer gutenbergId,
            String title,
            String originalTitle,
            String originalLanguage,
            Integer publicationYear,
            String authorDisplay,
            List<CatalogAuthor> authors,
            int popularity,
            RightsStatus productionStatus,
            Integer productionFreeFrom,
            String publisher,
            boolean literary) {

        public WorkRow(
                UUID id,
                String wikidataId,
                Integer gutenbergId,
                String title,
                String originalTitle,
                String originalLanguage,
                Integer publicationYear,
                String authorDisplay,
                List<CatalogAuthor> authors,
                int popularity,
                RightsStatus productionStatus,
                Integer productionFreeFrom) {
            this(
                    id,
                    wikidataId,
                    gutenbergId,
                    title,
                    originalTitle,
                    originalLanguage,
                    publicationYear,
                    authorDisplay,
                    authors,
                    popularity,
                    productionStatus,
                    productionFreeFrom,
                    null,
                    false);
        }
    }

    public record TitleRow(UUID workId, String text, String language, String kind, String normalized) {}

    /** A work as search returns it, with how well it matched. */
    public record Hit(
            UUID id,
            String title,
            String originalTitle,
            String originalLanguage,
            Integer publicationYear,
            String authorDisplay,
            int popularity,
            String workSlug,
            RightsStatus productionStatus,
            Integer productionFreeFrom,
            double score) {}

    /** The ids existing works carry, by Wikidata id and by Gutenberg id, so an import keeps them. */
    public Map<String, UUID> idsByWikidata() {
        Map<String, UUID> ids = new HashMap<>();
        jdbc.query("select id, wikidata_id from catalog_work where wikidata_id is not null", row -> {
            ids.put(row.getString(2), row.getObject(1, UUID.class));
        });
        return ids;
    }

    public Map<Integer, UUID> idsByGutenbergOnly() {
        Map<Integer, UUID> ids = new HashMap<>();
        jdbc.query(
                "select id, gutenberg_id from catalog_work where wikidata_id is null and gutenberg_id is not null",
                row -> {
                    ids.put(row.getInt(2), row.getObject(1, UUID.class));
                });
        return ids;
    }

    /** Gutenberg ids move between works as matching improves; clearing them first keeps the unique index happy. */
    public void clearGutenbergIds() {
        jdbc.update("update catalog_work set gutenberg_id = null where gutenberg_id is not null");
    }

    public void upsertWorks(List<WorkRow> rows, Instant now) {
        String sql = """
                insert into catalog_work (id, wikidata_id, gutenberg_id, title, original_title, original_language,
                    publication_year, author_display, authors, popularity, production_status, production_free_from,
                    updated_at, publisher, literary)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?, ?, ?, ?, ?, ?)
                on conflict (id) do update set wikidata_id = excluded.wikidata_id,
                    gutenberg_id = excluded.gutenberg_id, title = excluded.title,
                    original_title = excluded.original_title, original_language = excluded.original_language,
                    publication_year = excluded.publication_year, author_display = excluded.author_display,
                    authors = excluded.authors, popularity = excluded.popularity,
                    production_status = excluded.production_status,
                    production_free_from = excluded.production_free_from, updated_at = excluded.updated_at,
                    publisher = excluded.publisher, literary = excluded.literary
                """;
        Timestamp stamp = Timestamp.from(now);
        for (int from = 0; from < rows.size(); from += BATCH) {
            List<WorkRow> chunk = rows.subList(from, Math.min(rows.size(), from + BATCH));
            jdbc.batchUpdate(sql, chunk, chunk.size(), (statement, row) -> {
                statement.setObject(1, row.id());
                statement.setString(2, row.wikidataId());
                statement.setObject(3, row.gutenbergId(), Types.INTEGER);
                statement.setString(4, row.title());
                statement.setString(5, row.originalTitle());
                statement.setString(6, row.originalLanguage());
                statement.setObject(7, row.publicationYear(), Types.INTEGER);
                statement.setString(8, row.authorDisplay());
                statement.setString(9, objectMapper.writeValueAsString(row.authors()));
                statement.setInt(10, row.popularity());
                statement.setString(11, row.productionStatus().name());
                statement.setObject(12, row.productionFreeFrom(), Types.INTEGER);
                statement.setTimestamp(13, stamp);
                statement.setString(14, row.publisher());
                statement.setBoolean(15, row.literary());
            });
        }
    }

    /** Works the import no longer produced, unless the shelf, a want or an alias still points at them. */
    public int deleteStale(Instant importStarted) {
        return jdbc.update("""
                delete from catalog_work w where w.updated_at < ? and w.work_slug is null
                  and not exists (select 1 from want x where x.catalog_work_id = w.id)
                  and not exists (select 1 from catalog_alias a where a.catalog_work_id = w.id)
                """, Timestamp.from(importStarted));
    }

    /** Where a work's text can be fetched, as the import writes it. */
    public record SourceRow(
            UUID id,
            UUID workId,
            String kind,
            String language,
            String reference,
            String label,
            boolean translation,
            List<CatalogAuthor> translators,
            boolean edition) {}

    public void replaceSources(List<SourceRow> rows) {
        jdbc.update("delete from catalog_text_source");
        String sql = "insert into catalog_text_source"
                + " (id, catalog_work_id, kind, language, reference, label, translation, translators, edition)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?)";
        for (int from = 0; from < rows.size(); from += BATCH) {
            List<SourceRow> chunk = rows.subList(from, Math.min(rows.size(), from + BATCH));
            jdbc.batchUpdate(sql, chunk, chunk.size(), (statement, row) -> {
                statement.setObject(1, row.id());
                statement.setObject(2, row.workId());
                statement.setString(3, row.kind());
                statement.setString(4, row.language());
                statement.setString(5, row.reference());
                statement.setString(6, row.label());
                statement.setBoolean(7, row.translation());
                statement.setString(8, objectMapper.writeValueAsString(row.translators()));
                statement.setBoolean(9, row.edition());
            });
        }
    }

    public List<SourceRow> sources(UUID workId) {
        return jdbc.query(
                "select id, catalog_work_id, kind, language, reference, label, translation, translators::text, edition"
                        + " from catalog_text_source where catalog_work_id = ? order by kind, language, label",
                this::sourceRow,
                workId);
    }

    /** The processor's shape verdict for each download address it has measured, among {@code downloadUrls}. */
    public Map<String, String> shapes(Collection<String> downloadUrls) {
        if (downloadUrls.isEmpty()) {
            return Map.of();
        }
        Map<String, String> shapes = new HashMap<>();
        named.query(
                "select download_url, verdict from catalog_source_shape where download_url in (:urls)",
                new MapSqlParameterSource("urls", downloadUrls),
                row -> {
                    shapes.put(row.getString(1), row.getString(2));
                });
        return shapes;
    }

    /** Keep the processor's verdict on a file's shape; a later measurement replaces an earlier one. */
    public void recordShape(String downloadUrl, String verdict, Instant checkedAt) {
        jdbc.update(
                "insert into catalog_source_shape (download_url, verdict, checked_at) values (?, ?, ?)"
                        + " on conflict (download_url) do update set verdict = excluded.verdict,"
                        + " checked_at = excluded.checked_at",
                downloadUrl,
                verdict,
                Timestamp.from(checkedAt));
    }

    public Optional<SourceRow> source(UUID sourceId) {
        return jdbc
                .query(
                        "select id, catalog_work_id, kind, language, reference, label, translation, translators::text, edition"
                                + " from catalog_text_source where id = ?",
                        this::sourceRow,
                        sourceId)
                .stream()
                .findFirst();
    }

    private SourceRow sourceRow(ResultSet row, int index) throws SQLException {
        return new SourceRow(
                row.getObject(1, UUID.class),
                row.getObject(2, UUID.class),
                row.getString(3),
                row.getString(4),
                row.getString(5),
                row.getString(6),
                row.getBoolean(7),
                readAuthors(row.getString(8)),
                row.getBoolean(9));
    }

    public void replaceTitles(List<TitleRow> rows) {
        jdbc.update("delete from catalog_title");
        String sql =
                "insert into catalog_title (catalog_work_id, text, language, kind, normalized) values (?, ?, ?, ?, ?)";
        for (int from = 0; from < rows.size(); from += BATCH) {
            List<TitleRow> chunk = rows.subList(from, Math.min(rows.size(), from + BATCH));
            jdbc.batchUpdate(sql, chunk, chunk.size(), (statement, row) -> {
                statement.setObject(1, row.workId());
                statement.setString(2, row.text());
                statement.setString(3, row.language());
                statement.setString(4, row.kind());
                statement.setString(5, row.normalized());
            });
        }
    }

    /**
     * Works whose titles or authors contain the folded query as whole words or close to it. An exact title comes
     * first; then matches in quarter-point bands of similarity, the better-known work first within a band, so a famous
     * work is not buried under an obscure one that happens to match a letter better.
     */
    public List<Hit> search(String folded, int limit) {
        String sql = """
                select w.id, w.title, w.original_title, w.original_language, w.publication_year, w.author_display,
                       w.popularity, w.work_slug, w.production_status, w.production_free_from, s.score
                from (select id, max(score) as score
                      from (select t.catalog_work_id as id,
                                   case when t.normalized = :q then 2.0 else word_similarity(:q, t.normalized) end
                                       as score
                            from catalog_title t
                            where :q <% t.normalized
                            union all
                            select a.catalog_work_id, 2.0 from catalog_alias a where a.normalized = :q) m
                      group by id) s
                join catalog_work w on w.id = s.id
                order by s.score >= 2 desc, floor(s.score * 4) desc, w.popularity desc, w.title
                limit :limit
                """;
        return named.query(
                sql, new MapSqlParameterSource().addValue("q", folded).addValue("limit", limit), CatalogStore::hit);
    }

    /** The facts of every work, for recomputing production status as the years turn. */
    public record FactsRow(UUID id, Integer publicationYear, String authorsJson, String status, Integer freeFrom) {}

    public List<FactsRow> allFacts() {
        List<FactsRow> rows = new ArrayList<>();
        jdbc.query(
                "select id, publication_year, authors::text, production_status, production_free_from from catalog_work",
                row -> {
                    rows.add(new FactsRow(
                            row.getObject(1, UUID.class),
                            (Integer) row.getObject(2),
                            row.getString(3),
                            row.getString(4),
                            (Integer) row.getObject(5)));
                });
        return rows;
    }

    public void updateProductionStatus(List<FactsRow> rows) {
        jdbc.batchUpdate(
                "update catalog_work set production_status = ?, production_free_from = ? where id = ?",
                rows,
                BATCH,
                (statement, row) -> {
                    statement.setString(1, row.status());
                    statement.setObject(2, row.freeFrom(), Types.INTEGER);
                    statement.setObject(3, row.id());
                });
    }

    public List<CatalogAuthor> readAuthors(String json) {
        return List.of(objectMapper.readValue(json, CatalogAuthor[].class));
    }

    /** Links a shelf work to the catalogue work it is an edition of. */
    public void linkWorkSlug(UUID workId, String workSlug) {
        jdbc.update("update catalog_work set work_slug = ? where id = ?", workSlug, workId);
    }

    /** Clears a shelf work's link before /ops points it at another catalogue work. */
    public void unlinkWorkSlug(String workSlug) {
        jdbc.update("update catalog_work set work_slug = null where work_slug = ?", workSlug);
    }

    public boolean isLinked(String workSlug) {
        Integer count =
                jdbc.queryForObject("select count(*) from catalog_work where work_slug = ?", Integer.class, workSlug);
        return count != null && count > 0;
    }

    /** Works with exactly this folded title, best known first: the candidates an import links a shelf book to. */
    public List<Hit> exactTitle(String folded) {
        return named.query("""
                select distinct w.id, w.title, w.original_title, w.original_language, w.publication_year,
                       w.author_display, w.popularity, w.work_slug, w.production_status, w.production_free_from,
                       2.0 as score
                from catalog_title t join catalog_work w on w.id = t.catalog_work_id
                where t.normalized = :q and t.kind = 'TITLE'
                order by w.popularity desc
                limit 20
                """, new MapSqlParameterSource("q", folded), CatalogStore::hit);
    }

    /**
     * The best-known literary works that become free where we make books on 1 January of a year (or became free on
     * the last one): Public Domain Day. Only works whose year the facts pin down exactly are listed, and only novels,
     * plays, poems and the like: a list readers see is no place for every pamphlet that turns free.
     */
    public List<Hit> freeFrom(int year, int limit) {
        return named.query(
                """
                select w.id, w.title, w.original_title, w.original_language, w.publication_year, w.author_display,
                       w.popularity, w.work_slug, w.production_status, w.production_free_from, 0.0 as score
                from catalog_work w
                where w.production_free_from = :year and w.literary
                order by w.popularity desc, w.title
                limit :limit
                """, new MapSqlParameterSource().addValue("year", year).addValue("limit", limit), CatalogStore::hit);
    }

    /** One search as the demand page counts it. {@code requester} tells readers apart without storing an address. */
    public void logSearch(String folded, Hit top, UUID memberId, String requester, Instant at) {
        jdbc.update(
                "insert into catalog_search (query, top_work_id, top_state, member_id, requester, created_at)"
                        + " values (?, ?, ?, ?, ?, ?)",
                truncate(folded, 200),
                top == null ? null : top.id(),
                top == null ? null : top.productionStatus().name(),
                memberId,
                requester,
                Timestamp.from(at));
    }

    public record LoggedSearch(String query, UUID topWorkId, String topState, String requester, Instant at) {}

    public List<LoggedSearch> searchesSince(Instant since) {
        return jdbc.query(
                "select query, top_work_id, top_state, requester, created_at from catalog_search"
                        + " where created_at >= ? order by requester, created_at",
                (row, index) -> new LoggedSearch(
                        row.getString(1),
                        row.getObject(2, UUID.class),
                        row.getString(3),
                        row.getString(4),
                        row.getTimestamp(5).toInstant()),
                Timestamp.from(since));
    }

    /** Teaches search that a query means a work; a second alias for the same query replaces the first. */
    public void saveAlias(String folded, UUID workId, UUID createdBy, Instant at) {
        jdbc.update("""
                insert into catalog_alias (id, normalized, catalog_work_id, created_by_user_id, created_at)
                values (?, ?, ?, ?, ?)
                on conflict (normalized) do update set catalog_work_id = excluded.catalog_work_id,
                    created_by_user_id = excluded.created_by_user_id, created_at = excluded.created_at
                """, UUID.randomUUID(), truncate(folded, 200), workId, createdBy, Timestamp.from(at));
    }

    private static String truncate(String text, int length) {
        return text.length() <= length ? text : text.substring(0, length);
    }

    private static Hit hit(ResultSet row, int index) throws SQLException {
        return new Hit(
                row.getObject("id", UUID.class),
                row.getString("title"),
                row.getString("original_title"),
                row.getString("original_language"),
                (Integer) row.getObject("publication_year"),
                row.getString("author_display"),
                row.getInt("popularity"),
                row.getString("work_slug"),
                RightsStatus.valueOf(row.getString("production_status")),
                (Integer) row.getObject("production_free_from"),
                row.getDouble("score"));
    }
}
