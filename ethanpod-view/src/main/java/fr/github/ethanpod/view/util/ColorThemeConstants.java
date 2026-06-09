package fr.github.ethanpod.view.util;

import fr.github.ethanpod.util.setting.ConfigProperties;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColorThemeConstants {
    private static final ConfigProperties configProperties = ConfigProperties.getInstance();
    private static final List<ThemeChangeListener> listeners = new ArrayList<>();
    private static final Map<String, Color> COLOR_CACHE = new ConcurrentHashMap<>(32);
    private static ThemeType currentTheme = ThemeType.LIGHT;

    private ColorThemeConstants() {
        // Constructeur privé pour empêcher l'instanciation
    }

    public static void setTheme(ThemeType theme) {
        if (theme == currentTheme) return;
        currentTheme = theme;
        COLOR_CACHE.clear();
        notifyListeners();
    }

    public static void addThemeChangeListener(ThemeChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    private static void notifyListeners() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged(currentTheme);
        }
    }

    public static ThemeType getCurrentTheme() {
        return currentTheme;
    }

    public static Color getColor(String key) {
        return COLOR_CACHE.computeIfAbsent(key, k -> {
            String value = configProperties.getProperty(k);
            if (value != null && !value.isEmpty()) {
                return Color.web(value);
            }
            return null;
        });

    }

    private static Color themed(String family, String shade) {
        String prefix = currentTheme == ThemeType.DARK ? "dark" : "light";
        return getColor(prefix + "." + family + "." + shade);
    }

    // --- Couleurs principales ---
    public static Color getMain000() {
        return themed("main", "000");
    }

    public static Color getMain050() {
        return themed("main", "050");
    }

    public static Color getMain100() {
        return themed("main", "100");
    }

    public static Color getMain200() {
        return themed("main", "200");
    }

    public static Color getMain300() {
        return themed("main", "300");
    }

    public static Color getMain400() {
        return themed("main", "400");
    }

    public static Color getMain500() {
        return themed("main", "500");
    }

    public static Color getMain600() {
        return themed("main", "600");
    }

    public static Color getMain700() {
        return themed("main", "700");
    }

    public static Color getMain800() {
        return themed("main", "800");
    }

    public static Color getMain900() {
        return themed("main", "900");
    }

    public static Color getMain950() {
        return themed("main", "950");
    }


    // --- Couleurs grises ---
    public static Color getGrey000() {
        return themed("grey", "000");
    }

    public static Color getGrey050() {
        return themed("grey", "050");
    }

    public static Color getGrey100() {
        return themed("grey", "100");
    }

    public static Color getGrey200() {
        return themed("grey", "200");
    }

    public static Color getGrey300() {
        return themed("grey", "300");
    }

    public static Color getGrey400() {
        return themed("grey", "400");
    }

    public static Color getGrey500() {
        return themed("grey", "500");
    }

    public static Color getGrey600() {
        return themed("grey", "600");
    }

    public static Color getGrey700() {
        return themed("grey", "700");
    }

    public static Color getGrey800() {
        return themed("grey", "800");
    }

    public static Color getGrey900() {
        return themed("grey", "900");
    }

    public static Color getGrey950() {
        return themed("grey", "950");
    }
    
    public interface ThemeChangeListener {
        void onThemeChanged(ThemeType newTheme);
    }
}