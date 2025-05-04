package com.example.controller.enseignants;

import com.example.API_clients.ApiClients;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AjoutEnseignants extends JPanel {

    private JPanel header , content , formContainer , headerContainer, bodyContainer, buttonWrapper ,  enseignantPage;
    private JLabel title , contentTitle , nomLabel , matriculeLabel , taux_horaireLabel , volume_horaireLabel  ;
    private JTextField nom , matricule , taux_horaire , volume_horaire ;
    private JButton valider , annuler;
    private CardLayout enseignantLayout;
    private GridBagConstraints gbc;

    private MainEnseignants mainEnseignants;

    private JOptionPane jOptionPane = new JOptionPane();

    public AjoutEnseignants(JPanel enseignantPage , MainEnseignants mainEnseignant){

        this.mainEnseignants = mainEnseignant;

        this.enseignantPage = enseignantPage;
        this.enseignantLayout = (CardLayout) enseignantPage.getLayout();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        title = new JLabel("Enseignants > Ajout");
        header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0,8,0,8));
        title.setBorder(new EmptyBorder(22 , 12 , 22 , 22));
        title.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN , 14));
        header.add(title , BorderLayout.NORTH);

        content= new JPanel(new FlowLayout(FlowLayout.CENTER));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(50,0,0,0));
        formContainer = new JPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(380 , 300));
        content.add(formContainer , BorderLayout.CENTER);
        headerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentTitle = new JLabel("Ajouts d'enseignants");
        formContainer.add(headerContainer , BorderLayout.NORTH);
        headerContainer.add(contentTitle);
        contentTitle.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN , 14));
        bodyContainer = new JPanel(new GridBagLayout());
        formContainer.add(bodyContainer , BorderLayout.CENTER);

        gbc= new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill= GridBagConstraints.HORIZONTAL;

        nomLabel = new JLabel("Nom: ");
        matriculeLabel = new JLabel("Matricule: ");
        taux_horaireLabel = new JLabel("Taux Horaire: ");
        volume_horaireLabel = new JLabel("Nombre Heure: ");

        matricule = new JTextField();
        nom = new JTextField();
        taux_horaire= new JTextField();
        volume_horaire = new JTextField();

        nom.setPreferredSize(new Dimension(350,30));
        matricule.setPreferredSize(new Dimension(350,30));
        taux_horaire.setPreferredSize(new Dimension(350,30));
        volume_horaire.setPreferredSize(new Dimension(350,30));


        gbc.gridx = 0 ; gbc.gridy= 0 ;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(0,0,5,0);
        bodyContainer.add(matriculeLabel, gbc);

        gbc.gridx = 0 ; gbc.gridy= 1 ;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(matricule , gbc);

        gbc.gridx = 0 ; gbc.gridy= 2 ;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(0,0,5,0);
        bodyContainer.add(nomLabel , gbc);

        gbc.gridx = 0 ; gbc.gridy= 3 ;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(nom , gbc);

        gbc.gridx = 0 ; gbc.gridy= 4 ;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(0,0,5,0);
        bodyContainer.add(taux_horaireLabel , gbc);

        gbc.gridx = 0 ; gbc.gridy= 5 ;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(taux_horaire , gbc);

        gbc.gridx = 0 ; gbc.gridy= 6 ;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets= new Insets(0,0,5,0);
        bodyContainer.add(volume_horaireLabel , gbc);

        gbc.gridx = 0 ; gbc.gridy= 7 ;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(volume_horaire , gbc);

        valider= new JButton("Valider");
        annuler = new JButton("Annuler");
        buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.add(annuler);
        buttonWrapper.add(valider);

        gbc.gridx = 0 ; gbc.gridy= 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyContainer.add(buttonWrapper , gbc);



        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enseignantLayout.show(enseignantPage , "mainEnseignants");
            }
        });

        valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addProfs();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(header , BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private void addProfs() throws IOException {
        String matriculeValue = matricule.getText();
        String nomValue = nom.getText().toUpperCase();
        String taux_horaireValue = taux_horaire.getText();
        String volume_horaireValue = volume_horaire.getText();
        int taux;
        int volume;

        try {
            taux =  Integer.parseInt(taux_horaireValue);
            volume = Integer.parseInt(volume_horaireValue);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null , "Veuillez resepcter les types.");
            return;
        }

        if(nomValue.isEmpty() || matriculeValue.isEmpty() || taux_horaireValue.isEmpty() || volume_horaireValue.isEmpty()){
            JOptionPane.showMessageDialog(null , "Veuillez remplir les champs.");
            return;
        }

        JSONObject newEnseignants = new JSONObject();
        newEnseignants.put("matricule" , matriculeValue);
        newEnseignants.put("nom" , nomValue);
        newEnseignants.put("taux_horaire" , taux);
        newEnseignants.put("nombre_heure" , volume);

        JSONObject response = ApiClients.create("enseignants" , newEnseignants);


        nom.setText("");
        matricule.setText("");
        taux_horaire.setText("");
        volume_horaire.setText("");
        mainEnseignants.loadProfs();
        mainEnseignants.chart.loadData();
        JOptionPane.showMessageDialog(null , "Prof ajouté(e) avec success !");
        enseignantLayout.show(enseignantPage , "mainEnseignants");
    }
}
