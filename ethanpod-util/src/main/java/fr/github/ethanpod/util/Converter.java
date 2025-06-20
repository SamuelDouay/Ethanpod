package fr.github.ethanpod.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    static long kilo = 1024;
    static long mega = kilo * kilo;
    static long giga = mega * kilo;
    static long tera = giga * kilo;

    private Converter() {
        // no parameters
    }

    public static String getSize(long size) {
        double kb = (double) size / kilo;
        double mb = kb / kilo;
        double gb = mb / kilo;
        double tb = gb / kilo;
        if (size < kilo) {
            return size + " B";
        } else if (size < mega) {
            return String.format("%.0f", kb) + " KB";
        } else if (size < giga) {
            return String.format("%.0f", mb) + " MB";
        } else if (size < tera) {
            return String.format("%.0f", gb) + " GB";
        } else {
            return String.format("%.0f", tb) + " TB";
        }
    }

    public static String timestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
}
