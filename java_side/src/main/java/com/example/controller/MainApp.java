package com.example.controller;

import com.example.controller.chart.Chart;
import com.example.controller.enseignants.MainEnseignants;
import com.example.controller.navigation.Navigation;
import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    private JPanel mainWindow;
    private JTextArea gestionDEnseignantsTextArea;
    private JButton enseignantsButton;
    private JButton sallesButton;
    private JButton occupationDesSallesButton;
    private JPanel MainPages;
    private JPanel pageEnseignants;
    private JPanel pageSalles;
    private JPanel pageOccupation;
    private JPanel ajoutEnseignants;
    private JPanel modifEnseignants;
    private JPanel suppEnseignants;
    private JPanel mainEnseignants;
    private JButton ajouterButton;
    private JTextArea enseignantsAjoutTextArea;
    private JPanel ajoutEnsTitle;
    private JPanel ajoutEnsContent;
    private JPanel ensFormContainer;
    private JPanel pageChart;
    private JPanel mainSalles;
    private JPanel navBarLayout;

//    private JPanel mainPagesLayout;

    public MainApp(){
        setTitle("Occupation Salle");
//        setSize(1750 , 600);
        setResizable(true);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainWindow);
        pack();
        int packedHeight = this.getHeight();
        setMinimumSize(new Dimension(1350 , packedHeight));
        setVisible(true);


        MainPages.add("enseignants" , pageEnseignants);
        MainPages.add("chart" , pageChart);

        Chart chart = new Chart();
        mainEnseignants = new MainEnseignants(pageEnseignants , chart);



        pageEnseignants.add("mainEnseignants" , mainEnseignants);
        pageChart.add(chart);


        CardLayout enseignantsLayout = (CardLayout) pageEnseignants.getLayout();


        enseignantsLayout.show(pageEnseignants , "mainEnseignants");

        Navigation navigation = new Navigation(MainPages, pageEnseignants , pageChart);
        navigation.addNavigationButttons(enseignantsButton , sallesButton);

    }


}
