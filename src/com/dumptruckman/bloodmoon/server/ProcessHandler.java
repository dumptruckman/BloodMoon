package com.dumptruckman.bloodmoon.server;

    public interface ProcessHandler {
    
    	public boolean start(String command);
    
    public void stop();
    	
    	public Process getProcess();
    	
    	public StreamHandler getOutputHandler();
    	
    	public void sendText(String text);
}
