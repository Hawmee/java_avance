package com.example.controller.enseignants;

import com.example.API_clients.ApiClients;
import com.example.controller.chart.Chart;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainEnseignants extends JPanel {
    private JTable table;
    private JScrollPane tableScrollPane;
    private JPanel header, headerButtons, content, enseignantPage;
    private JLabel statsLabel;
    private JButton ajout;
    private CardLayout enseignantsLayout;
    private DefaultTableModel tableModel;
    private int totalPres;
    private int maxPres;
    private int minPres;
    private int count;
    public Chart chart;

    public MainEnseignants(JPanel enseignantPage , Chart chart)  {
        this.enseignantPage = enseignantPage;
        this.enseignantsLayout = (CardLayout) enseignantPage.getLayout();
        this.chart = chart;


        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 8, 0, 8));

        JLabel headerLabel = new JLabel("Enseignants");
        headerLabel.setBorder(new EmptyBorder(22, 12, 22, 22));
        headerLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        header.add(headerLabel, BorderLayout.NORTH);

        // Panel for the "Ajouter" button on the west
        JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westPanel.setBackground(Color.WHITE);

        // Panel for the stats label on the east
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.setBackground(Color.WHITE);

        // Add Ajouter button to west panel
        ajout = new JButton("Ajouter");
        ajout.setPreferredSize(new Dimension(ajout.getPreferredSize().width, 30));
        westPanel.add(ajout);

        // Add stats label to east panel
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setBorder(new EmptyBorder(0, 0, 0, 10)); // Right padding
        eastPanel.add(statsLabel);

        // Add the panels to the header
        header.add(westPanel, BorderLayout.WEST);
        header.add(eastPanel, BorderLayout.EAST);
        ajout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enseignantPage.add(new AjoutEnseignants(enseignantPage, MainEnseignants.this), "ajoutEnseignants");
                enseignantsLayout.show(enseignantPage, "ajoutEnseignants");
            }
        });

        tableModel = new DefaultTableModel(new String[]{"id", "Matricule", "Nom", "Taux Horaire", "Nombre d'heure" , "presentations", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setRowHeight(32);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 32));
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor());
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBackground(Color.WHITE);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);

        content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(12, 2, 0, 2));
        content.add(tableScrollPane, BorderLayout.CENTER);


//        this.chart.loadData();
        loadProfs();
        revalidate();
        repaint();

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }


    public void loadProfs(){
        tableModel.setRowCount(0); // Clear previous data

        totalPres = 0;
        maxPres = Integer.MIN_VALUE;
        minPres = Integer.MAX_VALUE;

        if (tableModel != null) {
            tableModel.setRowCount(0);
        } else {
            System.err.println("Erreur : tableModel est null dans loadProfs");
        };
        try {
            JSONArray enseignants = ApiClients.getAll("enseignants");

            for (int i = 0; i < enseignants.length(); i++) {
                JSONObject prof = enseignants.getJSONObject(i);
                int presentation = prof.optInt("taux_horaire") * prof.optInt("nombre_heure");
                totalPres += presentation;
                maxPres = Math.max(maxPres, presentation);
                minPres = Math.min(minPres, presentation);
                Object[] row = {
                        prof.optInt("id"),
                        prof.optInt("matricule"),
                        prof.optString("nom"),
                        prof.optInt("taux_horaire"),
                        prof.optInt("nombre_heure"),
                        presentation,
                        "modifier",
                        "supprimer"
                };
                tableModel.addRow(row);
            }

            if (enseignants.length() == 0) {
                minPres = 0;
                maxPres = 0;
                totalPres = 0;
            }

            statsLabel.setText(String.format("Total: %d     |  Min: %d     |  Max: %d", totalPres,
                    tableModel.getRowCount() == 0 ? 0 : minPres,
                    tableModel.getRowCount() == 0 ? 0 : maxPres));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteProf(int selectedProfId) {
        JOptionPane.showMessageDialog(null, "Prof supprimé !");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int) tableModel.getValueAt(i, 0) == selectedProfId) {
                tableModel.removeRow(i);
                break;
            }
        }
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton = createStyledButton("Modifier", new Color(0, 123, 255));
        private final JButton deleteButton = createStyledButton("Supprimer", new Color(220, 53, 69));

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(editButton);
            add(deleteButton);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton editButton = createStyledButton("Modifier", new Color(0, 123, 255));
        private final JButton deleteButton = createStyledButton("Supprimer", new Color(220, 53, 69));
        private int selectedRow;

        public ButtonEditor() {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> {
                if (selectedRow != -1) {
                    int selectedProfId = (int) table.getModel().getValueAt(selectedRow, 0);
                    enseignantPage.add(new ModifierEnseignants(enseignantPage, MainEnseignants.this, selectedProfId), "modifierEnseignant");
                    enseignantsLayout.show(enseignantPage, "modifierEnseignant");
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                if (selectedRow != -1 && selectedRow < table.getRowCount()) {
                    int selectedProfId = (int) table.getModel().getValueAt(selectedRow, 0);
                    String[] options = {"Valider", "Annuler"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Voulez-vous supprimer cet élément ?",
                            "Suppression de prof",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    if (choice == 0) {
                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }
                        try {
                            ApiClients.delete("enseignants" , selectedProfId);
                            SwingUtilities.invokeLater(() -> {
                                loadProfs();
                                chart.loadData();
                                selectedRow = -1;
                            });
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun élément sélectionné ou données invalides.");
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.selectedRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return button;
    }
}
