package dev.jayo.UI;

import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.UserSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {
    private JButton btnChangeTableLocation;
    private JButton btnRemindMeWhen;
    private JLabel lblTableLocation;
    private JPanel panel1;
    private JCheckBox cbGetNotifs;
    private JButton btnSaveAndQuit;

    private UserSettings userSettings = DateiLocations.userSettings;

    public Settings(HauptUI ui) {
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(ui);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Einstellungen");
        setVisible(true);

        cbGetNotifs.setSelected(userSettings.bekommtBenachrichtigungen);

        btnRemindMeWhen.addActionListener(e -> {
            new RemindMeWhen(ui);
        });
        cbGetNotifs.addActionListener(e -> {
            userSettings.bekommtBenachrichtigungen = cbGetNotifs.isSelected();
        });
        btnSaveAndQuit.addActionListener(e -> {
            DateiLocations.saveUserSettings();
            this.dispose();
        });
    }
}
