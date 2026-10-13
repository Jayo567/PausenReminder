package dev.jayo.UI;

import dev.jayo.Misc.PausenPlan;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public NewPausenZeit(JFrame UI, PausenPlan plan, String tag, String pausenName, ArrayList<String> Uhrzeit) {
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
            if (Uhrzeit.get(0) != null) {
                tfUhrzeitBis.setText(Uhrzeit.get(0));
            }
            if (Uhrzeit.get(1) != null) {
                tfUhrzeitBis.setText(Uhrzeit.get(1));
            }
        }


        btnSpeichern.addActionListener(e -> {

        });
    }

    public NewPausenZeit(JFrame UI, PausenPlan plan, String tag, String pausenName) {
        new NewPausenZeit(UI, plan, tag, pausenName, null);
    }

    public NewPausenZeit(JFrame UI, PausenPlan plan) {
        new NewPausenZeit(UI, plan, null, null, null);
    }
}
