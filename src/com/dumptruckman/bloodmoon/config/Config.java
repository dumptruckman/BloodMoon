package com.dumptruckman.bloodmoon.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3791277843215238935L;
    
    private File file;
    
    public Config(File file) {
        this.file = file;
    }
    
    public void loadConfig() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            try {
                this.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void saveConfig() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            try {
                this.store(fos, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
