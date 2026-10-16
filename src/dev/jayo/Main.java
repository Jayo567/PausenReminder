package dev.jayo;

import com.sshtools.twoslices.*;
import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.PausenState;
import dev.jayo.Misc.ReturnClasses.JetzigePause;
import dev.jayo.Misc.ReturnClasses.NaechstePauseReturn;
import dev.jayo.UI.HauptUI;

import java.text.DecimalFormat;
import java.time.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Heads up, this code base is a mix of German and English. "Denglisch" as we Germans might call it.

public class Main extends Thread {
    public HauptUI mainFenster;
    public static Main mainClass;

    private PausenState currentState = PausenState.WARTEN;
    private String currentPause = "";
    public boolean firstRunComplete = false;
    private static final ExecutorService toastExecutor = Executors.newSingleThreadExecutor();
    private static final ToastBuilder builder = new ToastBuilder();

    public Main() {
        mainClass = this;
    }

    public static void main(String[] args) {
        //DateiLocations.getAppData();
        DateiLocations.prepUserSettings();
        ToasterSettings settings = new ToasterSettings();

        settings.setAppName("Pausen Reminder");
        ToasterFactory.setSettings(settings);

        System.out.println(DateiLocations.userSettings);

        Main thread = new Main();
        thread.mainFenster = new HauptUI();
        thread.start();
    }

    public void sendNotificationAsync(ToastType type, String title, String description) {

        toastExecutor.submit(() -> {
            builder.title(title);
            builder.content(description);
            builder.type(type);

            try {
                builder.toast();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

                    if (DateiLocations.userSettings.bekommtBenachrichtigungen && currentState == PausenState.WARTEN) {
                        sendNotificationAsync(ToastType.INFO, "Pause Anfang!", jetzigePause.pausenName() + " hat angefangen! (1)");
                        currentState = PausenState.PAUSE;
                        currentPause = jetzigePause.pausenName();
                    }

                    Thread.sleep(1000);
                    continue;
                } else {
                    if (DateiLocations.userSettings.bekommtBenachrichtigungen && currentState == PausenState.PAUSE) {
                        sendNotificationAsync(ToastType.INFO, "Pause Vorbei!", currentPause + " ist vorbei.");
                        currentState = PausenState.WARTEN;
                        firstRunComplete = false;
                    }
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


                    if (!currentPause.equals(stuff.key()) && firstRunComplete && DateiLocations.userSettings.bekommtBenachrichtigungen) {
                        sendNotificationAsync(ToastType.INFO, "Pause Anfang!", currentPause + " hat angefangen! (2)");
                    } else {
                        firstRunComplete = true;
                    }

                    currentPause = stuff.key();

                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
