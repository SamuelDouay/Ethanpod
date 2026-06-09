package fr.github.ethanpod.view.util;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ImageCache {
    private static final Logger LOGGER = LogManager.getLogger(ImageCache.class);
    private static final Map<String, WeakReference<Image>> IMAGE_CACHE = new ConcurrentHashMap<>();
    private static final ReferenceQueue<Image> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final Map<WeakReference<Image>, String> REVERSE_MAP = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService CLEANUP_EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "ImageCache-Cleanup");
        t.setDaemon(true);
        return t;
    });

    static {
        // Démarrage automatique du nettoyage périodique
        startAutomaticCleanup();
    }

    private ImageCache() {
        // no parameters
    }

    /**
     * Récupère une image du cache ou la charge si nécessaire
     */
    public static Image getImage(String url) {
        // Vérifier si l'image est déjà en cache et encore référencée
        WeakReference<Image> weakRef = IMAGE_CACHE.get(url);
        if (weakRef != null) {
            Image cachedImage = weakRef.get();
            if (cachedImage != null) {
                return cachedImage;
            } else {
                // La référence est morte, la supprimer
                IMAGE_CACHE.remove(url);
                REVERSE_MAP.remove(weakRef);
            }
        }

        // Charger la nouvelle image
        Image newImage = new Image(url, true);
        WeakReference<Image> newWeakRef = new WeakReference<>(newImage, REFERENCE_QUEUE);
        IMAGE_CACHE.put(url, newWeakRef);
        REVERSE_MAP.put(newWeakRef, url);
        return newImage;
    }

    /**
     * Nettoie automatiquement les références mortes
     */
    public static void cleanupDeadReferences() {
        WeakReference<? extends Image> deadRef;
        while ((deadRef = (WeakReference<? extends Image>) REFERENCE_QUEUE.poll()) != null) {
            String url = REVERSE_MAP.remove(deadRef);
            if (url != null) {
                IMAGE_CACHE.remove(url);
            }
        }
    }

    /**
     * Démarre le nettoyage automatique périodique
     */
    private static void startAutomaticCleanup() {
        CLEANUP_EXECUTOR.scheduleWithFixedDelay(() -> {
            try {
                int sizeBefore = IMAGE_CACHE.size();
                cleanupDeadReferences();
                int sizeAfter = IMAGE_CACHE.size();

                if (sizeBefore != sizeAfter) {
                    LOGGER.debug("Nettoyage automatique: {} images supprimées", sizeBefore - sizeAfter);
                }
            } catch (Exception e) {
                LOGGER.warn("Erreur lors du nettoyage automatique: {}", e.getMessage(), e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Arrête le nettoyage automatique (à appeler à la fermeture de l'application)
     */
    public static void shutdown() {
        CLEANUP_EXECUTOR.shutdown();
        try {
            if (!CLEANUP_EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                CLEANUP_EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException _) {
            CLEANUP_EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}