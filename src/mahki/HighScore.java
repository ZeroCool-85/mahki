/*
 * Klasse zur Verwaltung der Highscore in Mahki
 */

package mahki;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Dennis Walz und Michael Holst
 */
public class HighScore extends JFrame{

    String[] list = new String[10]; /*  Liste aller Highscoredatensätze für die aktuelle Konfiguration  */
    final int colors;   /*  Anzahl der Farben der aktuellen Konfiguration   */
    final int width;    /*  Breite der aktuellen Konfiguration   */
    final int height;    /*  Höhe der aktuellen Konfiguration   */
    private String path;     /*  Pfad zur Speicherdatei   */
    private HashSet<Component> comps = new HashSet();    /*  Aktuelle Komponenten dieses Frames   */
    final boolean endless;  
    
    /**
     * Klasse zur Verwaltung und Anzeige der Highscore im Spiel Mahki
     * @param colors Anzahl der Farben des zur Highscore gehörenden Spiels
     * @param width Breite der Farben des zur Highscore gehörenden Spiels
     * @param height Höhe der Farben des zur Highscore gehörenden Spiels
     * @param isEndless Endlosmodus des zur Highscore gehörenden Spiels 
     */
    HighScore(int colors, int width, int height, boolean isEndless){
        
        this.endless = isEndless;
        this.colors = colors;
        this.width = width;
        this.height = height;
        this.path = "Highscore_" + colors + "_" + width + "_" + height + "_" + isEndless + ".txt";  
        this.setTitle("Highscore");
        this.setBounds(150, 150, 300, 300);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setAlwaysOnTop(true);
    }   
    
    /**
     * Lädt die zur aktuellen Konfiguration gespeicherte Highscore ein und formatiert diese zur Anzeige im JFrame vom Objekt HighScore
     */
    void refresh(){
        
        /*  Enfernen bisheriger Komponenten */
        for(Component c : comps)
            this.remove(c);
        
        /*  Layout des Frame    */        
        GridBagLayout gridBag = new GridBagLayout();
        this.setLayout(gridBag);        
        this.loadScores();
        GridBagConstraints cons = new GridBagConstraints(); 
        cons.anchor = GridBagConstraints.WEST;
        
        /*  Label für Überschrift erzeugen  */
        String endlessString = "- Endlosmodus -"; 
        if(!endless)
            endlessString = "";
        JLabel title = new JLabel("<html><body><center>Highscore<br>" + width + "x" + height + " mit " + colors + " Farben<br><font color = #FF0000>" + endlessString + "</font></center></body></html>");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        cons.gridy = 0;
        cons.weighty = 3;
        this.add(title, cons); 
        comps.add(title);
        
        /*  JPanel für die Highscoreliste erzeugen  */
        cons.gridy = 2;
        cons.gridwidth = 300;
        cons.gridx = 0;
        JPanel highPanel = getListAsPanel();        
        this.add(highPanel, cons);     
        comps.add(highPanel);
       
        this.validate();
    }
 
    /**
     * Abfrage ob Spieler mit seinen Punkten einenn Eintrag in die Highscore bekommt, falls Ja bekommt der Spieler einen EingabeDialog für seinen Namen, danach wird beantwortet ob eine Eintragung vorgenommen wurde
     * @param points Punkte des Spiels
     * @return true wenn Highscoreeintrag false falls nicht
     */
    boolean isHighScoreEntry(int points){
        
        if(isHighEnoughForHighscore(points)){
            new HighScore_NewScoreDialog(this, points);
            return true;
        }
        return false;
    }
    
    /**
     * Highscore Eintragungen als JPanel formatieren
     * @return Highscore Eintragungen als JPanel
     */
    private JPanel getListAsPanel() {
        GridBagLayout gridBag = new GridBagLayout(); 
        JPanel p = new JPanel(gridBag);
        GridBagConstraints cons = new GridBagConstraints();        
        cons.fill = GridBagConstraints.VERTICAL;
        cons.anchor = GridBagConstraints.WEST;
        
        for(int i = 0; i < list.length; i++){            
            cons.gridy = i + 1;
            cons.gridx = 1;
            if(list[i] == null){     
                p.add(new JLabel(i+1 + ". "), cons); 
            }
            else{      
                p.add(new JLabel(i+1 + ". " + list[i].split(":")[0] + " "), cons);
                cons.gridx = 2;
                p.add(new JLabel(" " + list[i].split(":")[1] + " Punkte"), cons);
            }           
        }
        return p;
    }
    
    /**
     * Überprüfung ob zu überprüfende Punkte für eine Eintragung in die Highscore reichen
     * @param points zu überprüfende Punkte
     * @return true wenn Eintragung möglich, false wenn nicht
     */
    boolean isHighEnoughForHighscore(int points){
        
        for(int i = 0; i < list.length; i++)            
            if(list[i] == null || Integer.parseInt(list[i].split(":")[1]) < points)
               return true;           
        return false;
    }
    
    /**
     * Hinzufügen eines neuen Scores in die Highscore. Eintragung wird nur vorgenommen falls Punkte größer als die bereits eingetragenen Spiele
     * @param name Name des Spielers
     * @param points Punkte des Spielers
     */
    void addScore(String name, int points){
              
        String tmp = null;
        for(int i = 0; i < list.length; i++){
            
            if(tmp != null){
                
                String tmp2 = list[i];
                list[i] = tmp;
                if(tmp2 == null)
                    return;
                tmp = tmp2;
            }
            else     
                if(list[i] == null || Integer.parseInt(list[i].split(":")[1]) < points){
                    
                    tmp = list[i];                   
                    list[i] = name + ":" + points; 
                    if(tmp == null)
                        return;
                      
                }                       
        }
    }
    
     /**
     * Laden der Highscore aus einer gespeicherten Textdatei falls diese exisitiert
     */
    private void loadScores()
    {
        String strLine;
        BufferedReader br;
        DataInputStream in;
        FileInputStream fstream;
        int count = 0;
        
        try {
            File f = new File(path);
            if (!f.exists())
                return;

            fstream = new FileInputStream(path);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));           

            while ((strLine = br.readLine()) != null && count < list.length)                
                list[count++] = strLine;
            
            in.close();
            
        } catch (Exception ex) {
             System.err.println("Fehler beim Laden der Highscore-Datei ("+path+"): " + ex.getMessage());
        }	
    }
    
    /**
     * Speichern der Highscore für die aktuelle Konfiguration in eine Textdatei
     */
    public void saveScores()
    {
        File f = new File(path);
        if (!f.exists()) {

            try {
                f.createNewFile();
            } catch (IOException ex) {
                System.err.println("Fehler beim Erstellen der Highscore-Datei ("+path+"): " + ex.getMessage());
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            for (int i = 0; i < list.length; i++) {
                
                if(list[i] == null)
                    break;
                out.write(list[i]+"");
                out.newLine(); 
            }
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.err.println("Fehler beim Speichern der Highscore-Datei ("+path+"): " + ex.getMessage());
        }
    }

}
