package gracekoffi.tracker.media.repository;

import gracekoffi.tracker.media.model.Media;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediaRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MediaRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertMedia(Media media) {
        String sql = """
            INSERT INTO media (tmdb_id, imdb_id, title, thumbnail_url, type, rating)
            VALUES (:tmdbId, :imdbId, :title, :thumbnailUrl, :type, :rating)
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("tmdbId", media.getTmdbId())
                .addValue("imdbId", media.getImdbId())
                .addValue("title", media.getTitle())
                .addValue("thumbnailUrl", media.getThumbnailUrl())
                .addValue("type", media.getType())
                .addValue("rating", media.getRating());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        // Récupération de l'id auto-incrémenté généré par MySQL
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    /**
     * Requête préparée : Chercher un média par son TMDB ID (pour éviter les doublons)
     */
    public Optional<Media> findByTmdbId(String tmdbId) {
        String sql = "SELECT * FROM media WHERE tmdb_id = :tmdbId";
        MapSqlParameterSource params = new MapSqlParameterSource("tmdbId", tmdbId);

        List<Media> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> new Media(
                rs.getInt("id"),
                rs.getString("tmdb_id"),
                rs.getString("imdb_id"),
                rs.getString("title"),
                rs.getString("thumbnail_url"),
                rs.getString("type"),
                rs.getFloat("rating")
        ));

        return results.stream().findFirst();
    }
}