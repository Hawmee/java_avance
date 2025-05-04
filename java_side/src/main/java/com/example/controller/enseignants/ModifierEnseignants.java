package com.example.controller.enseignants;

import com.example.API_clients.ApiClients;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ModifierEnseignants extends JPanel {

    private JPanel header, content, formContainer, headerContainer, bodyContainer, buttonWrapper, enseignantPage;
    private JLabel title, contentTitle, nomLabel, matriculeLabel, tauxLabel, nbrLabel;
    private JTextField nom, matricule, taux, nbr;
    private JButton valider, annuler;
    private CardLayout enseignantLayout;
    private GridBagConstraints gbc;

    private MainEnseignants mainEnseignants;
    private int selectedProfId;

    public ModifierEnseignants(JPanel enseignantPage, MainEnseignants mainEnseignants, int profId) {
        this.mainEnseignants = mainEnseignants;
        this.selectedProfId = profId;
        this.enseignantPage = enseignantPage;
        this.enseignantLayout = (CardLayout) enseignantPage.getLayout();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        title = new JLabel("Enseignants > Modification");
        header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 8, 0, 8));
        title.setBorder(new EmptyBorder(22, 12, 22, 22));
        title.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        header.add(title, BorderLayout.NORTH);

        content = new JPanel(new FlowLayout(FlowLayout.CENTER));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(50, 0, 0, 0));
        formContainer = new JPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(380, 300));
        content.add(formContainer, BorderLayout.CENTER);
        headerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentTitle = new JLabel("Modification d'un Enseignant");
        formContainer.add(headerContainer, BorderLayout.NORTH);
        headerContainer.add(contentTitle);
        contentTitle.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        bodyContainer = new JPanel(new GridBagLayout());
        formContainer.add(bodyContainer, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomLabel = new JLabel("Nom: ");
        matriculeLabel = new JLabel("Matricule: ");
        tauxLabel = new JLabel("Taux Horaire: ");
        nbrLabel = new JLabel("Nombre d'Heure");
        nom = new JTextField();
        matricule = new JTextField();
        taux = new JTextField();
        nbr = new JTextField();

        nom.setPreferredSize(new Dimension(350, 30));
        matricule.setPreferredSize(new Dimension(350, 30));
        taux.setPreferredSize(new Dimension(350, 30));
        nbr.setPreferredSize(new Dimension(350, 30));


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        bodyContainer.add(nomLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(nom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        bodyContainer.add(matriculeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(matricule, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        bodyContainer.add(tauxLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(taux, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        bodyContainer.add(nbrLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(nbr, gbc);

        valider = new JButton("Valider");
        annuler = new JButton("Annuler");
        buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.add(annuler);
        buttonWrapper.add(valider);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(buttonWrapper, gbc);


        valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateProf();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enseignantLayout.show(enseignantPage, "mainEnseignants");
            }
        });


        populateForm(selectedProfId);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }


    public void populateForm(int profId) {
        try {
            JSONObject prof = ApiClients.getById("enseignants", profId);

            nom.setText(prof.optString("nom", ""));
            matricule.setText(prof.optString("matricule", ""));
            taux.setText(prof.optString("taux_horaire", ""));
            nbr.setText(prof.optString("nombre_heure", ""));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données : " + e.getMessage());
        }
    }

    private void updateProf() throws IOException {
            String matriculeValue = matricule.getText();
            String nomValue = nom.getText().toUpperCase();
            String tauxValue = taux.getText();
            String nbrValue = nbr.getText();
            int tauxInt ;
            int nbrInt ;

            try{
                tauxInt = Integer.parseInt(tauxValue);
                nbrInt = Integer.parseInt(nbrValue);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null , "Veuillez resepcter les types.");
                return;
            }

            if(nomValue.isEmpty() || matriculeValue.isEmpty() || tauxValue.isEmpty() || nbrValue.isEmpty()){
                JOptionPane.showMessageDialog(null , "Veuillez remplir les champs.");
                return;
            }

            JSONObject newEnseignants = new JSONObject();
            newEnseignants.put("matricule" , matriculeValue);
            newEnseignants.put("nom" , nomValue);
            newEnseignants.put("taux_horaire" , tauxInt);
            newEnseignants.put("nombre_heure" , nbrInt);

            JSONObject response = ApiClients.update("enseignants" , selectedProfId , newEnseignants);


            nom.setText("");
            matricule.setText("");
            taux.setText("");
            nbr.setText("");
            JOptionPane.showMessageDialog(null , "Prof mis a Jour !");
            mainEnseignants.loadProfs();
            mainEnseignants.chart.loadData();
            enseignantLayout.show(enseignantPage , "mainEnseignants");
    }
}
