/**
 *Spielstein des Spiels Mahki
 */
package mahki;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import static mahki.Spielstein.colorToImage;

/**
 *
 * @author Dennis Walz
 */
public class Spielstein extends JLabel {
    
    private static final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW};   /*  Mögliche Farben des Spielsteins */
    private static int MINFARBEN = 2;   /*  Mindestanzahl an Farben in der Auswahl  */
    private static int MAXFARBEN = 5;   /*  Maximalanzahl an Farben in der Auswahl  */
    private final Color color;    /*    Farbe des Spielsteins   */
    private ImageIcon image;    /*  Bild auf dem Spielstein */
    private Spielstein stein;   /*  Objekt des Spielsteins  */
    
    /**
     * Spielstein als JLabel, beinhaltet Darstellungssteuerungung. Angebunden an SpielsteinClickEvent.class
     * @param color Farbe des Spielsteins
     * @param width Breite in Pixeln
     * @param heigth Höhe in Pixeln
     * @param gui GUI auf der der Spielstein dargestellt wird
     */
    Spielstein(Color color, int width, int heigth, GUI gui){
        this.color = color;
        this.resizeIcon(width, heigth);        
        this.addMouseListener(new SpielsteinClickEvent(this, gui));
        this.stein = this;       
    }    

    /**
     * Anpassung der Größe des Icons an die des Labels
     * @param width Breite 
     * @param heigth Höhe
     */
    void resizeIcon(int width, int heigth){
        
        try {
            image = colorToImage(color);
            Image resized = image.getImage().getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
            image.setImage(resized);
            this.setIcon(image);            
        } catch (IOException ex) {
            Logger.getLogger(Spielstein.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSize(width, heigth);
    }
    
    /**
     * Zufallsfarbe im Rahmen der erlaubten Farben erzeugen
     * @param anzahlFarbenImSpektrum Antahl gewünschter Farben
     * @return Zufallsfarbe
     */
    public static Color getRandomColor(int anzahlFarbenImSpektrum){
        
        if(anzahlFarbenImSpektrum < MINFARBEN)
            anzahlFarbenImSpektrum = MINFARBEN;
        else if (anzahlFarbenImSpektrum > MAXFARBEN)
            anzahlFarbenImSpektrum = MAXFARBEN;        
        if(anzahlFarbenImSpektrum >= colors.length)
            anzahlFarbenImSpektrum = colors.length - 1;
        return colors[new Random().nextInt(anzahlFarbenImSpektrum)];
    }
    
    /**
     * Erzeugen einer Explosionsanimation
     */
    public void explode() {
        
        new Timer().scheduleAtFixedRate(new TimerTask() {

            int count = 0;
            @Override
            public void run() {
                
                ImageIcon icon = null;
                switch(count){
         
                case 0:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion0.png")); 
                    break;
                case 1:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion01.png")); 
                    break;
                case 2:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion02.png")); 
                    break;
                case 3:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion03.png")); 
                    break;
                case 4:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion04.png")); 
                    break;
                case 5:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion05.png")); 
                    break;
                case 6:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion06.png")); 
                    break;
                case 7:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion07.png")); 
                    break;
                case 8:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion08.png")); 
                    break;
                case 9:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion09.png")); 
                    break;
                case 10:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion10.png")); 
                    break;
                case 11:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion11.png")); 
                    break;
                case 12:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion12.png")); 
                    break;
                case 13:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion13.png")); 
                    break;
                case 14:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion14.png")); 
                    break;
                case 15:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion15.png")); 
                    break;
                case 16:
                        icon = new ImageIcon(Spielstein.class.getResource("images/explosion/explosion16.png")); 
                    break;
                case 17:
                        cancel();
                    break;
                }
                if(icon != null){
                 
                    Image resized = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    icon.setImage(resized);                
                }
                setIcon(icon);
                repaint();
                count++;
            }
        }, 0, 1000/16);      
    }
    
    /**
     * Farbe des Spielsteins
     * @return Farbe des Spielsteins
     */
    public Color getColor() {
        return this.color;
    }  
    
    /**
     * IconImage in den Urzustand zurücksetzen
     */
    void resetIcon() {
        this.setIcon(image);
        this.repaint();
    }
    
    /**
     * Farbe in Wort überführen
     * @param color Farbe
     * @return Farbe als Symbol
     */
    public static String colorToString(Color color) {
        if(color.equals(Color.RED))
            return "rot";
        if(color.equals(Color.BLUE))
            return "blau";
        if(color.equals(Color.GREEN))
            return "gruen";
        if(color.equals(Color.PINK))
            return "pink";
        if(color.equals(Color.YELLOW))
            return "gelb";
        if(color == null)
            return "";
        return null;
    }
    
    /**
     * Farbe in Symbol überführen
     * @param color Farbe
     * @return Farbe als Symbol
     */
    public static String colorToSymbol(Color color) {
        if(color.equals(Color.RED))
            return "☢";
        if(color.equals(Color.BLUE))
            return "☠";
        if(color.equals(Color.GREEN))
            return "☯";
        if(color.equals(Color.PINK))
            return "☮";
        if(color.equals(Color.YELLOW))
            return "☭";
        if(color == null)
            return "";
        return null;
    }

    /**
     * Farbe in ein Bild überführen
     * @param color Farbe
     * @return Zur Farbe gehöriges Bild
     * @throws IOException  Exception durch das Laden der Bilddatei
     */
    public static ImageIcon colorToImage(Color color) throws IOException {
               
        if(color == null)
            return new ImageIcon(Spielstein.class.getResource("images/empty.png"));
        if(color.equals(Color.RED))
            return new ImageIcon(Spielstein.class.getResource("images/rot.png"));
        if(color.equals(Color.BLUE))
             return new ImageIcon(Spielstein.class.getResource("images/blau.png"));
        if(color.equals(Color.GREEN))
            return new ImageIcon(Spielstein.class.getResource("images/gruen.png"));
        if(color.equals(Color.PINK))
             return new ImageIcon(Spielstein.class.getResource("images/pink.png"));
        if(color.equals(Color.YELLOW))
             return new ImageIcon(Spielstein.class.getResource("images/gelb.png"));
       
        return null;
    }
}
