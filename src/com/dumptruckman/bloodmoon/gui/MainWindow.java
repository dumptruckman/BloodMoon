package com.dumptruckman.bloodmoon.gui;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.dumptruckman.bloodmoon.config.Config;
import com.dumptruckman.bloodmoon.server.ProcessHandler;
import com.dumptruckman.bloodmoon.server.SimpleProcessHandler;

import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JScrollPane;

public class MainWindow extends JFrame {
	
    	public MainWindow() {
        config = new Config(new File("BloodMoon.properties"));
        config.loadConfig();
        
        getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][][]"));
    
        scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, "cell 0 0,grow");
        
        outputPane = new JTextPane();
        outputPane.setToolTipText("Path to Terraria goes here!");
        scrollPane.setViewportView(outputPane);
        outputPane.setEditable(false);
        
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendTextToProcess();
            }
        });
        getContentPane().add(inputField, "flowx,cell 0 1,growx");
        inputField.setColumns(10);
        
        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent arg0) {
                sendTextToProcess();		
            	}
        });
        getContentPane().add(btnSend, "cell 0 1");
        
        btnStartStop = new JButton("Start");
        btnStartStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (btnStartStop.getText().equals("Start"))
                    startProcess();
                else
                    stopProcess();
            }
        });
        getContentPane().add(btnStartStop, "flowx,cell 0 2");
        
        btnHide = new JButton("Hide");
        btnHide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hideUnhideWindow();
            }
        });
        getContentPane().add(btnHide, "cell 0 2");
        
        pathField = new JTextField();
        pathField.setToolTipText("Whatever you want to run goes here.");
        getContentPane().add(pathField, "cell 0 2,growx");
        pathField.setColumns(10);
        pathField.setText(getConfig().getProperty("path", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\terraria\\TerrariaServer.exe"));
        
        enableSystemTrayIcon();
    	}
    
    /**
     * 
     */
    	private static final long serialVersionUID = 1892077366412840497L;
    	private JTextField inputField;
    	private JTextPane outputPane;
    	private JButton btnSend;
    	
    	ProcessHandler processHandler = null;
    	private JScrollPane scrollPane;
    	private JButton btnStartStop;
    
    private Timer serverTask = null;
    private JButton btnHide;
    private boolean isHidden = false;
    
    private TrayIcon trayIcon = new TrayIcon(new BufferedImage(5,5,BufferedImage.TYPE_BYTE_GRAY));
    private JTextField pathField;
    
    private Config config;
    
    
    public final void startProcess() {
        getConfig().setProperty("path", pathField.getText());
        getConfig().saveConfig();
        outputPane.setText("");
        setProcessHandler(new SimpleProcessHandler());
        if (!getProcessHandler().start(pathField.getText())) {
            outputPane.setText("Error starting server!");
            return;
        }
        btnStartStop.setText("Force Stop");
        
        // Create a timer to read lines from the 
        serverTask = new Timer();
        serverTask.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String line = null;
                if (getProcessHandler() == null) {
                    endTimer();
                    return;
                }
                line = getProcessHandler().getOutputHandler().getLine(); 
                if (getProcessHandler().getProcess() == null) {
                    endTimer();
                    return;
                }
                final String finalLine = line;
                if (!finalLine.isEmpty())
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        outputPane.setText(outputPane.getText() + finalLine);
                    }
                });
            }
        }, 0, 50);
    }
    
    private void endTimer() {
        setProcessHandler(null);
        btnStartStop.setText("Start");
        serverTask.cancel();
        return;
    }
    
    public final void stopProcess() {
        getProcessHandler().stop();
        btnStartStop.setText("Start");
    }
    
    public final void sendTextToProcess() {
        if (getProcessHandler() != null) {
            getProcessHandler().sendText(inputField.getText());
            inputField.setText("");
        }
    }
    
    private synchronized ProcessHandler getProcessHandler() {
            return this.processHandler;
    }
    
    private synchronized void setProcessHandler(ProcessHandler handler) {
            this.processHandler = handler;
    }
    
    public Config getConfig() {
        return this.config;
    }
    
    private void enableSystemTrayIcon() {
        SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                if (java.awt.SystemTray.isSupported()) {
                    btnHide.setEnabled(true);
                    btnHide.setToolTipText("Press this to minimize to tray.");
                    // get the SystemTray instance
                    java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
                    // create a action listener to listen for default action executed on the tray icon
                    ActionListener listener = new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            hideUnhideWindow();
                        }
                    };
                    // create a popup menu
                    java.awt.PopupMenu popup = new java.awt.PopupMenu();
                    // create menu item for the default action
                    java.awt.MenuItem defaultItem = new java.awt.MenuItem("Show/Hide");
                    defaultItem.addActionListener(listener);
                    popup.add(defaultItem);

                    trayIcon.setImageAutoSize(true);
                    // set the TrayIcon properties
                    trayIcon.addActionListener(listener);
                    // add the tray image
                    try {
                        tray.add(trayIcon);
                    } catch (java.awt.AWTException e) {
                        System.err.println(e);
                    }
                } else {
                    btnHide.setEnabled(false);
                    btnHide.setToolTipText("Your Operating System does not support this action!");
                }
            }
        });
    }
    
    @SuppressWarnings("deprecation")
    private void hideUnhideWindow() {
        if (!isHidden) {
            show(false);
            isHidden = true;
        } else {
            show(true);
            isHidden = false;
        }
    }
}
