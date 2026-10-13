package dev.jayo.UI;

import com.google.gson.Gson;
import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.PausenPlan;

import javax.swing.*;
import java.io.*;

public class HauptUI extends JFrame {
    private JPanel panel1;
    private JButton btnBearbeiten;
    private JLabel lblStatus;
    private JLabel lblNaechstePause;
    private JButton btnEnde;

    private PausenZeitenEditor pausenEditor;

    public HauptUI() {
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pausen Reminder");
        setVisible(true);

        DateiLocations.ensureFolders(this);

        PausenPlan plan = new PausenPlan();
        Gson gson = new Gson();

        File pausenJson = new File(DateiLocations.IT_ORDNER + File.separator +
                DateiLocations.JAYO_ORDNER + File.separator +
                DateiLocations.PAUSEN_ZEITEN);

        try (FileReader in = new FileReader(pausenJson)) {
            plan = gson.fromJson(in, PausenPlan.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(plan.Montag);

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
                throw new RuntimeException(ex);
            }

            System.exit(0);
        });
    }
}
