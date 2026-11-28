package com.almonium.learning.play.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.play.dto.response.HigherLowerBest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** The higher_lower_best table: one best run per member and language. */
@Repository
@RequiredArgsConstructor
public class HigherLowerBestStore {
    private final NamedParameterJdbcTemplate jdbc;

    public List<HigherLowerBest> bests(UUID userId) {
        return jdbc.query(
                "select language, best from higher_lower_best where user_id = :user order by language",
                new MapSqlParameterSource("user", userId),
                (row, index) -> new HigherLowerBest(Language.valueOf(row.getString("language")), row.getInt("best")));
    }

    /** The current best, locked until the transaction ends so two runs recorded at once cannot both read it. */
    public Optional<Integer> lockBest(UUID userId, Language language) {
        return jdbc.query("""
                        select best from higher_lower_best
                        where user_id = :user and language = :language for update
                        """, params(userId, language), (row, index) -> row.getInt("best")).stream()
                .findFirst();
    }

    /** Keeps the longer of the stored best and this run. */
    public void keepLonger(UUID userId, Language language, int run) {
        jdbc.update("""
                insert into higher_lower_best (user_id, language, best, updated_at)
                values (:user, :language, :run, now())
                on conflict (user_id, language) do update
                set best = greatest(higher_lower_best.best, excluded.best), updated_at = now()
                """, params(userId, language).addValue("run", run));
    }

    private static MapSqlParameterSource params(UUID userId, Language language) {
        return new MapSqlParameterSource("user", userId).addValue("language", language.name());
    }
}
