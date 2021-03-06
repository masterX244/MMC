/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.server;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.API.Handler;
import org.cc86.MMC.API.Packet;
import org.cc86.MMC.API.Processor;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tgoerner
 */
public class EventManager implements Processor
{
    private static final Logger l = LogManager.getLogger();
    private HashMap<String,List<Handler>> listenerPool = new HashMap<>();
    
    
    public EventManager()
    {
        Main.m.setEvtmgr(this);
    }
    
    public void sendEventToRegisteredClients(final Packet p)
    {
        String eventID = (String) p.getData().get("command");
        l.trace("Event Dump:+\n"+ new Yaml().dump(p)+"\n End Event dump");
        l.trace("Listeners dump:+\n"+ new Yaml().dump(listenerPool)+"\n End Listener dump");
        if(eventID==null)
        {
            throw new InvalidParameterException("Don't be stupid, the command field cannot be Null");
        }
        else
        {
            
            List<Handler> listeners = listenerPool.get(eventID);
            l.trace("Listeners for event ID "+eventID+ " are existing:"+(listeners!=null?"yes":"no"));
            if(listeners!=null) //null wenn keine Listener registriert wurden auf dieses Event
            {
                listeners.forEach((Handler h)->h.respondToLinkedClient(p));
            }
        }
    }
    @Override
    public void process(Packet r, Handler h)
    {
        HashMap<String,Object> data = r.getData();
        String type = (String) data.get("type");
        String eventID = (String) data.get("eventID");
        String mode = (String) data.get("mode");
        if(type!=null&&type.equals("set")&&eventID!=null&&mode!=null)
        {
            switch (mode)
            {
                case "register":
                    l.info("registering "+h.getClientIP()+" for "+eventID);
                    List<Handler> listeners = listenerPool.get(eventID);
                    if(listeners==null)
                    {
                        listeners= new ArrayList<>();
                        listenerPool.put(eventID, listeners);
                    }
                    if(!listeners.contains(h))
                    {
                        listeners.add(h);
                    }
                    break;
                case "unregister":
                    List<Handler> listeners2 = listenerPool.get(eventID);
                    if(listeners2!=null&&listeners2.contains(h))
                    {
                        listeners2.remove(h);
                    }
                    break;
            }
        }
    }
    
    public void unregisterAllListenesForHandler(Handler h)
    {
        listenerPool.forEach((String nope,List<Handler>ll)->ll.remove(h));
    }
}
