package dev.jayo.Misc;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.*;

public class DateiLocations {
    public static String IT_ORDNER = "S:/";
    public static String JAYO_ORDNER = "jayoApp";
    public static String PAUSEN_ZEITEN = "pausenZeiten.json";
    public static String USER_SETTINGS = "BenutzerEinstellungen.json";

    public static UserSettings userSettings = new UserSettings();

    public static File getPausenJson() {
        return new File(IT_ORDNER + File.separator + JAYO_ORDNER + File.separator + PAUSEN_ZEITEN);
    }

    public static void saveUserSettings() {
        Gson gson = new Gson();
        var file = new File(getAppData(), DateiLocations.USER_SETTINGS);

        try (FileWriter in = new FileWriter(file)) {
            gson.toJson(DateiLocations.userSettings, in);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Konnte nicht BenutzerEinstellungen.json speichern.");
        }
    }

    public static File getAppData() {
        String appDataPath = System.getenv("APPDATA");
        System.out.println(appDataPath);

        if (appDataPath != null) {
            File appFolder = new File(appDataPath, "jayoApp");

            if (!appFolder.exists()) {
                boolean created = appFolder.mkdir();

                if (!created) {
                    System.out.println("AppData Folder nicht erstellt.");
                }
            }

            return appFolder;
        } else {
            System.out.println("Kein Windows OS oder OS mit AppData.");
            return null;
        }
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

    public static void prepUserSettings() {
        Gson gson = new Gson();
        File settings = new File(getAppData(), USER_SETTINGS);

        System.out.println(settings.getAbsolutePath());

        System.out.println();

        userSettings = new UserSettings();

        if (!settings.exists()) {
            try {
                settings.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Konnte nicht BenutzerEinstellungen.json erstellen.");
            }
        } else {
            try (FileReader in = new FileReader(settings)) {
                userSettings = gson.fromJson(in, UserSettings.class);
            } catch (IOException e) {
                e.printStackTrace();
                userSettings = new UserSettings();
            }
        }
    }
}
