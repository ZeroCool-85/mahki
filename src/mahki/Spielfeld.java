/*
 * Spielfeld Klasse für das Spiel Mahki
 */
package mahki;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Dennis Walz und Michael Holst
 */
public class Spielfeld{    
    
    private int combo;  /* Zähler für Anzahl gemeinsam entferneter Blöcke  */  
    private int anzahlFarben;   /* Anzahl zu generierender Farben  */      
    private Spielstein[][] feld;    /* Spielfeld */
    private ArrayList <Spielstein[][]> undolist = new ArrayList();  /* Liste gespiecherter Spielfeldzustände für Undo */
    private ArrayList <Integer> undopunkte = new ArrayList(); /* Liste gespeicherter Punktezuwächse für Undo */
    private int punkte; /* Durch Abräumen erhaltene Punkte */
    private int turns; /* Gemachte Züge */
    
    /**
     * Erzeugt ein neues Spielfeld (leer, muss zunächst durch fuellen() gefüllt werden)
     * @param xgroesse Anzahl der Felder in der Breite
     * @param ygroesse  Anzahl der Felder in der Höhe
     * @param anzahlFarben Anzahl unterschiedlicher Farben (2-5)
     */
    Spielfeld(int xgroesse, int ygroesse, int anzahlFarben){
        
        this.feld = new Spielstein[ygroesse][xgroesse];
        this.anzahlFarben = anzahlFarben;
        this.punkte = 0;
        this.turns = 0;
    }
    
    /**
     * Löst alle durch Auswahl eines Spielsteins resultierenden Methoden aus. 
     * Im Detail:
     * <li>Speichern des Feldes zum Undo
     * <li>Zusammenziehen des Feldes (Schließen der Lücken) vertikal
     * <li>Zusammenziehen des Feldes horizontal
     * <li>Punktevergabe
     * <li>Zählen der Spielzüge
     * @param stein Ausgewählter Spielstein
     * @return Anzahl mitzerstörter Spielsteine
     */
    public int clickSpielstein (Spielstein stein){
        
        if(!hatUmliegendeGleicheSteine(getSteinPostionX(stein), getSteinPostionY(stein), stein.getColor()))
            return 0;
        
        combo = 0;
        int punkte = 0;
        
        //Aktuelles Feld speichern
        Spielstein[][] feldtmp = new Spielstein[feld.length][feld[0].length];
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                feldtmp[y][x] = feld[y][x]; 
        undolist.add(feldtmp);
        
        //Steine entfernen und einrücken        
        removeSpielstein(stein);    
        punkte = combo * 2 - 2;
        undopunkte.add(punkte);        
        feldVertiaklZusammenfallenLassen();
        feldHorizontalZusammenfallenLassen();
        vergibPunkte(punkte);  
        turns++;
        return combo;
    }
    
    /**
     * Alle Spielsteine des Spielfeldes holen
     * @return Spielsteine als Array
     */
    Spielstein[] getSpielsteine(){
        
        int count = 0;
        Spielstein[] liste = new Spielstein[feld.length * feld[0].length];
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                liste[count++] = feld[y][x];
        return liste;
    }
    
    /**
     * Überprüft ob das Spielfeld komplett abgeräumt wurde
     * @return true wenn leer, false wenn Steine übrig
     */
    public boolean isFeldleer(){
        
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                if(feld[y][x] != null)
                    return false;
        return true;
    }
    
    /**
     * Spaltenanzahl des Spielfeldes
     * @return Spaltenanzahl des Spielfeldes
     */
    public int getWidth(){
        return feld[0].length;
    }
    /**
     * Zeilenanzahl des Spielfeldes
     * @return Zeilenanzahl des Spielfeldes
     */
    public int getHeight(){
        return feld.length;
    }
    
    /**
     * Undo der letzten Spielfeldveränderung
     * @return true wenn undo erfolgreich, false wenn keine Spielfeldveränderungen gespeichert
     */
    public boolean undo() {
        
        if(undolist.isEmpty())
            return false;
        feld = undolist.get(undolist.size() - 1);
        punkte -= undopunkte.get(undopunkte.size() - 1);
        undolist.remove(undolist.size() - 1);
        undopunkte.remove(undopunkte.size() - 1);
        
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                if(feld[y][x] != null)
                    feld[y][x].resetIcon();
        turns--;
        return true;
    }

    /**
     * Überprüfung ob noch ein Zug möglich ist
     * @return true wenn möglich, false wenn nicht
     */
    public boolean isZugMoeglich(){
        
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                if(getSteinAtPostion(x, y) != null)
                    if(       ( getSteinAtPostion(x + 1, y) != null && getSteinAtPostion(x + 1, y).getColor().equals(getSteinAtPostion(x, y).getColor()) )                            
                            ||( getSteinAtPostion(x - 1, y) != null && getSteinAtPostion(x - 1, y).getColor().equals(getSteinAtPostion(x, y).getColor()) )
                            ||( getSteinAtPostion(x, y + 1) != null && getSteinAtPostion(x, y + 1).getColor().equals(getSteinAtPostion(x, y).getColor()) )
                            ||( getSteinAtPostion(x, y - 1) != null && getSteinAtPostion(x, y - 1).getColor().equals(getSteinAtPostion(x, y).getColor()) )
                       )
                       return true;
        return false;
    }

    /**
     * Füllen des Spielfeldes
     * @param widthStein Breite des Spielsteins in Pixeln
     * @param heightStein Höhe des Spielsteins in Pixeln
     * @param gui GUI in die das Spielfeld, bzw. die Spielsteinbuttons eingebunden werden
     */
    public void fuellen(int widthStein, int heightStein, GUI gui) {
        
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                feld[y][x] = new Spielstein(Spielstein.getRandomColor(this.anzahlFarben), widthStein, heightStein, gui);
    }
    
    /**
     * Überführung des Spielfeldes in einen String mit Symbolen anstelle von Farben
     * @return Spielfeld als String
     */    
    @Override
    public String toString(){
        String s = "  |";
        for(int x = 0; x < feld[0].length; x++)
            s+=" "+x + " | ";
        s+="\n";
        for(int y = 0; y < feld.length; y++){
            s += y + " | ";
            for(int x = 0; x < feld[0].length; x++){
                if(feld[y][x] != null)
                    s+= Spielstein.colorToSymbol(feld[y][x].getColor()) + " | ";
                else
                    s+= "☐ | ";
            }
            s += "\n";
        }
        return s;
    }
    
    /**
     * Abfrage eines Spielsteins anhand der Koordinaten im Spielfeld
     * @param x x-Position
     * @param y y-Position
     * @return Spielstein wenn Feld belegt, null wenn Feld nicht belegt oder Feld nicht vorhanden
     */
    public Spielstein getSteinAtPostion(int x, int y){
        
        if(feld.length < y +1 || feld[0].length < x + 1 || x < 0 || y < 0)
            return null;        
        return feld[y][x];        
    }
   
    /**
     * Aktueller Punktestand
     * @return Aktueller Punktestand
     */
    public int getPunkte(){
        return this.punkte;
    }
    
    /**
     * Fügt einen Spielstein zum Feld hinzu
     * @param stein Spielstein
     * @param x x-Postion
     * @param y y-Position
     */
    private void addSpielstein(Spielstein stein, int x, int y){
        
        if(feld.length < y +1 || feld[0].length < x + 1){
            System.err.println("Error: Feld exisitiert nicht");
            return;
        }
        feld[y][x] = stein;
    }
    
    /**
     * Spielfeldkoordinate Y eines Spielsteins
     * @param stein Spielstein
     * @return Spielfeldkoordinate Y eines Spielsteins
     */
    private int getSteinPostionX(Spielstein stein){
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                if(feld[y][x] != null && feld[y][x].equals(stein))
                    return x;
        return 0;
    }
    
    /**
     * Spielfeldkoordinate X eines Spielsteins
     * @param stein Spielstein
     * @return Spielfeldkoordinate X eines Spielsteins
     */
    private int getSteinPostionY(Spielstein stein){
        for(int y = 0; y < feld.length; y++)
            for(int x = 0; x < feld[0].length; x++)
                if(feld[y][x] != null && feld[y][x].equals(stein))
                    return y;
        return 0;
    }
     
    /**
     * Entfernt einen Spielstein aus dem Feld und
     * <li>Erhöht den Combozähler
     * <li>Startet die Explosionsanimation des Spielsteins
     * <li>Entfernt umliegende Steine gleicher Farbe
     * @param stein 
     */
    private void removeSpielstein (Spielstein stein){
        
       removeSpielstein(getSteinPostionX(stein), getSteinPostionY(stein));                                   
    }
    /**
     * Entfernt einen Spielstein aus dem Feld und
     * <li>Erhöht den Combozähler
     * <li>Startet die Explosionsanimation des Spielsteins
     * <li>Entfernt umliegende Steine gleicher Farbe
     * @param x Spielfeldkoordinate x
     * @param y Spielfeldkoordinate y
     */
    private void removeSpielstein (int x, int y){
        
        Color color = getSteinAtPostion(x, y).getColor();
        combo++;
        getSteinAtPostion(x, y).explode();
        feld[y][x] = null;        
        removeUmliegendeSteine(x, y, color);
    }
    
    /**
     * Überprüfung ob es an einer Position im Spielfeld umgliegend Steine der gleichen Farbe gibt
     * @param x Spiefledkoordinate x
     * @param y Spielfeldkoordinate y
     * @param color Farbe des zentralen Spielsteins
     * @return true wenn Stein gleicher Farbe in Umgebung, False wenn kein Stein gleicher Farbe vorhanden
     */
    private boolean hatUmliegendeGleicheSteine(int x, int y, Color color){
        
        if(getSteinAtPostion(x + 1, y) != null && getSteinAtPostion(x + 1, y).getColor().equals(color) )            
            return true;
        
        if(getSteinAtPostion(x - 1, y) != null && getSteinAtPostion(x - 1, y).getColor().equals(color) )
            return true;
        
        if(getSteinAtPostion(x, y +1) != null && getSteinAtPostion(x, y +1).getColor().equals(color) )
            return true;
        
        if(getSteinAtPostion(x, y -1) != null && getSteinAtPostion(x, y -1).getColor().equals(color) )
            return true;
        return false;
    }
    
    /**
     * Entfernt alle umliegenden Spielsteine entsprechender Farbe
     * @param x Spielsteinposition x
     * @param y Spielsteinposition y
     * @param color Farbe des zentralen Spielsteins
     */
    private void removeUmliegendeSteine(int x, int y, Color color){
        
        if(getSteinAtPostion(x + 1, y) != null && getSteinAtPostion(x + 1, y).getColor().equals(color) )            
            removeSpielstein(getSteinAtPostion(x + 1, y));
        
        if(getSteinAtPostion(x - 1, y) != null && getSteinAtPostion(x - 1, y).getColor().equals(color) )
            removeSpielstein(getSteinAtPostion(x - 1, y));
        
        if(getSteinAtPostion(x, y +1) != null  && getSteinAtPostion(x, y +1).getColor().equals(color) )
            removeSpielstein(getSteinAtPostion(x, y +1));
        
        if(getSteinAtPostion(x, y -1) != null  && getSteinAtPostion(x, y -1).getColor().equals(color) )
            removeSpielstein(getSteinAtPostion(x, y -1));
    }
    
    /**
     * Spielsteine Vertiakl zusammenrücken falls Lücken im Spielfeld
     */
    private void feldVertiaklZusammenfallenLassen(){
        
        for(int y = feld.length - 1; y > 0; y--)
            for(int x = 0; x < feld[0].length; x++)
                if(feld[y][x] == null)                    
                    for(int ytmp = y; ytmp >= 0; ytmp--)
                        if(feld[ytmp][x] != null){
                            
                            feld[y][x] = feld[ytmp][x];
                            feld[ytmp][x] = null;
                            break;
                        }      
    }    
    
    /**
     * Spielsteine horizontal zusammenrücken falls Lücke am Boden
     */
    private void feldHorizontalZusammenfallenLassen(){
        
        for(int x = 0; x < feld[0].length; x++)
            if(feld[feld.length - 1][x] == null){
                
                for(int xtmp = x; xtmp < feld[0].length; xtmp++)   { 
                    
                    if(feld[feld.length - 1][xtmp] != null){
                        
                        for(int y = 0; y < feld.length; y++){
                 
                            feld[y][x] = feld[y][xtmp];
                            feld[y][xtmp] = null;                        
                        }
                        
                        break;
                    }
                }
            }
    }    

    /**
     * Punkte vergeben
     * @param punkte Anzahl zu addierender Punkte
     */
    void vergibPunkte(int punkte) {
        this.punkte += punkte;
    }
    
    /**
     * Zählen der leeren Spalten
     * @return Anzahl leerer Spalten
     */
    int countLeereSpalten(){
        int count = 0;
        for(int i = 0; i < feld[0].length; i++)
            if(feld[feld.length - 1][i] == null)
                count++;
        return count;
    }
    /**
     * Gemachte Züge dieses Spielfeldes
     * @return Züge
     */
    int getTurns() {
        return turns;
    }
    
}
