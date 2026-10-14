package dev.jayo.Misc;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public ArrayList<String> getNaechstePause() {
        LocalDate today = LocalDate.now();

        DayOfWeek tag = today.getDayOfWeek();

        Map<String, ArrayList<String>> toModify = getDayFromString(tag.name());

        return null;
    }

    public Map<String, ArrayList<String>> getDayFromString(String tag) {
        switch (tag.toLowerCase()) {
            case "montag", "monday" -> {return Montag;}
            case "dienstag", "tuesday" -> {return Dienstag;}
            case "mittwoch", "wednesday" -> {return Mittwoch;}
            case "donnerstag", "thursday" -> {return Donnerstag;}
            case "freitag", "friday" -> {return Freitag;}
        }
        return null;
    }
}
