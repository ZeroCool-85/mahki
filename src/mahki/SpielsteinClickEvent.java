/*
 * Listener für Klick auf Spielstein.
 */
package mahki;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Dennis Walz
 */
public class SpielsteinClickEvent implements MouseListener {

    private final Spielstein stein;
    private static boolean clickBlocked = false;
    private final GUI gui;

    /**
     * Event für Spielstein von Mahki
     * @param stein Zu behandelnder Spielstein
     * @param gui Gui auf der sich der Stein befindet
     */
    public SpielsteinClickEvent(Spielstein stein, GUI gui) {
        this.stein = stein;
        this.gui = gui;
    }

    /**
     * Leere Methode
     * @param e mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    /**
     * Leere Methode
     * @param e mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Handler für klicken des Spielsteins bei loslassen der Maus
     * @param e mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        
        /*  Abbrechen falls aktuell bereits eine Explosionsanimation läuft  */
        if(clickBlocked)
            return;
        
        if(gui.game.clickSpielstein(stein) > 0){
            clickBlocked = true;
            /*  Timer für Freigabe des Klickens und erneuern der Anzeige des Spielfeldes nach Explosion */
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {

                    gui.refresh();
                    clickBlocked = false;
                }
            }, 1000);
        }
            
    }
    /**
     * Leere Methode
     * @param e mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    /**
     * Leere Methode
     * @param e mouseEvent
     */ 
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
