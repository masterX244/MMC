/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cc86.MMC.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cc86.MMC.client.API.Connection;

/**
 *
 * @author iZc <nplusc.de>
 */
public class Main
{
    private static final Logger l = LogManager.getLogger();
    private static UI ui;
    private static RadioUI radio;
    private static StreamUI stream;
    private static final Dispatcher disp = new Dispatcher();
    private static Connection c;
    
    private static String piIP;
    
    public static void main(String[] args)
    {
        //TODO PI-DETECTION
        String srvr =serverDiscovery();
        l.info(srvr);
        if(srvr.equals("0.0.0.0"))
        {
            l.error("NO SERVER FOUND");
            System.exit(0);
                    
        }
        c=new TCPConnection(srvr, 0xCC86);
        try {
            c.connect();
        } catch (IOException ex) {
            //System.out.println(ex.m);
            System.out.println("Ell-Emm-AhhX2");
            l.error("FAILED TO CONNECT");
            System.exit(0);
        }
        piIP=srvr;
        disp.connect(c);
        
        java.awt.EventQueue.invokeLater(()->
        {
             ui = new UI();
            //ui.setVisible(true);
            radio=new RadioUI();
            //radio.setVisible(true);
            stream=new StreamUI();
            stream.setVisible(true);
            
        });
        
        
    }
    public static Dispatcher getDispatcher()
    {
        return disp;
    }
    public static UI getUi()
    {
        return ui;
    }

    public static RadioUI getRadio()
    {
        return radio;
    }
    /*packahgeprotected*/ static void serverKillen()
    {
        Mod_Exit x = new Mod_Exit();
        x.connect(c);
        x.exxit();
    }
    
    public static String getServerIP()
    {
        return piIP;
    }
    
    
    
    public static String serverDiscovery()
    {
        String res="0.0.0.0";
        DatagramSocket c;
        // Find the server using UDP broadcast
        try
        {
            //Open a random port to send the package
            c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "DISCOVER_MMC_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            try
            {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 0xCC86);
                c.send(sendPacket);
                l.info("Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e)
            {
            }

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp())
                {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
                {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null)
                    {
                        continue;
                    }

                    // Send the broadcast package!
                    try
                    {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e)
                    {
                    }

                    l.info("Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            l.info("Done looping over all network interfaces. Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            l.info("Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_MMC_RESPONSE"))
            {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                res=(receivePacket.getAddress()+"").substring(1);
            }

            //Close the port!
            c.close();
        } catch (IOException ex)
        {
            
        }
        return res;
    }
}
