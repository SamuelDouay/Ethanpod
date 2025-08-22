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

    private static final int MAX_CACHE_SIZE = 1000;
    private static final ScheduledExecutorService CLEANUP_EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "ImageCache-Cleanup");
        t.setDaemon(true); // Thread daemon pour ne pas empêcher l'arrêt de l'application
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
        // Nettoyage des références mortes avant de procéder
        cleanupDeadReferences();

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

        // Vérifier la taille du cache avant d'ajouter
        if (IMAGE_CACHE.size() >= MAX_CACHE_SIZE) {
            performCacheEviction();
        }

        // Charger la nouvelle image
        Image newImage = new Image(url, true);
        WeakReference<Image> newWeakRef = new WeakReference<>(newImage, REFERENCE_QUEUE);
        IMAGE_CACHE.put(url, newWeakRef);
        REVERSE_MAP.put(newWeakRef, url);

        LOGGER.debug("Image ajoutée au cache: {} (taille cache: {})", url, IMAGE_CACHE.size());
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
                LOGGER.debug("Image automatiquement supprimée du cache: {}", url);
            }
        }
    }

    /**
     * Éviction du cache quand il est plein
     */
    private static void performCacheEviction() {
        // Première tentative : nettoyer les références mortes
        cleanupDeadReferences();

        // Si le cache est encore plein, supprimer quelques entrées
        if (IMAGE_CACHE.size() >= MAX_CACHE_SIZE) {
            int toRemove = IMAGE_CACHE.size() - MAX_CACHE_SIZE + 10; // Supprimer 10 de plus pour éviter les évictions fréquentes

            IMAGE_CACHE.entrySet().iterator().forEachRemaining(entry -> {
                if (toRemove <= 0) return;

                WeakReference<Image> weakRef = entry.getValue();
                Image image = weakRef.get();
                if (image == null) {
                    // Référence déjà morte
                    IMAGE_CACHE.remove(entry.getKey());
                    REVERSE_MAP.remove(weakRef);
                } else {
                    // Forcer la suppression
                    IMAGE_CACHE.remove(entry.getKey());
                    REVERSE_MAP.remove(weakRef);
                    LOGGER.debug("Image évincée du cache: {}", entry.getKey());
                }
            });
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
        }, 5, 5, TimeUnit.SECONDS); // Nettoyage toutes les 30 secondes
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

    /**
     * Vide complètement le cache
     */
    public static void clearCache() {
        IMAGE_CACHE.clear();
        REVERSE_MAP.clear();
        // Vider aussi la queue des références
        while (REFERENCE_QUEUE.poll() != null) {
            // Continue jusqu'à ce que la queue soit vide
        }
        LOGGER.info("Cache vidé complètement");
    }

    /**
     * Retourne des statistiques du cache
     */
    public static CacheStats getCacheStats() {
        cleanupDeadReferences(); // S'assurer que les stats sont à jour

        int totalEntries = IMAGE_CACHE.size();
        int aliveReferences = 0;

        for (WeakReference<Image> ref : IMAGE_CACHE.values()) {
            if (ref.get() != null) {
                aliveReferences++;
            }
        }

        return new CacheStats(totalEntries, aliveReferences, MAX_CACHE_SIZE);
    }

    /**
     * Classe pour les statistiques du cache
     */
    public static class CacheStats {
        public final int totalEntries;
        public final int aliveReferences;
        public final int maxSize;
        public final double usageRatio;

        public CacheStats(int totalEntries, int aliveReferences, int maxSize) {
            this.totalEntries = totalEntries;
            this.aliveReferences = aliveReferences;
            this.maxSize = maxSize;
            this.usageRatio = maxSize > 0 ? (double) totalEntries / maxSize : 0;
        }

        @Override
        public String toString() {
            return String.format("Cache: %d/%d entrées (%.1f%%), %d références vivantes",
                    totalEntries, maxSize, usageRatio * 100, aliveReferences);
        }
    }
}