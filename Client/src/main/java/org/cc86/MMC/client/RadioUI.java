/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc86.MMC.client;

import java.awt.EventQueue;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import org.cc86.MMC.client.API.Module;

/**
 *
 * @author tgoerner
 */
public class RadioUI extends JPanel
{

    /**
     * Creates new form RadioUI
     */
    public RadioUI()
    {
        initComponents();
    }

    @Override
    public void setVisible(boolean b)
    {
        super.setVisible(b); 
        poke();
    }

    private void poke()
    {
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Radio)
            {
                ((Mod_Radio)m1).requestMappings();
            }
        }
    }

    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnPlayURL = new javax.swing.JButton();
        btnCancelPlayback = new javax.swing.JButton();
        txfURL = new javax.swing.JTextField();
        txfShortID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstFavoriten = new javax.swing.JList();
        btnShortIDSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnSelSender = new javax.swing.JButton();
        btnF5 = new javax.swing.JButton();
        lblStation = new javax.swing.JLabel();

        btnPlayURL.setText("URL abspielen");
        btnPlayURL.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPlayURLActionPerformed(evt);
            }
        });

        btnCancelPlayback.setText("Stream Stop");
        btnCancelPlayback.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelPlaybackActionPerformed(evt);
            }
        });

        lstFavoriten.setModel(new DefaultListModel<String>());
        lstFavoriten.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstFavoriten);

        btnShortIDSave.setText("Kurzname speichern");
        btnShortIDSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShortIDSaveActionPerformed(evt);
            }
        });

        jLabel1.setText("URL");

        jLabel2.setText("KurzID");

        btnSelSender.setText("Sender wählen");
        btnSelSender.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSelSenderActionPerformed(evt);
            }
        });

        btnF5.setText("Aktualisieren");
        btnF5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnF5ActionPerformed(evt);
            }
        });

        lblStation.setText("Aktuelle Station: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txfURL)
                            .addComponent(txfShortID)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPlayURL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSelSender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnF5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnShortIDSave)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(btnCancelPlayback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStation)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSelSender)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlayURL))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfShortID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShortIDSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelPlayback, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelSenderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelSenderActionPerformed
    {//GEN-HEADEREND:event_btnSelSenderActionPerformed
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Radio)
            {
                ((Mod_Radio)m1).switchToStation((String) lstFavoriten.getSelectedValue());
            }
        }
    }//GEN-LAST:event_btnSelSenderActionPerformed

    private void btnF5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnF5ActionPerformed
    {//GEN-HEADEREND:event_btnF5ActionPerformed
        poke();
    }//GEN-LAST:event_btnF5ActionPerformed

    private void btnShortIDSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnShortIDSaveActionPerformed
    {//GEN-HEADEREND:event_btnShortIDSaveActionPerformed
        Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Radio)
            {
                ((Mod_Radio)m1).addMappingForID(txfShortID.getText(), txfURL.getText());
            }
        }
    }//GEN-LAST:event_btnShortIDSaveActionPerformed

    private void btnPlayURLActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPlayURLActionPerformed
    {//GEN-HEADEREND:event_btnPlayURLActionPerformed
       Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Radio)
            {
                ((Mod_Radio)m1).switchToStation((String) txfURL.getText());
            }
        }
    }//GEN-LAST:event_btnPlayURLActionPerformed

    private void btnCancelPlaybackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelPlaybackActionPerformed
    {//GEN-HEADEREND:event_btnCancelPlaybackActionPerformed
         Module[] m = Main.getDispatcher().getModules();
        for (Object m1 : m) {
            if(m1 instanceof Mod_Radio)
            {
                ((Mod_Radio)m1).stopCurrentRadioStream();
            }
        }
    }//GEN-LAST:event_btnCancelPlaybackActionPerformed
    
    public void updateList(HashMap<String,String> mappings)
    {
        final DefaultListModel<String> mdl = (DefaultListModel<String>) lstFavoriten.getModel();
        mdl.clear();
        mappings.keySet().forEach(mdl::addElement);
    }

    public void updateStation(final String station)
    {
        EventQueue.invokeLater(()->lblStation.setText((!station.isEmpty())?"Aktuelle Station: "+station:"Kein Sender aktiv"));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelPlayback;
    private javax.swing.JButton btnF5;
    private javax.swing.JButton btnPlayURL;
    private javax.swing.JButton btnSelSender;
    private javax.swing.JButton btnShortIDSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStation;
    private javax.swing.JList lstFavoriten;
    private javax.swing.JTextField txfShortID;
    private javax.swing.JTextField txfURL;
    // End of variables declaration//GEN-END:variables
}
