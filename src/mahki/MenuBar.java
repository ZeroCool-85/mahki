/*
 *  Menüleiste für Mahki GUI
 * 
 */

package mahki;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Michael Holst
 */
public class MenuBar extends JMenuBar implements ActionListener {
    private final GUI gui;

    MenuBar(final GUI gui){
        
        this.gui = gui;
        JMenu menuDatei = new JMenu("Datei");
        JMenuItem menuItemOption = new JMenuItem("Optionen");
        
        JMenuItem menuItemRestart = new JMenuItem("Neustart");
        JMenuItem menuItemClose = new JMenuItem("Schließen");
        JMenu bearbeiten = new JMenu("Bearbeiten");
        JMenuItem menuItemBack = new JMenuItem();
        JMenu menuHighscore = new JMenu("Highscore");
        JMenuItem menuItemHighscore = new JMenuItem("Anzeigen");
        
        
        //Optionen Menüpunkt
        menuItemOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        menuItemOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("btn_icon/option.png"))); 
        menuItemOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOptionActionPerformed(evt);
            }
        });
        menuDatei.add(menuItemOption);
        
        //Neustart Menüpunkt
        menuItemRestart.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        menuItemRestart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mahki/btn_icon/neustart.png"))); 
        menuItemRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRestartActionPerformed(evt);
            }
        });
        menuDatei.add(menuItemRestart);

        //Schließen Menüpunkt
        menuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        menuItemClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mahki/btn_icon/schließen.png")));
        menuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCloseActionPerformed(evt);
            }
        });
        menuDatei.add(menuItemClose);
        this.add(menuDatei);

        //Bearbeiten Menü
        menuItemBack.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.ALT_MASK));
        menuItemBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mahki/btn_icon/zurück.png"))); 
        menuItemBack.setText("Schritt zurück");
        menuItemBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBackActionPerformed(evt);
            }
        });
        bearbeiten.add(menuItemBack);
        this.add(bearbeiten);     
        
        //Highscore Menü
        menuItemHighscore.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK));
        menuItemHighscore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mahki/btn_icon/highscore.png")));
        menuItemHighscore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new Highscore_Optionen(gui);
            }
        });
        menuHighscore.add(menuItemHighscore);       
        this.add(menuHighscore);             
    }    
    
    private void menuItemOptionActionPerformed(ActionEvent evt) {
       new Optionen(gui);
    }
    private void menuItemCloseActionPerformed(ActionEvent evt) {        
       System.exit(0);
    }
    private void menuItemRestartActionPerformed(ActionEvent evt) {
        gui.startGame();
    }
    private void menuItemBackActionPerformed(ActionEvent evt) {
       gui.undoTurn();
    }
    @Override
    public void actionPerformed(ActionEvent e) {        
    }

}
