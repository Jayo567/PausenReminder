package dev.jayo.UI;

import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.UserSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemindMeWhen extends JFrame {
    private JList list1;
    private JButton btnNew;
    private JButton btnDel;
    private JButton btnSaveAndQuit;
    private JPanel panel1;

    private UserSettings userSettings = DateiLocations.userSettings;
    private DefaultListModel<Integer> listModel = new DefaultListModel<>();

    private void updateList() {
        listModel.clear();

        for (Integer i : userSettings.erinnerungZeiten) {
            listModel.addElement(i);
        }
    }

    public RemindMeWhen(Settings ui) {
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(ui);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Erinnere mich...");
        setVisible(true);

        list1.setModel(listModel);

        updateList();

        btnNew.addActionListener(e -> {
            String angabe = JOptionPane.showInputDialog("Geben sie eine zahl ein.");
            try {
                int Int = Integer.parseInt(angabe);

                userSettings.erinnerungZeiten.add(Int);
                updateList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Das ist keine zahl.");
            }
        });
        btnSaveAndQuit.addActionListener(e -> {
            DateiLocations.saveUserSettings();
            this.dispose();
        });
        btnDel.addActionListener(e -> {
            int[] liste = list1.getSelectedIndices();

            for (int i : liste) {
                Integer resultat = listModel.getElementAt(i);

                userSettings.erinnerungZeiten.remove(resultat);
                listModel.remove(i);
                updateList();
            }
        });
    }
}
