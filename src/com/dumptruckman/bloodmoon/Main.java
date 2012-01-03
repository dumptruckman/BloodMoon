package com.dumptruckman.bloodmoon;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.dumptruckman.bloodmoon.gui.MainWindow;

public class Main {
	
    MainWindow mainWindow;
    
    	/**
    	 * @param args
    	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    	Main window = new Main();
                    window.mainWindow.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    	
    	/**
    	 * Create the application.
    	 */
    public Main() {
        initialize();
    }
    
    	/**
    	 * Initialize the contents of the frame.
    	 */
    	private void initialize() {
        		mainWindow = new MainWindow();
        		mainWindow.setBounds(100, 100, 450, 300);
        		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
