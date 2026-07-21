CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    hash_pass VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tmdb_id VARCHAR(50) NOT NULL UNIQUE,
    imdb_id VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(500),
    type VARCHAR(20) NOT NULL, -- 'MOVIE', 'TV', 'ANIME'
    rating FLOAT
);

CREATE TABLE IF NOT EXISTS viewing (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    media_id INT NOT NULL,
    status INT NOT NULL, -- 0: To see, 1: In progress, 2: Seen, 3: Stopped
    rating FLOAT,
    watchlisted_at TIMESTAMP,
    watched_at TIMESTAMP,
    rated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS episode (
    id INT AUTO_INCREMENT PRIMARY KEY,
    media_id INT NOT NULL,
    season_number INT NOT NULL,
    episode_number INT NOT NULL,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_episode (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    episode_id INT NOT NULL,
    is_seen BOOLEAN DEFAULT FALSE,
    watched_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (episode_id) REFERENCES episode(id) ON DELETE CASCADE
);