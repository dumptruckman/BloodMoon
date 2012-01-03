package com.dumptruckman.bloodmoon.server;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class SimpleProcessHandler implements ProcessHandler {
	
    	private Process process = null;
    	private OutputStreamWriter outputStreamWriter = null;
    	private StreamHandler streamHandler = null;

    public boolean start(String command) {
    		//File jar = new File(gui.config.cmdLine.getServerJar());
    	this.setOutputHandler(null);
        try {
            // Run the server
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            this.setProcess(pb.start());

            this.setOutputHandler(new StreamHandler(this.getProcess().getInputStream()));
            this.getOutputHandler().start();
            this.outputStreamWriter = new OutputStreamWriter(this.getProcess().getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Problem starting server");
            e.printStackTrace();
            return false;
        }
        return true;
    	}
    
    public void stop() {
        if (getProcess() != null)
            getProcess().destroy();
    }

    	public Process getProcess() {
        return process;
    	}

    	private void setProcess(Process process) {
        this.process = process;
    }
	
    	public StreamHandler getOutputHandler() {
        return streamHandler;
    }
	
    private void setOutputHandler(StreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }
	
    	public void sendText(String text) {
        if (outputStreamWriter != null) {
            try {
                	outputStreamWriter.write(text + "\n");
                	outputStreamWriter.flush();
            } catch (IOException e) {
                
            }
        }
    }
}
