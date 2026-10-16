package dev.jayo.UI;

import com.google.gson.Gson;
import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.PausenPlan;

import javax.swing.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class HauptUI extends JFrame {
    private JPanel panel1;
    private JButton btnBearbeiten;
    public JLabel lblNaechstePause;
    private JButton btnEnde;
    private JButton btnEinstellungen;

    private PausenZeitenEditor pausenEditor;
    private Settings settings;

    public PausenPlan plan = new PausenPlan();

    public HauptUI() {
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pausen Reminder");
        setVisible(true);

        DateiLocations.ensureFolders(this);

        Gson gson = new Gson();


//        File pausenJson = new File(DateiLocations.IT_ORDNER + File.separator +
//                DateiLocations.JAYO_ORDNER + File.separator +
//                DateiLocations.PAUSEN_ZEITEN);

        File pausenJson = DateiLocations.getPausenJson();

        try (FileReader in = new FileReader(pausenJson)) {
            plan = gson.fromJson(in, PausenPlan.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PausenPlan finalPlan = plan;
        btnBearbeiten.addActionListener(e -> {
            if (pausenEditor == null) {
                pausenEditor = new PausenZeitenEditor(this, finalPlan);
            } else {
                pausenEditor.setLocationRelativeTo(this);
                pausenEditor.setVisible(true);

            }
            pausenEditor.encodeTable(finalPlan);
        });

        btnEnde.addActionListener(e -> {
            try (FileWriter in = new FileWriter(pausenJson)) {
                gson.toJson(finalPlan, in);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Konnte nicht pausenZeiten.json speichern.");
            }


            DateiLocations.saveUserSettings();

            System.exit(0);
        });

        btnEinstellungen.addActionListener(e -> {
            if (settings == null) {
                settings = new Settings(this);
            } else if (!settings.isVisible()) {
                settings.setVisible(true);
            } else {
                settings.requestFocus();
            }
        });
    }
}
