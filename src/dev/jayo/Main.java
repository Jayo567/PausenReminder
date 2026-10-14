package dev.jayo;

import dev.jayo.Misc.NaechstePauseReturn;
import dev.jayo.UI.HauptUI;

import java.text.DecimalFormat;
import java.time.*;

// Heads up, this code base is a mix of German and English. "Denglisch" as we Germans might call it.

public class Main extends Thread {
    public HauptUI mainFenster;

    public static void main(String[] args) {
        Main thread = new Main();
        thread.mainFenster = new HauptUI();
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                LocalDateTime now = LocalDateTime.now();

                NaechstePauseReturn stuff = mainFenster.plan.getNaechstePause();

                if (stuff == null) {
                    mainFenster.lblNaechstePause.setText("Gar nix mehr für heute.");
                } else {
                    Instant jetztInstant = now.toInstant(ZoneOffset.UTC);
                    Instant dannInstant = stuff.instant();

                    Duration res = Duration.between(jetztInstant, dannInstant);

                    DecimalFormat decimalFormat = new DecimalFormat("00");

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
