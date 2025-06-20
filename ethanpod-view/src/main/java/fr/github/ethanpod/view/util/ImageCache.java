package fr.github.ethanpod.view.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private static final int MAX_CACHE_SIZE = 1000; // Ajuste selon tes besoins

    private ImageCache() {
        // no parameters
    }

    public static Image getImage(String url) {
        if (IMAGE_CACHE.containsKey(url)) {
            return IMAGE_CACHE.get(url);
        }

        // Mécanisme de contrôle de taille du cache
        if (IMAGE_CACHE.size() >= MAX_CACHE_SIZE) {
            // Stratégie simple: supprimer une entrée aléatoire
            String keyToRemove = IMAGE_CACHE.keySet().iterator().next();
            IMAGE_CACHE.remove(keyToRemove);
        }

        Image image = new Image(url, true); // true pour chargement en background
        IMAGE_CACHE.put(url, image);
        return image;
    }

    public static void clearCache() {
        IMAGE_CACHE.clear();
    }
}
