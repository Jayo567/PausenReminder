package dev.jayo;

import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.ReturnClasses.JetzigePause;
import dev.jayo.Misc.ReturnClasses.NaechstePauseReturn;
import dev.jayo.UI.HauptUI;

import java.text.DecimalFormat;
import java.time.*;

// Heads up, this code base is a mix of German and English. "Denglisch" as we Germans might call it.

public class Main extends Thread {
    public HauptUI mainFenster;

    public static void main(String[] args) {
        //DateiLocations.getAppData();
        DateiLocations.prepUserSettings();

        System.out.println(DateiLocations.userSettings);

        Main thread = new Main();
        thread.mainFenster = new HauptUI();
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (mainFenster == null || mainFenster.plan == null) {
                    System.out.println("Etwas existiert nicht.");
                    Thread.sleep(1000);
                    continue;
                }

                LocalDateTime now = LocalDateTime.now();
                DecimalFormat decimalFormat = new DecimalFormat("00");

                JetzigePause jetzigePause = mainFenster.plan.getJetzigePause();
                NaechstePauseReturn stuff = mainFenster.plan.getNaechstePause();

                if (jetzigePause != null) {

                    Instant jetztInstant = now.toInstant(ZoneOffset.UTC);
                    Instant dannInstant = jetzigePause.instant();

                    Duration res = Duration.between(jetztInstant, dannInstant);

                    long seconds = res.getSeconds();
                    long minutes = res.abs().toMinutes();
                    long hours = res.abs().toHours();

                    mainFenster.lblNaechstePause.setText(
                            jetzigePause.pausenName() + " endet in: " +
                                     hours + ":" + decimalFormat.format((minutes % 60)) + ":" + decimalFormat.format((seconds % 60)) +
                                    " (" + jetzigePause.uhrzeit().getLast() + ")"
                    );

                    Thread.sleep(1000);
                    continue;
                }

                if (stuff == null) {
                    mainFenster.lblNaechstePause.setText("Gar nix mehr für heute.");
                } else {
                    Instant jetztInstant = now.toInstant(ZoneOffset.UTC);
                    Instant dannInstant = stuff.instant();

                    Duration res = Duration.between(jetztInstant, dannInstant);

                    long seconds = res.getSeconds();
                    long minutes = res.abs().toMinutes();
                    long hours = res.abs().toHours();

                    mainFenster.lblNaechstePause.setText(stuff.key() + " in: " + hours + ":" + decimalFormat.format((minutes % 60)) + ":" + decimalFormat.format((seconds % 60))
                            + " (" + stuff.uhrzeit().getFirst() + ")");

                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
