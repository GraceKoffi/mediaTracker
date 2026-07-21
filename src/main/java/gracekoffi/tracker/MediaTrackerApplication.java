package gracekoffi.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // <-- C'est cette annotation qui définit le point d'entrée !
public class MediaTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaTrackerApplication.class, args);
    }
}