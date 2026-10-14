package dev.jayo.Misc;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DateiLocations {
    public static String IT_ORDNER = "S:/";
    public static String JAYO_ORDNER = "jayoApp";
    public static String PAUSEN_ZEITEN = "pausenZeiten.json";

    public static File getPausenJson() {
        return new File(IT_ORDNER + File.separator + JAYO_ORDNER + File.separator + PAUSEN_ZEITEN);
    }

    public static void ensureFolders(JFrame fenster) {
        File itOrdner = new File(IT_ORDNER);

        if (!itOrdner.exists()) {
            JOptionPane.showMessageDialog(fenster, "BV_IT (S:\\) Festplatte fehlt! Hat dein Laptop zugang darauf?");
        } else {
            File jayoOrdner = new File(itOrdner, JAYO_ORDNER);

            if (!jayoOrdner.exists()) {
                boolean madeDir = jayoOrdner.mkdir();
                if (!madeDir) {
                    JOptionPane.showMessageDialog(fenster, "Konnte nicht order für pausenZeiten.json erstellen.");
                } else {
                    File pausenZeiten = new File(jayoOrdner, PAUSEN_ZEITEN);
                    if (!pausenZeiten.exists()) {
                        try {
                            pausenZeiten.createNewFile();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(fenster, "Konnte nicht pausenZeiten.json erstellen.");
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
