/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.modules.audio;

import de.nplusc.izc.tools.baseTools.Tools;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

/**
 *
 * @author tgoerner
 */
public class SWTTYProvider implements TTYProvider
{
    //startup sudo pigpiod
    //M 10 W nach /dev/pigpio
    //Output in /dev/pigout
    //M 9 R
private static final Logger l = LogManager.getLogger();
    //cat auf /dev/pigout & /dev/pigerr
    //static String[] alphabet = "a#b#c#d#e#f#g#h#i#j#k#l#m#n#o#p#q#r#s#t#u#v#w#x#y#z".split("#");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       new SWTTYProvider().uartHandler(System.out::println, System.in, true);
    }

    private static void setup()
    {
        Tools.runCmdWithPassthru(IoBuilder.forLogger("External.sudoedChmod").buildPrintStream(),"sudo","chmod","777","/sys/class/softuart/softuart/data");
    }
    @Override
    public void uartHandler(final Consumer<Integer> out,final InputStream ctrl,final boolean addPrefix)
    {
        new Thread(()->
        {
            InputStream fis = null;
            OutputStream fos = null;

            try
            {
                //final Socket s = new Socket("127.0.0.1", 8888);
                //fis = new FileInputStream();
                if(!new File("/sys/class/softuart/softuart/data").exists())
                {
                    System.out.println("no running softuart driver, shutting down");
                    System.exit(0);
                }


                setup();
                fos = new FileOutputStream("/sys/class/softuart/softuart/data");
                PrintStream ps = new PrintStream(fos);
                //fos = s.getOutputStream();/*new InputStreamReader(s.getInputStream()*/

                new Thread(() ->
                {
                    BufferedInputStream br;
                    try
                    {
                       byte[] data = new byte[256000];
                       br = new BufferedInputStream(new FileInputStream("/sys/class/softuart/softuart/data"));
                       br.mark(4096);
                        while (true)
                        {
                            int len = br.read(data);
                            for(int i=0;i<len;i+=2)
                            {
                                int datapkg = ((data[i]&0xF0)>>4)|((data[i+1]&0xF8)<<1);
                                boolean parity = numberOfSetBits(datapkg)%2==0;
                                if(parity)
                                {
                                    out.accept(datapkg);
                                }
                            }
                            br.reset();
                            
                           try
                           {
                               Thread.sleep(100);
                           } catch (InterruptedException ex)
                           {
                           }
                        }
                    } catch (FileNotFoundException ex)
                    {
                        ex.printStackTrace();
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }).start();//*/
                 BufferedReader bs = new BufferedReader(new InputStreamReader(ctrl));
                while (true)
                {
                    l.warn("d'arvit");
                    String line = bs.readLine()+"\n";//alphabet[new Random().nextInt(26)] + alphabet[new Random().nextInt(26)] + alphabet[new Random().nextInt(26)] + "\r\n";
                    l.info("SENT_UART:"+line);
                    ps.print(line);
                    ps.flush();
                    //Thread.sleep(1000);
                }
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                try
                {
                    //fis.close();
                    fos.close();
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }}
        ).start();
    }
    
    private static int numberOfSetBits(int i)
    {
         // Java: use >>> instead of >>
         // C or C++: use uint32_t
         i = i - ((i >>> 1) & 0x55555555);
         i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
         return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
    }
}
