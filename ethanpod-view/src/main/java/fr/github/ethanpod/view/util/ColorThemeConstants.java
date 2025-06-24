package fr.github.ethanpod.view.util;

import fr.github.ethanpod.logic.setting.ConfigProperties;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorThemeConstants {
    private static final ConfigProperties configProperties = ConfigProperties.getInstance();
    // Liste des écouteurs pour le changement de thème
    private static final List<ThemeChangeListener> listeners = new ArrayList<>();
    // Thème par défaut
    private static ThemeType currentTheme = ThemeType.LIGHT;

    private ColorThemeConstants() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Définit le thème actuel et notifie les écouteurs si le thème a changé
     */
    public static void setTheme(ThemeType theme) {
        ThemeType oldTheme = currentTheme;
        currentTheme = theme;

        // Seulement notifier si le thème a changé
        if (oldTheme != theme) {
            notifyListeners();
        }
    }

    /**
     * Ajoute un écouteur de changement de thème
     */
    public static void addThemeChangeListener(ThemeChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Supprime un écouteur de changement de thème
     */
    public static void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie tous les écouteurs qu'un changement de thème s'est produit
     */
    private static void notifyListeners() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged(currentTheme);
        }
    }

    /**
     * Récupère le thème actuel
     */
    public static ThemeType getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Récupère une couleur à partir du fichier de configuration
     */
    public static Color getColor(String key) {
        String colorValue = configProperties.getProperty(key);
        if (colorValue != null && !colorValue.isEmpty()) {
            return Color.web(colorValue);
        }
        return null;
    }

    // --- Couleurs principales ---
    public static Color getMain000() {
        return currentTheme == ThemeType.DARK ? getDarkMain000() : getLightMain000();
    }

    public static Color getMain050() {
        return currentTheme == ThemeType.DARK ? getDarkMain050() : getLightMain050();
    }

    public static Color getMain100() {
        return currentTheme == ThemeType.DARK ? getDarkMain100() : getLightMain100();
    }

    public static Color getMain200() {
        return currentTheme == ThemeType.DARK ? getDarkMain200() : getLightMain200();
    }

    public static Color getMain300() {
        return currentTheme == ThemeType.DARK ? getDarkMain300() : getLightMain300();
    }

    public static Color getMain400() {
        return currentTheme == ThemeType.DARK ? getDarkMain400() : getLightMain400();
    }

    public static Color getMain500() {
        return currentTheme == ThemeType.DARK ? getDarkMain500() : getLightMain500();
    }

    public static Color getMain600() {
        return currentTheme == ThemeType.DARK ? getDarkMain600() : getLightMain600();
    }

    public static Color getMain700() {
        return currentTheme == ThemeType.DARK ? getDarkMain700() : getLightMain700();
    }

    public static Color getMain800() {
        return currentTheme == ThemeType.DARK ? getDarkMain800() : getLightMain800();
    }

    public static Color getMain900() {
        return currentTheme == ThemeType.DARK ? getDarkMain900() : getLightMain900();
    }

    public static Color getMain950() {
        return currentTheme == ThemeType.DARK ? getDarkMain950() : getLightMain950();
    }

    // --- Couleurs grises ---
    public static Color getGrey000() {
        return currentTheme == ThemeType.DARK ? getDarkGrey000() : getLightGrey000();
    }

    public static Color getGrey050() {
        return currentTheme == ThemeType.DARK ? getDarkGrey050() : getLightGrey050();
    }

    public static Color getGrey100() {
        return currentTheme == ThemeType.DARK ? getDarkGrey100() : getLightGrey100();
    }

    public static Color getGrey200() {
        return currentTheme == ThemeType.DARK ? getDarkGrey200() : getLightGrey200();
    }

    public static Color getGrey300() {
        return currentTheme == ThemeType.DARK ? getDarkGrey300() : getLightGrey300();
    }

    public static Color getGrey400() {
        return currentTheme == ThemeType.DARK ? getDarkGrey400() : getLightGrey400();
    }

    public static Color getGrey500() {
        return currentTheme == ThemeType.DARK ? getDarkGrey500() : getLightGrey500();
    }

    public static Color getGrey600() {
        return currentTheme == ThemeType.DARK ? getDarkGrey600() : getLightGrey600();
    }

    public static Color getGrey700() {
        return currentTheme == ThemeType.DARK ? getDarkGrey700() : getLightGrey700();
    }

    public static Color getGrey800() {
        return currentTheme == ThemeType.DARK ? getDarkGrey800() : getLightGrey800();
    }

    public static Color getGrey900() {
        return currentTheme == ThemeType.DARK ? getDarkGrey900() : getLightGrey900();
    }

    public static Color getGrey950() {
        return currentTheme == ThemeType.DARK ? getDarkGrey950() : getLightGrey950();
    }

    // --- Getters pour les couleurs du thème clair ---
    private static Color getLightMain000() {
        return getColor("light.main.000");
    }

    private static Color getLightMain050() {
        return getColor("light.main.050");
    }

    private static Color getLightMain100() {
        return getColor("light.main.100");
    }

    private static Color getLightMain200() {
        return getColor("light.main.200");
    }

    private static Color getLightMain300() {
        return getColor("light.main.300");
    }

    private static Color getLightMain400() {
        return getColor("light.main.400");
    }

    private static Color getLightMain500() {
        return getColor("light.main.500");
    }

    private static Color getLightMain600() {
        return getColor("light.main.600");
    }

    private static Color getLightMain700() {
        return getColor("light.main.700");
    }

    private static Color getLightMain800() {
        return getColor("light.main.800");
    }

    private static Color getLightMain900() {
        return getColor("light.main.900");
    }

    private static Color getLightMain950() {
        return getColor("light.main.950");
    }

    private static Color getLightGrey000() {
        return getColor("light.grey.000");
    }

    private static Color getLightGrey050() {
        return getColor("light.grey.050");
    }

    private static Color getLightGrey100() {
        return getColor("light.grey.100");
    }

    private static Color getLightGrey200() {
        return getColor("light.grey.200");
    }

    private static Color getLightGrey300() {
        return getColor("light.grey.300");
    }

    private static Color getLightGrey400() {
        return getColor("light.grey.400");
    }

    private static Color getLightGrey500() {
        return getColor("light.grey.500");
    }

    private static Color getLightGrey600() {
        return getColor("light.grey.600");
    }

    private static Color getLightGrey700() {
        return getColor("light.grey.700");
    }

    private static Color getLightGrey800() {
        return getColor("light.grey.800");
    }

    private static Color getLightGrey900() {
        return getColor("light.grey.900");
    }

    private static Color getLightGrey950() {
        return getColor("light.grey.950");
    }

    // --- Getters pour les couleurs du thème sombre ---
    private static Color getDarkMain000() {
        return getColor("dark.main.000");
    }

    private static Color getDarkMain050() {
        return getColor("dark.main.050");
    }

    private static Color getDarkMain100() {
        return getColor("dark.main.100");
    }

    private static Color getDarkMain200() {
        return getColor("dark.main.200");
    }

    private static Color getDarkMain300() {
        return getColor("dark.main.300");
    }

    private static Color getDarkMain400() {
        return getColor("dark.main.400");
    }

    private static Color getDarkMain500() {
        return getColor("dark.main.500");
    }

    private static Color getDarkMain600() {
        return getColor("dark.main.600");
    }

    private static Color getDarkMain700() {
        return getColor("dark.main.700");
    }

    private static Color getDarkMain800() {
        return getColor("dark.main.800");
    }

    private static Color getDarkMain900() {
        return getColor("dark.main.900");
    }

    private static Color getDarkMain950() {
        return getColor("dark.main.950");
    }

    private static Color getDarkGrey000() {
        return getColor("dark.grey.000");
    }

    private static Color getDarkGrey050() {
        return getColor("dark.grey.050");
    }

    private static Color getDarkGrey100() {
        return getColor("dark.grey.100");
    }

    private static Color getDarkGrey200() {
        return getColor("dark.grey.200");
    }

    private static Color getDarkGrey300() {
        return getColor("dark.grey.300");
    }

    private static Color getDarkGrey400() {
        return getColor("dark.grey.400");
    }

    private static Color getDarkGrey500() {
        return getColor("dark.grey.500");
    }

    private static Color getDarkGrey600() {
        return getColor("dark.grey.600");
    }

    private static Color getDarkGrey700() {
        return getColor("dark.grey.700");
    }

    private static Color getDarkGrey800() {
        return getColor("dark.grey.800");
    }

    private static Color getDarkGrey900() {
        return getColor("dark.grey.900");
    }

    private static Color getDarkGrey950() {
        return getColor("dark.grey.950");
    }

    /**
     * Interface pour les écouteurs de changement de thème
     */
    public interface ThemeChangeListener {
        void onThemeChanged(ThemeType newTheme);
    }
}