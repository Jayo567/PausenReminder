package dev.jayo;

import dev.jayo.UI.HauptUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Thread {
    public HauptUI mainFenster;

    public static void main(String[] args) {
        Main thread = new Main();
        thread.start();
        thread.mainFenster = new HauptUI();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("hi");
                Thread.sleep(1000);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                LocalDateTime now = LocalDateTime.now();
                String formatted = now.format(formatter);

                if (mainFenster != null) {
                    mainFenster.lblNaechstePause.setText(formatted);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
