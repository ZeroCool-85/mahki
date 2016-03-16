/*
 * GUI für das Spiel Mahki und Main Methode des Spiels
 */

package mahki;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Dennis Walz und Michael Holst
 */
public class GUI{
    
    private final GUI gui;
    private Thread newGameThread;
    private Thread resizeThread;
    private final static int DEFAULT_WIDTHFIELDS = 15;  /* Default Anzahl Spielfelder X    */
    private final static int DEFAULT_HEIGHTFIELDS = 10; /* Default Anzahl Spielfelder X    */
    private final static int DEFAULT_COLORS = 4;    /* Default Anzahl Farben    */
    private final static int WINDOWWIDTH = 800; /*Fensterbreite */
    private final static int WINDOWHEIGTH = 600;    /*  Fenstergröße    */
    private final static int BORDERWIDTHX = 1;  /*  Rand des Spiels X   */
    private final static int BORDERWIDTHY = 4;  /*  Rand des Spiels y   */
    
    private HashSet<Component> comps;   /* Komponenten des Frames   */
    private JFrame loadingFrame;    /*  Ladebildschirm    */
    private HighScore highscore;    /*  Highscore des aktuellen Spiels  */  
    
    boolean isEndlessMode;  /*  Endless-Spielmodus aktiviert = true */
    JFrame guiFrame;    /*  Frame   */
    int gamefield_x; /* Anzahl Felder des Spielfeldes horizontal */
    int gamefield_y; /* Anzahl Felder des Spielfeldes vertikal */
    int monstersize; /* Grösse einer Spielfigur */
    int colors;  /* Anzahl Farben im Spielfeld */
    int framewidth; /* Breite des Frames */
    int frameheigth; /* Höhe des Frames */;
    Spielfeld game; /*  Spielfeld */
   
    public static void main(String[] args) {
        
        new GUI();
    }

    public GUI()
    {         
        /** Frame initialisieren */          
        guiFrame = new JFrame();
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Mahki Gridbag Version");
        guiFrame.setSize(WINDOWWIDTH, WINDOWHEIGTH);
        guiFrame.setLocationRelativeTo(null);
        try {
            guiFrame.setIconImage(Spielstein.colorToImage(Color.GREEN).getImage());
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /** Lade"balken"Popup initialisieren    **/
        loadingFrame = new JFrame("Loading");
        loadingFrame.setSize(200, 200);
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.add(new JLabel( new ImageIcon(Spielstein.class.getResource("images/loading.png"))));
        loadingFrame.setAlwaysOnTop(true);
        loadingFrame.setUndecorated(true);
        
        /** Werte initialisieren */  
        this.gamefield_x = DEFAULT_WIDTHFIELDS;
        this.gamefield_y = DEFAULT_HEIGHTFIELDS;
        this.colors = DEFAULT_COLORS;       
        this.comps = new HashSet<>();
        this.frameheigth = guiFrame.getHeight();
        this.framewidth = guiFrame.getWidth();
        this.isEndlessMode = false;
        this.gui = this;       
        
        /** Menüelemente hinzufügen    */  
        guiFrame.setJMenuBar(new mahki.MenuBar(this)); 
        
        /** Listener für Veränderung der Fenstergröße    */         
        guiFrame.getRootPane().addComponentListener(new ComponentListener(){            
           
            public void componentResized(ComponentEvent evt) {                
                
                if(guiFrame.getWidth() != framewidth || guiFrame.getHeight() != frameheigth){
                    frameheigth = guiFrame.getHeight();
                    framewidth = guiFrame.getWidth();
                    resizeGame();
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        
        /** Spielfeld erzeugen und anzeigen lassen */  
        startGame();  
    }
    
    /**
     * Erzeugt ein neues Spielfeld und zeigt dieses anschließend in der GUI
     */
    void startGame(){
        
        setLoading();
        /*  Neues Spielfeld als Thread erzeugen um Ladescreen normal anzeigen zu lassen */
        newGameThread = new Thread(new Runnable() {

            Thread thisthread;
            @Override
            public void run() {
               
                /*  Highscore neu laden falls für anderes Spielfeld generiert */
                if(highscore == null || highscore.endless != isEndlessMode || highscore.width != gamefield_x || highscore.height != gamefield_y || highscore.colors != colors){

                    highscore = new HighScore(colors, gamefield_x, gamefield_y, isEndlessMode);
                    highscore.refresh();
                }
                /*  Spielfeld initialisieren, Monstergröße festlegen und Anzeige aktualisieren  */
                monstersize = Math.min(guiFrame.getWidth()/gamefield_x - BORDERWIDTHX, guiFrame.getHeight()/gamefield_y - BORDERWIDTHY);        
                game = new Spielfeld(gamefield_x, gamefield_y, colors);
                game.fuellen(monstersize, monstersize, gui);        
                guiFrame.setVisible(true);
                refresh();
                unsetLoading();
            }
        });
        newGameThread.start();
    }

    /**
     * Füllt die GUI mit dem aktuellen Zustand des Spielfeldes, ausserdem Punkte und Züge des Spiels
     */  
    void refresh() {
        
        /*  Bisherige Komponenten entfernen */
        for(Component c : comps)
            guiFrame.remove(c);
        
        /*  Layouten der gui    */
        GridBagLayout gridBag = new GridBagLayout();       
        GridBagConstraints cons = new GridBagConstraints();        
        cons.fill = GridBagConstraints.VERTICAL;        
        guiFrame.setLayout(new BorderLayout(0, 0));
        
        /*  JPanel für das Spielfeld erzeugen und mit Spielfiguren füllen   */
        JPanel p1 = new JPanel(gridBag);
        p1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        comps.add(p1);
        for(int y = 0; y < gamefield_y; y++)
            for(int x = 0; x < gamefield_x; x++){
                
               cons.gridx = x;
               cons.gridy = y;
                Spielstein b = game.getSteinAtPostion(x, y);    
                if(b != null){                    
                    p1.add(b, cons);
                }
                else{
                    JLabel p = new JLabel();
                    ImageIcon image = new ImageIcon(Spielfeld.class.getResource("images/empty.png"));
                    Image resized = image.getImage().getScaledInstance(monstersize,monstersize, Image.SCALE_SMOOTH);
                    image.setImage(resized);
                    p.setIcon(image); 
                    p1.add(p, cons);
                }
            }        
        guiFrame.add(p1, BorderLayout.CENTER);  
        
        /* Jlabel für Punkte und Spielzüge erstellen    */
        JLabel stats = new JLabel(" Punkte: " + game.getPunkte() + " " + " " + " " + " Züge: " + game.getTurns());
        stats.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        guiFrame.add(stats, BorderLayout.SOUTH);
        comps.add(stats);
        
        guiFrame.validate();
        checkWin();               
    }
    
    
    /**
     * Passt die größe der Spielfiguren der des Frames an. Wird als Thread ausgeführt. Nur der letzte gestartete Restart-Thread läuft durch sofern Threads parallel laufen
     */
    void resizeGame(){
        
        setLoading();                   
        /*  Resize der Spielfiguren in eigenen Thread um Ladescreen normal anzeigen zu lassen   */           
        resizeThread = new Thread(new Runnable() {                             

            @Override
            public void run() {
               Thread thisthread = resizeThread;
               monstersize = Math.min(guiFrame.getWidth()/gamefield_x - BORDERWIDTHX, guiFrame.getHeight()/gamefield_y - BORDERWIDTHY);  
               for(Spielstein s : game.getSpielsteine()){                   
                   
                   if(!resizeThread.equals(thisthread)){                        /*   Thread beenden falls neuer resizeThread gestartet wurde */                       
                       return;
                   }                   
                   if(s == null)
                       continue;
                   s.resizeIcon(monstersize, monstersize);
                }
                refresh(); 
                unsetLoading();
            }
        });
        resizeThread.start();      
        
    }

    /**
     * Spielzug rückgängig machen und Spielfeld aktualusieren
     */
    void undoTurn() {
        game.undo();
        refresh();
    }

    /**
     * Prüfen ob das Spielfeld abgeräumt bzw. kein weiter Zug mehr möglich ist. Danach Neustart des Spiels
     */
    private void checkWin() {
       if(game.isFeldleer()){
           game.vergibPunkte(game.countLeereSpalten() * 10);
           if(isEndlessMode){
               
                JOptionPane.showMessageDialog(guiFrame, "Leergeräumt, das nächste Feld wartet. Deine Punkte: " + game.getPunkte());
                this.game.fuellen(this.gamefield_x, this.gamefield_y, this);                
                this.resizeGame();
                this.refresh();
           }
           else{
               if(!highscore.isHighScoreEntry(game.getPunkte()))
                    JOptionPane.showMessageDialog(guiFrame, "Du hast gewonnen.\nErreichte Punkte: " + game.getPunkte());
               else
                    highscore.setVisible(true);
               this.startGame();
           }
        }
        else if(!game.isZugMoeglich()){            
            game.vergibPunkte(game.countLeereSpalten() * 10);
            if(!highscore.isHighScoreEntry(game.getPunkte()))
                JOptionPane.showMessageDialog(guiFrame, "Du kannst das Feld nicht mehr abräumen.\nErreichte Punkte: " + game.getPunkte());
            else
                highscore.setVisible(true);
            this.startGame();
        }
    }
    
    /**
     * Ladescreen anzeigen
     */
    void setLoading(){
        loadingFrame.setVisible(true);
    }
    
    /**
     * Ladescreen verbergen
     */
    void unsetLoading(){
        loadingFrame.setVisible(false);
    }
}