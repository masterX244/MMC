/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.stream;

import org.cc86.MMC.API.API;
import org.cc86.MMC.API.Plugin;

/**
 *
 * @author tgoerner
 */
public class StreamPlugin implements Plugin{
    
    StreamProcessor h;
    
    @Override
    public void register() {
        h = new StreamProcessor();
        API.getDispatcher().registerOnRequestType("vnc", h); 
        API.getDispatcher().registerOnRequestType("mp4", h); 
        API.getDispatcher().registerOnRequestType("miracast", h); 
    }
    @Override
    public String getName() {
        return "Radio";
    }
    @Override
    public void shutdown()
    {
        //h.shitdown();
    }
}
