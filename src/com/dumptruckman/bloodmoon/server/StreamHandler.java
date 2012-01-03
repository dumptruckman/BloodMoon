package com.dumptruckman.bloodmoon.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamHandler extends Thread {
	
    	InputStream inputStream;
    	String line = null;

    // reads everything from is until empty. 
    StreamHandler(InputStream is) {
        this.inputStream = is;
    }

    public void run() {
        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(this.inputStream);
            br = new BufferedReader(isr);
            
            String temp = null;
            
            synchronized (this) {
                while ((temp = br.readLine()) != null) {
                    setLine(temp + System.getProperty("line.separator"));
                    try {
                        this.wait(0);
                    } catch (InterruptedException e) {
                        System.out.println("StreamGrabber thread interrupted");
                    }
                }
            }
            
            setLine("[Process Terminated!]");
            br.close();
        } catch (IOException ioe) {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            ioe.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public synchronized String getLine() {
        	if (this.line != null) {
            	String tempLine = this.line;
            	this.line = null;
            	this.notify();
            	return tempLine;
        }
        return "";
    }
    
    private synchronized String setLine(String newLine) {
        	this.line = newLine;
        	return this.line;
    }
}
