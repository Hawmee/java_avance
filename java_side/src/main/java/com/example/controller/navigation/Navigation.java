package com.example.controller.navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Navigation {
    private CardLayout mainLayout , enseignantLayout ;
    private JPanel mainPages , enseignantPage , chartPages;

    public Navigation( JPanel mainPages , JPanel enseignantPage , JPanel chartPages){
        this.mainPages=mainPages;
        this.enseignantPage=enseignantPage;
        this.mainLayout = (CardLayout) mainPages.getLayout();
        this.enseignantLayout =(CardLayout) enseignantPage.getLayout();
        this.chartPages = chartPages;

    }

    public void addNavigationButttons(JButton enseignantsButton , JButton salleButton){
        enseignantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainLayout.show(mainPages , "enseignants");
                enseignantLayout.show(enseignantPage , "mainEnseignants");
            }
        });

        salleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainLayout.show(mainPages, "chart");
            }
        });

    }
}

