/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio.drivers.technics.se540;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tgoerner
 */
 @SuppressWarnings("PointlessBitwiseExpression")
public class ProtocolHandler
{
      private static final Logger l = LogManager.getLogger();
      
    private ArrayList<Byte> receivebuffer = new ArrayList<>();
    private final List<Consumer<List<Byte>>> listeners = new ArrayList<>(256);
    Thread handlingThread;
    private static final int MAX_PACKET_SIZE=11;
    private static final byte INVERTED_SIZE_MASK = 0x38;
    private static final int INVERTED_SIZE_OFFSET = 3;
    private static final byte CNT_MASK = (byte)0xC0;
    private static final int CNT_OFFSET = 6;
    private static final int CNT_LENGTH = 2;
    private static final int CNT_BYTE = 0;
    private static final byte BASE_SIZE_MASK = 0x07;
    private static final int BASE_SIZE_LENGTH = 3;
    private static final int[] CRC8_TABLE = new int[]
        {0, 94, 188, 226, 97, 63, 221, 131, 194, 156, 126, 32, 163, 253, 31, 65,
	157, 195, 33, 127, 252, 162, 64, 30, 95, 1, 227, 189, 62, 96, 130, 220,
	35, 125, 159, 193, 66, 28, 254, 160, 225, 191, 93, 3, 128, 222, 60, 98,
	190, 224, 2, 92, 223, 129, 99, 61, 124, 34, 192, 158, 29, 67, 161, 255,
	70, 24, 250, 164, 39, 121, 155, 197, 132, 218, 56, 102, 229, 187, 89, 7,
	219, 133, 103, 57, 186, 228, 6, 88, 25, 71, 165, 251, 120, 38, 196, 154,
	101, 59, 217, 135, 4, 90, 184, 230, 167, 249, 27, 69, 198, 152, 122, 36,
	248, 166, 68, 26, 153, 199, 37, 123, 58, 100, 134, 216, 91, 5, 231, 185,
	140, 210, 48, 110, 237, 179, 81, 15, 78, 16, 242, 172, 47, 113, 147, 205,
	17, 79, 173, 243, 112, 46, 204, 146, 211, 141, 111, 49, 178, 236, 14, 80,
	175, 241, 19, 77, 206, 144, 114, 44, 109, 51, 209, 143, 12, 82, 176, 238,
	50, 108, 142, 208, 83, 13, 239, 177, 240, 174, 76, 18, 145, 207, 45, 115,
	202, 148, 118, 40, 171, 245, 23, 73, 8, 86, 180, 234, 105, 55, 213, 139,
	87, 9, 235, 181, 54, 104, 138, 212, 149, 203, 41, 119, 244, 170, 72, 22,
	233, 183, 85, 11, 136, 214, 52, 106, 43, 117, 151, 201, 74, 20, 246, 168,
	116, 42, 200, 150, 21, 75, 169, 247, 182, 232, 10, 84, 215, 137, 107, 53};
    private static final int CRC8_START = 0xff;
    
    
    private static final int SRV_BYTE = 1;
    private static final int SRV_MASK = 0x07;
    private static final int SRV_LENGTH = 3;
    private static final int SRV_OFFSET = 0;
    public static final int SRV_RET = 0;
    public static final int SRV_SET = 1;
    public static final int SRV_GET = 2;
    public static final int SRV_EVT = 3;
    public static final int SRV_SYS = 4;
    
    private static final int CMD_BYTE = 1;
    private static final int CMD_MASK = 0xF8;
    private static final int CMD_OFFSET = 3;
    private static final int CMD_LENGTH = 5;
    private static final int CMD_VERSION_VER = 1;
    private static final int CMD_RESET_RST = 2;
    private static final int CMD_BOOTLOADER_BOOT = 3;
    private static final int CMD_EVENT_EVT = 4;
    private static final int CMD_STATISTIC_STAT = 5;
    private static final int CMD_POWER_PWR = 6;
    private static final int CMD_VOLUME_VOL = 7;
    private static final int CMD_VOLUME_VOLREL = 8;
    private static final int CMD_VOLUME_MUTE = 9;
    private static final int CMD_VOLUME_BALREL = 10;
    private static final int CMD_VOLUME_BAL = 11;
    private static final int CMD_SOURCE_SRC = 12;
    private static final int CMD_SPEAKER_SPK = 13;
    private static final int CMD_SPEAKER_SPK_CFG = 14;
    private static final int CMD_TIME_TIME = 15;
    
    private static final List<Consumer<List<Byte>>> evtListeners = new ArrayList<>(2<<CMD_LENGTH);//initiale größe sollte die commands alle aufnehmen können
    static
    {
        evtListeners.add(CMD_STATISTIC_STAT,null);
        
        evtListeners.add(CMD_POWER_PWR,null);
        
        Consumer<List<Byte>> vollistener = EventHandlerVolume::handleEvent;
        evtListeners.add(CMD_VOLUME_VOL,vollistener);
        evtListeners.add(CMD_VOLUME_MUTE,vollistener);
        evtListeners.add(CMD_VOLUME_BAL,vollistener);
        
        evtListeners.add(CMD_SOURCE_SRC,EventHandlerSource::handleEvent);
        
        evtListeners.add(CMD_SPEAKER_SPK,null);
    }
    
    
    private static final int USRDATA_REQ_START_BYTE=2;
    
    
    private static final int USRDATA_RET_START_BYTE=3;
    private static final int RET_ST_BYTE = 1;
    private static final int RET_ST_MASK = 0x18;
    private static final int RET_ST_OFFSET = 3;
    private static final int RET_ST_LENGTH = 2;
    private static final int RET_ST_ACK = 1;
    
    private static final int RET_ST_PND = 2;
    private static final int RET_ST_NACK = 3;
    private static final int REQ_CRC_BYTE = 1;
    
    private static final byte NACK_UNKNOWN = 1;
    
    private static final byte NACK_INVALID = 1;
    
    private static final byte NACK_BUSY = 1;
    
    
    
    
    private static byte crc(int oldcrc,int in)
    {
        if(oldcrc<0)
        {
            oldcrc=(oldcrc+128)&0xff;
        }
        if(in<0)
        {
            in=(in+128)&0xff;
        }
        return(byte) CRC8_TABLE[oldcrc^in];
    }
    public ProtocolHandler()
    {
        handlingThread = new Thread(()->
        {
            while(true)
            {
                
                List<Byte> suspected_package = receivebuffer.subList(0, (MAX_PACKET_SIZE>receivebuffer.size()?receivebuffer.size():MAX_PACKET_SIZE));
                
                byte hdr = suspected_package.get(0);
                int size_first = ((hdr^0xff)&INVERTED_SIZE_MASK)>>INVERTED_SIZE_OFFSET;
                int size_last = (hdr&BASE_SIZE_MASK);
                
                if(size_first==size_last&&size_first+3<suspected_package.size())
                {
                    int crc = CRC8_START;
                    int srv = suspected_package.get(SRV_BYTE)&SRV_MASK;
                    int extrabyte = srv==0?1:0;
                    for(int i=0;i<size_first+2+extrabyte-1;i++)
                    {
                        crc = crc((byte)crc,suspected_package.get(i));
                    }
                    if(crc==suspected_package.get(suspected_package.size()-1))
                    {
                        List<Byte> packet = new ArrayList<>(10);
                        for(int i=0;i<size_first;i++)
                        {
                            packet.add(suspected_package.get(i));
                            receivebuffer.remove(0);
                            packetReceived(packet);
                        }
                    }
                    else
                    {
                        receivebuffer.remove(0);
                    }
                }
                else
                {
                    receivebuffer.remove(0);
                }
            }
        });
        handlingThread.setName("ProtocolReceiverSE540");
    }
    public void startReceive()
    {
        handlingThread.start();
    }
    
    public void receiveByte(byte b)
    {
        receivebuffer.add(b);
    }
    
    /**
     * Removes the specified callback
     * @param crc The crc of the sent request
     */
    public void unregister_callback(int crc)
    {
        listeners.remove(crc);
    }
    /**
     * Generates a packet for sending via the UART. CRC and frame is added by this method before send
     * @param cnt 2-bit counter that should change when sending new packet
     * @param service integer ID of the selected service
     * @param command integer ID of the command type, see SRV_* constants
     * @param userdata Userdata bytes of the packet
     * @param callback Callback handler for responses to this packet;
     * @throws InvalidPacketException
     */
    public void send_packet(int cnt, int service,int command, List<Byte> userdata, Consumer<List<Byte>> callback) throws InvalidPacketException
    {
        List<Byte> raw_packet = new ArrayList<>(MAX_PACKET_SIZE);
        int udsize = userdata.size();
        if(udsize>(1<<BASE_SIZE_LENGTH))
        {
            throw new InvalidPacketException("Userdata too long");
        }
        if(cnt>(1<<CNT_LENGTH))
        {
            throw new InvalidPacketException("Counter too large");
        }
        int udsize_inv = udsize^((1<<BASE_SIZE_LENGTH)-1);
        raw_packet.add(0, (byte)((cnt<<CNT_OFFSET)|(udsize_inv<<INVERTED_SIZE_OFFSET)|(udsize)));
        if(command>(1<<CMD_LENGTH))
        {
            throw new InvalidPacketException("Command out of range");
        }
        if(service>(1<<SRV_LENGTH))
        {
            throw new InvalidPacketException("Service out of range");
        }
        raw_packet.add(CMD_BYTE,(byte)0);
        if(CMD_BYTE!=SRV_BYTE)
        {
            raw_packet.add(SRV_BYTE,(byte)0);
        }
        raw_packet.add(CMD_BYTE,(byte)(raw_packet.get(CMD_BYTE)|(command<<CMD_OFFSET)));
        raw_packet.add(SRV_BYTE,(byte)(raw_packet.get(SRV_BYTE)|(service<<SRV_OFFSET)));
        int packetlength = USRDATA_REQ_START_BYTE+udsize;
        raw_packet.addAll(USRDATA_REQ_START_BYTE, userdata);
        int crc = CRC8_START;
        for(int i=0;i<packetlength;i++)
        {
            crc = crc((byte)crc,raw_packet.get(i));
        }
        raw_packet.add(packetlength,(byte)crc);
        
        listeners.add(crc, (packet)->
        {
            int statebyte = packet.get(RET_ST_BYTE);
            statebyte=(statebyte&RET_ST_MASK)>>RET_ST_OFFSET;
            if(statebyte==RET_ST_PND)
            {
                //TODO check timeout; error out if too late;
                return;
            }
            if(callback!=null)
            {
                callback.accept(packet);
            }
        });
        
        //TODO consumer erstellen der auf Resend nötig prüft, dortrein dann den echten callback falls richtige antwort
    }

    /**
     * Sends a response packet
     * @param cnt Continue value of the source packet
     * @param req_crc CRC of the source packet
     * @param ret_st Statuscode of the response
     * @param userdata payload packets
     * @throws InvalidPacketException 
     */
    public void send_response(int cnt, int req_crc,int ret_st, List<Byte> userdata) throws InvalidPacketException
    {
        List<Byte> raw_packet = new ArrayList<>(MAX_PACKET_SIZE);
        int udsize = userdata.size();
        if(udsize>(1<<BASE_SIZE_LENGTH))
        {
            throw new InvalidPacketException("Userdata too long");
        }
        if(cnt>(1<<CNT_LENGTH))
        {
            throw new InvalidPacketException("Counter too large");
        }
        if(ret_st>(1<<RET_ST_LENGTH))
        {
            throw new InvalidPacketException("Response-Status too large");
        }
        int udsize_inv = udsize^((1<<BASE_SIZE_LENGTH)-1);
        raw_packet.add(0, (byte)((cnt<<CNT_OFFSET)|(udsize_inv<<INVERTED_SIZE_OFFSET)|(udsize)));
        raw_packet.add(CMD_BYTE,(byte)0);
        if(RET_ST_BYTE!=SRV_BYTE)
        {
            raw_packet.add(RET_ST_BYTE,(byte)0);
        }
        raw_packet.add(RET_ST_BYTE,(byte)(raw_packet.get(RET_ST_BYTE)|(ret_st<<RET_ST_OFFSET)));
        raw_packet.add(SRV_BYTE,(byte)(raw_packet.get(SRV_BYTE)|(SRV_RET<<SRV_OFFSET)));
        int packetlength = USRDATA_RET_START_BYTE+udsize;
        raw_packet.addAll(USRDATA_RET_START_BYTE, userdata);
        int crc = CRC8_START;
        for(int i=0;i<packetlength;i++)
        {
            crc = crc((byte)crc,raw_packet.get(i));
        }
        raw_packet.add(packetlength,(byte)crc);
        
        
        //TODO consumer erstellen der auf Resend nötig prüft, dortrein dann den echten callback falls richtige antwort
    }
    
    
    
    //Todo trigger resend bei PacketLoss via timeout
    private void packetReceived(List<Byte> packet)
    {
       
        int srv = packet.get(SRV_BYTE)&SRV_MASK>>SRV_OFFSET;
        //int ret_ST = (packet.get(RET_ST_BYTE)&RET_ST_MASK)>>RET_ST_OFFSET;
        int req_crc = packet.get(REQ_CRC_BYTE);
        if(srv!=SRV_RET)
        {
            if(srv!=SRV_EVT)
            {
                return;
            }
            handle_event(packet);
            return;
        }
        if(req_crc<0)
        {
            req_crc=(req_crc+128)&0xFF;
        }
        if(listeners.get(req_crc)!=null)
        {
            listeners.get(req_crc).accept(packet);
        }
        
    }
    private void handle_event(List<Byte> packet)
    {
        int cmd=packet.get(CMD_BYTE)&CMD_MASK>>CMD_OFFSET;
        int req_crc=packet.get(packet.size()-1);
        int cnt = packet.get(CNT_BYTE)&CNT_MASK>>CNT_OFFSET;
        Consumer<List<Byte>> eventhandler = evtListeners.get(cmd);
        if(eventhandler!=null)
        {
            eventhandler.accept(packet);
        }
        else
        {
            
            this.send_response(cnt,req_crc , RET_ST_NACK,Arrays.asList(new Byte[]{NACK_UNKNOWN}));
            l.warn("Got unknown command ID {} in event response",cmd);
            
        }
    }
}
