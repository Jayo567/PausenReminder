package dev.jayo.Misc;

import dev.jayo.Misc.ReturnClasses.JetzigePause;
import dev.jayo.Misc.ReturnClasses.NaechstePauseReturn;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
        Map<String, ArrayList<String>> toModify = getDayFromString(tag);

        if (toModify != null && toModify.get(pausenName) != null) {
            toModify.replace(pausenName, Uhrzeit);
        } else if (toModify != null) {
            toModify.put(pausenName, Uhrzeit);
        }
    }

    public void remove(String tag, String pausenName) {
        Map<String, ArrayList<String>> toModify = getDayFromString(tag);

        if (toModify != null && toModify.get(pausenName) != null) {
            toModify.remove(pausenName);
        }
    }

    public JetzigePause getJetzigePause() {
        LocalDate today = LocalDate.now();

        DayOfWeek tag = today.getDayOfWeek();

        Map<String, ArrayList<String>> toSearch = getDayFromString(tag.name());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime now = LocalDateTime.now();

        Instant jetztInstant = now.toInstant(ZoneOffset.UTC);

        for (String key : toSearch.keySet()) {
            ArrayList<String> Uhrzeit = toSearch.get(key);

            String anfang = Uhrzeit.getFirst();
            String ende;

            if (Uhrzeit.size() >= 2) {
                ende = Uhrzeit.getLast();

                LocalTime anfangLT = LocalTime.parse(anfang, formatter);
                LocalTime endeLT = LocalTime.parse(ende, formatter);

                Instant dannInstant = anfangLT.atDate(today).toInstant(ZoneOffset.UTC);
                Instant endeInstant = endeLT.atDate(today).toInstant(ZoneOffset.UTC);

                Duration diffAnfang = Duration.between(jetztInstant, dannInstant);
                Duration diffEnde = Duration.between(jetztInstant, endeInstant);



                if (diffAnfang.isNegative() && diffEnde.isPositive()) {
                    return new JetzigePause(
                            key,
                            Uhrzeit,
                            endeInstant
                    );
                }
            }
        }

        return null;
    }

    public NaechstePauseReturn getNaechstePause() {
        LocalDate today = LocalDate.now();

        DayOfWeek tag = today.getDayOfWeek();

        Map<String, ArrayList<String>> toSearch = getDayFromString(tag.name());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime now = LocalDateTime.now();

        Instant jetztInstant = now.toInstant(ZoneOffset.UTC);

        long shortestTime = Long.MAX_VALUE;
        String selectedKey = "";
        Instant selectedInstant = null;

        for (String key : toSearch.keySet()) {
            ArrayList<String> Uhrzeit = toSearch.get(key);

            String anfang = Uhrzeit.getFirst();

            LocalTime anfangLT = LocalTime.parse(anfang, formatter);

            Instant dannInstant = anfangLT.atDate(today).toInstant(ZoneOffset.UTC);

            Duration res = Duration.between(jetztInstant, dannInstant);

            if (shortestTime > res.toSeconds() && res.toSeconds() >= 0) {
                shortestTime = res.toSeconds();
                selectedKey = key;
                selectedInstant = dannInstant;
            }
        }

        if (!selectedKey.isEmpty()) {
            return new NaechstePauseReturn(
                    selectedKey,
                    toSearch.get(selectedKey),
                    selectedInstant
            );
        }

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
