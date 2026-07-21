package gracekoffi.tracker.media.service;

import com.fasterxml.jackson.databind.JsonNode;
import gracekoffi.tracker.media.model.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TMDBService {

    private final RestClient restClient;

    public TMDBService(
            @Value("${tmdb.api.token}") String apiToken,
            @Value("${tmdb.api.baseUrl}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Recherche de films, séries et personnes sur TMDB
     * Endpoint TMDB: GET /search/multi?query=...&language=fr-FR
     */
    public String searchMulti(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/multi")
                        .queryParam("query", query)
                        .queryParam("language", "fr-FR")
                        .build())
                .retrieve()
                .body(String.class);
    }

    /**
     * Récupérer les détails d'un film
     * Endpoint TMDB: GET /movie/{id}?language=fr-FR
     */
    public String getMovieDetails(int movieId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{id}")
                        .queryParam("language", "fr-FR")
                        .build(movieId))
                .retrieve()
                .body(String.class);
    }

    /**
     * Récupérer les détails d'une série TV
     * Endpoint TMDB: GET /tv/{id}?language=fr-FR
     */
    public String getTvDetails(int tvId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/{id}")
                        .queryParam("language", "fr-FR")
                        .build(tvId))
                .retrieve()
                .body(String.class);
    }

    /**
     * Interroge TMDB et mappe la réponse vers notre objet Java Media
     * prêt à être inséré en BDD par le MediaRepository.
     */
    public Media fetchMediaFromTMDB(String tmdbId, String type) {
        String path = type.equalsIgnoreCase("MOVIE") ? "/movie/{id}" : "/tv/{id}";

        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("language", "fr-FR")
                        .build(tmdbId))
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new RuntimeException("Média non trouvé sur TMDB pour l'ID : " + tmdbId);
        }

        // Les films exposent "title", les séries "name"
        String title = response.has("title") && !response.get("title").isNull()
                ? response.get("title").asText()
                : response.get("name").asText();

        String posterUrl = response.has("poster_path") && !response.get("poster_path").isNull()
                ? "https://image.tmdb.org/t/p/w500" + response.get("poster_path").asText()
                : null;

        // Gestion de l'IMDB ID
        String imdbId = response.has("imdb_id") && !response.get("imdb_id").isNull()
                ? response.get("imdb_id").asText()
                : null;

        // Note moyenne sur TMDB
        float rating = response.has("vote_average") ? (float) response.get("vote_average").asDouble() : 0.0f;

        return new Media(null, tmdbId, imdbId, title, posterUrl, type.toUpperCase(), rating);
    }
}