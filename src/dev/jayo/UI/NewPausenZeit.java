package dev.jayo.UI;

import dev.jayo.Misc.PausenPlan;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Map;

public class NewPausenZeit extends JFrame {
    private JPanel panel1;
    private JTextField tfUhrzeitVon;
    private JButton btnSpeichern;
    private JComboBox cbTag;
    private JTextField tfUhrzeitBis;
    private JButton btnAbbrechen;
    private JTextField tfPausenName;

    private final Map<String, Integer> tagInts = Map.of(
            "Montag", 0,
            "Dienstag", 1,
            "Mittwoch", 2,
            "Donnerstag", 3,
            "Freitag", 4
    );
    private final String[] tage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    private ArrayList<String> getUhrzeit() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        String Uhrzeit1 = tfUhrzeitVon.getText();

        if (Uhrzeit1.isEmpty()) {
            throw new Exception("Uhrzeit \"Von\" fehlt!");
        }

        String Uhrzeit2 = tfUhrzeitBis.getText();

        LocalTime.parse(Uhrzeit1, formatter);


        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add(Uhrzeit1);
        if (!Uhrzeit2.isEmpty()) {
            LocalTime.parse(Uhrzeit2, formatter);
            toReturn.add(Uhrzeit2);
        }

        return toReturn;
    }

    public NewPausenZeit(PausenZeitenEditor UI, PausenPlan plan, String tag, String pausenName, ArrayList<String> Uhrzeit) {
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Pausen Zeiten Editor");
        setLocationRelativeTo(UI);
        setVisible(true);

        if (pausenName != null) {
            tfPausenName.setText(pausenName);
        }

        if (tag != null) {
            cbTag.setSelectedIndex(tagInts.get(tag));
        }

        if (Uhrzeit != null) {
            if (Uhrzeit.getFirst() != null) {
                tfUhrzeitVon.setText(Uhrzeit.getFirst());
            }
            if (Uhrzeit.get(1) != null) {
                tfUhrzeitBis.setText(Uhrzeit.get(1));
            }
        }


        btnSpeichern.addActionListener(e -> {
            if (tfUhrzeitVon.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte geben sie eine \"Von\" Uhrzeit an!");
            } else {
                try {
                    ArrayList<String> Uhrzeit2 = getUhrzeit();

                    plan.update(
                            tage[cbTag.getSelectedIndex()],
                            tfPausenName.getText(),
                            Uhrzeit2
                            );
                    UI.encodeTable(plan);
                    this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                    ex.printStackTrace();
                }


            }
        });
        btnAbbrechen.addActionListener(e -> {
            this.dispose();
        });
    }

    public NewPausenZeit(PausenZeitenEditor UI, PausenPlan plan, String tag, String pausenName) {
        new NewPausenZeit(UI, plan, tag, pausenName, null);
    }

    public NewPausenZeit(PausenZeitenEditor UI, PausenPlan plan) {
        new NewPausenZeit(UI, plan, null, null, null);
    }
}
