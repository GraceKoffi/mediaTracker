package gracekoffi.tracker.media.model;

public class Media {
    private Integer id;
    private String tmdbId;
    private String imdbId;
    private String title;
    private String thumbnailUrl;
    private String type;
    private Float rating;

    public Media() {}

    public Media(Integer id, String tmdbId, String imdbId, String title, String thumbnailUrl, String type, Float rating) {
        this.setId(id);
        this.setTmdbId(tmdbId);
        this.setImdbId(imdbId);
        this.setTitle(title);
        this.setThumbnailUrl(thumbnailUrl);
        this.setType(type);
        this.setRating(rating);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}