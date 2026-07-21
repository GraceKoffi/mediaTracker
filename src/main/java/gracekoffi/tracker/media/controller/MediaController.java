package gracekoffi.tracker.media.controller;

import gracekoffi.tracker.media.model.Media;
import gracekoffi.tracker.media.repository.MediaRepository;
import gracekoffi.tracker.media.service.TMDBService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final TMDBService tmdbService;
    private final MediaRepository mediaRepository;

    public MediaController(TMDBService tmdbService, MediaRepository mediaRepository) {
        this.tmdbService = tmdbService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String query) {
        return tmdbService.searchMulti(query);
    }

    @PostMapping("/add")
    public Media addMedia(@RequestParam("tmdbId") String tmdbId, 
                          @RequestParam("type") String type) {
        
        // 1. Est-ce qu'on l'a déjà en BDD MySQL ?
        Optional<Media> existingMedia = mediaRepository.findByTmdbId(tmdbId);
        if (existingMedia.isPresent()) {
            return existingMedia.get(); // On renvoie directement le média sans re-solliciter TMDB
        }

        // 2. Si non, on va chercher les infos fraîches chez TMDB
        Media newMedia = tmdbService.fetchMediaFromTMDB(tmdbId, type);

        // 3. On l'insère en base grâce à nos requêtes préparées
        int generatedId = mediaRepository.insertMedia(newMedia);
        newMedia.setId(generatedId);

        // 4. On renvoie le média enregistré !
        return newMedia;
    }
}