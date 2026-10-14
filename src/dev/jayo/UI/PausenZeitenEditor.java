package dev.jayo.UI;

import com.google.gson.Gson;
import dev.jayo.Misc.DateiLocations;
import dev.jayo.Misc.PausenPlan;
import dev.jayo.Misc.TagRunnable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PausenZeitenEditor extends JFrame {
    private JTable tblMain;
    private JButton btnEdit;
    private JPanel panel1;
    private JButton btnSpeichern;
    private JButton neuButton;
    private JButton btnDel;
    private JButton btnSchlieszen;
    private String[] Tage = {"Pausen Name", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    private Map<String, Integer> tagColumns = Map.of(
            "Montag", 1,
            "Dienstag", 2,
            "Mittwoch", 3,
            "Donnerstag", 4,
            "Freitag", 5
    );

    private Map<Integer, String> tagColumnsReverse = Map.of(
            1, "Montag",
            2, "Dienstag",
            3, "Mittwoch",
            4, "Donnerstag",
            5, "Freitag"
    );


    public DefaultTableModel tableModel = new DefaultTableModel(Tage, 1) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    } ;


    public Integer getRowFromString(String string, int column) {
        for (var i = 0; i < tableModel.getRowCount(); i++) {
            Object value = tableModel.getValueAt(i, column);
            if (value instanceof String string1) {
                if (string.equals(string1)) {
                    return i;
                }
            }
        }
        return null;
    }

    public void encodeTable(PausenPlan plan) {
        tableModel.setRowCount(0);

        TagRunnable runnable = (tag, tagName) -> {
            for (String key : tag.keySet()) {
                ArrayList<String> Uhrzeit = tag.get(key);
                Integer reihe = getRowFromString(key, 0);

                if (reihe == null) {
                    int rows = tableModel.getRowCount();
                    System.out.println(rows);
                    tableModel.setRowCount(rows + 1);
                    System.out.println(tableModel.getRowCount());
                    tableModel.setValueAt(
                            key,
                            rows,
                            0
                    );
                    reihe = rows;
                    tableModel.fireTableDataChanged();
                    System.out.println(tableModel.getValueAt(rows, 0));
                }

                tableModel.setValueAt(
                        Uhrzeit,
                        reihe,
                        tagColumns.get(tagName)
                );
            }
        };

        plan.runTagRunnable(runnable);


    }

    public PausenZeitenEditor(JFrame UI, PausenPlan plan) {
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Pausen Zeiten Editor");
        setLocationRelativeTo(UI);
        setVisible(true);

        tblMain.setModel(tableModel);
        tblMain.getTableHeader().setReorderingAllowed(false);

        neuButton.addActionListener(e -> new NewPausenZeit(this, plan));
        btnEdit.addActionListener(e -> {
            int column = tblMain.getSelectedColumn();
            int row = tblMain.getSelectedRow();

            Object var = tableModel.getValueAt(row, column);
            System.out.println("X:"+ row + " Y:" + column);
            System.out.println(var);
            System.out.println(tableModel.getValueAt(row, 0));
            System.out.println(tagColumnsReverse.get(column));

            if (var instanceof ArrayList<?>) {
                ArrayList<String> list = (ArrayList<String>) var;
                new NewPausenZeit(
                        this,
                        plan,
                        tagColumnsReverse.get(column),
                        (String) tableModel.getValueAt(row, 0),
                        list
                );
            }
        });
        btnSchlieszen.addActionListener(e -> {
            this.dispose();
        });
        btnSpeichern.addActionListener(e -> {
            Gson gson = new Gson();

            File pausenJson = DateiLocations.getPausenJson();

            try (FileWriter in = new FileWriter(pausenJson)) {
                gson.toJson(plan, in);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDel.addActionListener(e -> {
            int ja = JOptionPane.showConfirmDialog(this, "Sind sie sicher?");

            if (ja == JOptionPane.YES_OPTION) {
                int column = tblMain.getSelectedColumn();
                int row = tblMain.getSelectedRow();

                plan.remove(
                        tagColumnsReverse.get(column),
                        (String) tableModel.getValueAt(row, 0)
                );
                this.encodeTable(plan);
            }
        });
    }
}
