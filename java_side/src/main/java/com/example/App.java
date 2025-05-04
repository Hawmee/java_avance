package com.example;

import com.example.controller.MainApp;

import javax.swing.*;


public class App {

//    private static void out(String text){
//        System.out.println(text);
//    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(MainApp::new);
    }
}

