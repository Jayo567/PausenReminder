package dev.jayo.Misc;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PausenPlan {
    public Map<String, ArrayList<String>> Montag = new HashMap<>();
    public Map<String, ArrayList<String>> Dienstag = new HashMap<>();
    public Map<String, ArrayList<String>> Mittwoch = new HashMap<>();
    public Map<String, ArrayList<String>> Donnerstag = new HashMap<>();
    public Map<String, ArrayList<String>> Freitag = new HashMap<>();

    public void runTagRunnable(TagRunnable runnable) {
        runnable.Run(Montag, "Montag");
        runnable.Run(Dienstag, "Dienstag");
        runnable.Run(Mittwoch, "Mittwoch");
        runnable.Run(Donnerstag, "Donnerstag");
        runnable.Run(Freitag, "Freitag");
    }

    public void update(String tag, String pausenName, ArrayList<String> Uhrzeit) {
        Map<String, ArrayList<String>> toModify = null;
        switch (tag) {
            case "Montag" -> toModify = Montag;
            case "Dienstag" -> toModify = Dienstag;
            case "Mittwoch" -> toModify = Mittwoch;
            case "Donnerstag" -> toModify = Donnerstag;
            case "Freitag" -> toModify = Freitag;
        }

        if (toModify != null && toModify.get(pausenName) != null) {
            toModify.replace(pausenName, Uhrzeit);
        } else if (toModify != null) {
            toModify.put(pausenName, Uhrzeit);
        }
    }

    public Map<String, ArrayList<String>> getDayFromString(String tag) {
        switch (tag) {
            case "Montag", "Monday" -> {return Montag;}
            case "Dienstag", "Tuesday" -> {return Dienstag;}
            case "Mittwoch", "Wednesday" -> {return Mittwoch;}
            case "Donnerstag", "Thursday" -> {return Donnerstag;}
            case "Freitag", "Friday" -> {return Freitag;}
        }
        return null;
    }
}
